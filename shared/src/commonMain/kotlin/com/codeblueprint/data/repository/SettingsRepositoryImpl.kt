package com.codeblueprint.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.codeblueprint.data.mapper.SettingsMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.Settings
import com.codeblueprint.domain.model.ThemeMode
import com.codeblueprint.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 설정 Repository 구현체
 */
class SettingsRepositoryImpl(
    private val database: CodeBlueprintDatabase,
    private val mapper: SettingsMapper
) : SettingsRepository {

    private val queries = database.codeBlueprintQueries

    init {
        // 설정 초기화 (없으면 기본값으로 생성)
        queries.initSettings()
    }

    override fun getSettings(): Flow<Settings> {
        return queries.getSettings()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                entity?.let { mapper.toDomain(it) } ?: mapper.getDefault()
            }
    }

    override suspend fun setTheme(theme: ThemeMode) {
        queries.updateTheme(theme.name)
    }

    override suspend fun setDefaultCodeLanguage(language: ProgrammingLanguage) {
        queries.updateDefaultCodeLanguage(language.name)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        queries.updateNotificationsEnabled(if (enabled) 1L else 0L)
    }
}
