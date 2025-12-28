package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.Settings
import com.codeblueprint.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * 설정 데이터 접근을 추상화하는 Repository 인터페이스
 */
interface SettingsRepository {

    /**
     * 현재 설정 조회
     */
    fun getSettings(): Flow<Settings>

    /**
     * 테마 변경
     */
    suspend fun setTheme(theme: ThemeMode)

    /**
     * 기본 코드 언어 변경
     */
    suspend fun setDefaultCodeLanguage(language: ProgrammingLanguage)

    /**
     * 알림 설정 변경
     */
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
