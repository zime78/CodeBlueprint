package com.codeblueprint.presentation.settings

import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.ThemeMode

/**
 * 설정 화면 UI 상태
 */
sealed class SettingsUiState {

    /**
     * 로딩 중
     */
    data object Loading : SettingsUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val theme: ThemeMode,
        val defaultCodeLanguage: ProgrammingLanguage,
        val notificationsEnabled: Boolean,
        val appVersion: String = "1.0.0"
    ) : SettingsUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : SettingsUiState()
}

/**
 * 설정 화면 이벤트
 */
sealed class SettingsEvent {
    data class OnThemeChange(val theme: ThemeMode) : SettingsEvent()
    data class OnLanguageChange(val language: ProgrammingLanguage) : SettingsEvent()
    data class OnNotificationsToggle(val enabled: Boolean) : SettingsEvent()
}
