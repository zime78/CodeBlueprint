package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.Settings
import com.codeblueprint.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * 설정 조회 UseCase
 */
class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    /**
     * 현재 설정 조회
     */
    operator fun invoke(): Flow<Settings> = repository.getSettings()
}
