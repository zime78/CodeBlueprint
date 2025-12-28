package com.codeblueprint.domain.model

/**
 * 앱 설정 도메인 모델
 */
data class Settings(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val defaultCodeLanguage: ProgrammingLanguage = ProgrammingLanguage.KOTLIN,
    val notificationsEnabled: Boolean = true
)

/**
 * 테마 모드
 */
enum class ThemeMode(val displayName: String) {
    LIGHT("라이트"),
    DARK("다크"),
    SYSTEM("시스템 설정");

    companion object {
        fun fromString(value: String): ThemeMode {
            return entries.find { it.name == value } ?: SYSTEM
        }
    }
}
