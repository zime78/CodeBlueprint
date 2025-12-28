package com.codeblueprint.presentation.settings

import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.ThemeMode
import com.codeblueprint.domain.usecase.GetSettingsUseCase
import com.codeblueprint.domain.usecase.SaveSettingsUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 설정 화면 ViewModel
 */
class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    /**
     * 설정 로드
     */
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading

            try {
                getSettingsUseCase().collect { settings ->
                    _uiState.value = SettingsUiState.Success(
                        theme = settings.theme,
                        defaultCodeLanguage = settings.defaultCodeLanguage,
                        notificationsEnabled = settings.notificationsEnabled
                    )
                }
            } catch (e: Exception) {
                _uiState.value = SettingsUiState.Error(
                    e.message ?: "설정을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnThemeChange -> setTheme(event.theme)
            is SettingsEvent.OnLanguageChange -> setDefaultLanguage(event.language)
            is SettingsEvent.OnNotificationsToggle -> setNotifications(event.enabled)
        }
    }

    /**
     * 테마 변경
     */
    private fun setTheme(theme: ThemeMode) {
        viewModelScope.launch {
            try {
                saveSettingsUseCase.setTheme(theme)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 기본 코드 언어 변경
     */
    private fun setDefaultLanguage(language: ProgrammingLanguage) {
        viewModelScope.launch {
            try {
                saveSettingsUseCase.setDefaultCodeLanguage(language)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 알림 설정 변경
     */
    private fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                saveSettingsUseCase.setNotificationsEnabled(enabled)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}
