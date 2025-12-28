package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.repository.CodeExecutionRepository

/**
 * 코드 실행 UseCase
 */
class ExecuteCodeUseCase(
    private val codeExecutionRepository: CodeExecutionRepository
) {
    /**
     * 코드 실행
     *
     * @param code 실행할 소스 코드
     * @param language 프로그래밍 언어
     * @param expectedOutput 미리 정의된 예상 출력값 (있으면 시뮬레이션 대신 이 값 반환)
     * @return 코드 실행 결과
     */
    suspend operator fun invoke(
        code: String,
        language: ProgrammingLanguage,
        expectedOutput: String? = null
    ): CodeExecutionResult {
        return codeExecutionRepository.executeCode(code, language, expectedOutput)
    }
}
