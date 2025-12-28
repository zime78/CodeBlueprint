package com.codeblueprint.presentation.playground

import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.usecase.ExecuteCodeUseCase
import com.codeblueprint.platform.ClipboardService
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 코드 플레이그라운드 ViewModel
 */
class CodePlaygroundViewModel(
    private val executeCodeUseCase: ExecuteCodeUseCase,
    private val clipboardService: ClipboardService,
    private val initialCode: String = "",
    private val initialLanguage: ProgrammingLanguage = ProgrammingLanguage.KOTLIN
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<CodePlaygroundUiState>(CodePlaygroundUiState.Idle())
    val uiState: StateFlow<CodePlaygroundUiState> = _uiState.asStateFlow()

    private val _code = MutableStateFlow(initialCode.ifEmpty { getDefaultCode(initialLanguage) })
    val code: StateFlow<String> = _code.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(initialLanguage)
    val selectedLanguage: StateFlow<ProgrammingLanguage> = _selectedLanguage.asStateFlow()

    private val _copyMessage = MutableStateFlow<String?>(null)
    val copyMessage: StateFlow<String?> = _copyMessage.asStateFlow()

    /**
     * 이벤트 처리
     */
    fun onEvent(event: CodePlaygroundEvent) {
        when (event) {
            is CodePlaygroundEvent.OnCodeChange -> {
                _code.value = event.code
            }
            is CodePlaygroundEvent.OnLanguageChange -> {
                _selectedLanguage.value = event.language
                _code.value = getDefaultCode(event.language)
                _uiState.value = CodePlaygroundUiState.Idle()
            }
            is CodePlaygroundEvent.OnExecute -> {
                executeCode()
            }
            is CodePlaygroundEvent.OnClearOutput -> {
                _uiState.value = CodePlaygroundUiState.Idle()
            }
            is CodePlaygroundEvent.OnResetCode -> {
                _code.value = getDefaultCode(_selectedLanguage.value)
                _uiState.value = CodePlaygroundUiState.Idle()
            }
            is CodePlaygroundEvent.OnCopyCode -> {
                copyCode()
            }
            is CodePlaygroundEvent.OnCopyOutput -> {
                copyOutput()
            }
            is CodePlaygroundEvent.OnClearCopyMessage -> {
                _copyMessage.value = null
            }
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 코드 복사
     */
    private fun copyCode() {
        val currentCode = _code.value
        if (currentCode.isBlank()) {
            _copyMessage.value = "복사할 코드가 없습니다"
            return
        }

        val success = clipboardService.copyToClipboard(currentCode)
        _copyMessage.value = if (success) "코드가 복사되었습니다" else "복사에 실패했습니다"
    }

    /**
     * 출력 복사
     */
    private fun copyOutput() {
        val output = when (val state = _uiState.value) {
            is CodePlaygroundUiState.Idle -> state.output
            is CodePlaygroundUiState.Success -> state.result.output
            is CodePlaygroundUiState.Error -> state.message
            else -> ""
        }

        if (output.isBlank()) {
            _copyMessage.value = "복사할 출력이 없습니다"
            return
        }

        val success = clipboardService.copyToClipboard(output)
        _copyMessage.value = if (success) "출력이 복사되었습니다" else "복사에 실패했습니다"
    }

    /**
     * 코드 실행
     */
    private fun executeCode() {
        val currentCode = _code.value.trim()
        if (currentCode.isBlank()) {
            _uiState.value = CodePlaygroundUiState.Error("실행할 코드를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.value = CodePlaygroundUiState.Executing

            try {
                val result = executeCodeUseCase(currentCode, _selectedLanguage.value)
                if (result.success) {
                    _uiState.value = CodePlaygroundUiState.Success(result)
                } else {
                    _uiState.value = CodePlaygroundUiState.Error(
                        result.errorMessage ?: "실행 중 오류가 발생했습니다."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CodePlaygroundUiState.Error(
                    e.message ?: "알 수 없는 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 언어별 기본 코드
     */
    private fun getDefaultCode(language: ProgrammingLanguage): String {
        return when (language) {
            ProgrammingLanguage.KOTLIN -> DEFAULT_KOTLIN_CODE
            ProgrammingLanguage.JAVA -> DEFAULT_JAVA_CODE
            ProgrammingLanguage.PYTHON -> DEFAULT_PYTHON_CODE
            ProgrammingLanguage.JAVASCRIPT -> DEFAULT_JAVASCRIPT_CODE
            ProgrammingLanguage.SWIFT -> DEFAULT_SWIFT_CODE
        }
    }

    companion object {
        private val DEFAULT_KOTLIN_CODE = """
            // Singleton 패턴 예제
            object DatabaseConnection {
                init {
                    println("Database connection initialized")
                }

                fun query(sql: String) {
                    println("Executing query: ${'$'}sql")
                }
            }

            fun main() {
                DatabaseConnection.query("SELECT * FROM users")
                println("Singleton instance check: ${'$'}{DatabaseConnection === DatabaseConnection}")
            }
        """.trimIndent()

        private val DEFAULT_JAVA_CODE = """
            public class Singleton {
                private static Singleton instance;

                private Singleton() {
                    System.out.println("Singleton instance created");
                }

                public static Singleton getInstance() {
                    if (instance == null) {
                        instance = new Singleton();
                    }
                    return instance;
                }

                public static void main(String[] args) {
                    Singleton s1 = Singleton.getInstance();
                    Singleton s2 = Singleton.getInstance();
                    System.out.println("Same instance: " + (s1 == s2));
                }
            }
        """.trimIndent()

        private val DEFAULT_PYTHON_CODE = """
            # Singleton 패턴 예제
            class Singleton:
                _instance = None

                def __new__(cls):
                    if cls._instance is None:
                        cls._instance = super().__new__(cls)
                        print("Singleton instance created")
                    return cls._instance

            # 테스트
            s1 = Singleton()
            s2 = Singleton()
            print(f"Same instance: {s1 is s2}")
        """.trimIndent()

        private val DEFAULT_JAVASCRIPT_CODE = """
            // Singleton 패턴 예제
            const Singleton = (function() {
                let instance;

                function createInstance() {
                    console.log("Singleton instance created");
                    return { name: "Singleton" };
                }

                return {
                    getInstance: function() {
                        if (!instance) {
                            instance = createInstance();
                        }
                        return instance;
                    }
                };
            })();

            // 테스트
            const s1 = Singleton.getInstance();
            const s2 = Singleton.getInstance();
            console.log("Same instance: " + (s1 === s2));
        """.trimIndent()

        private val DEFAULT_SWIFT_CODE = """
            // Singleton 패턴 예제
            class Singleton {
                static let shared = Singleton()

                private init() {
                    print("Singleton instance created")
                }

                func doSomething() {
                    print("Singleton is doing something")
                }
            }

            // 테스트
            let s1 = Singleton.shared
            let s2 = Singleton.shared
            print("Same instance: \(s1 === s2)")
        """.trimIndent()
    }
}
