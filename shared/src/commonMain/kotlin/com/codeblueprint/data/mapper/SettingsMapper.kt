package com.codeblueprint.data.mapper

import com.codeblueprint.db.SettingsEntity
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.Settings
import com.codeblueprint.domain.model.ThemeMode

/**
 * Settings 엔티티 <-> 도메인 모델 변환 Mapper
 */
class SettingsMapper {

    /**
     * DB 엔티티를 도메인 모델로 변환
     */
    fun toDomain(entity: SettingsEntity): Settings {
        return Settings(
            theme = ThemeMode.fromString(entity.theme),
            defaultCodeLanguage = ProgrammingLanguage.fromString(entity.default_code_language),
            notificationsEnabled = entity.notifications_enabled == 1L
        )
    }

    /**
     * 기본 설정 반환
     */
    fun getDefault(): Settings = Settings()
}
