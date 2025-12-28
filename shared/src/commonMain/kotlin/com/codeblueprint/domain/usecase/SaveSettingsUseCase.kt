package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.ThemeMode
import com.codeblueprint.domain.repository.SettingsRepository

/**
 * 설정 저장 UseCase
 */
class SaveSettingsUseCase(
    private val repository: SettingsRepository
) {
    /**
     * 테마 변경
     */
    suspend fun setTheme(theme: ThemeMode) {
        repository.setTheme(theme)
    }

    /**
     * 기본 코드 언어 변경
     */
    suspend fun setDefaultCodeLanguage(language: ProgrammingLanguage) {
        repository.setDefaultCodeLanguage(language)
    }

    /**
     * 알림 설정 변경
     */
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        repository.setNotificationsEnabled(enabled)
    }
}
