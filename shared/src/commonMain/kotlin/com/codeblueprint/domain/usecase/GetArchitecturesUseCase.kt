package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.ArchitecturePattern
import com.codeblueprint.domain.repository.ArchitectureRepository
import kotlinx.coroutines.flow.Flow

/**
 * 아키텍처 패턴 목록 조회 UseCase
 */
class GetArchitecturesUseCase(
    private val repository: ArchitectureRepository
) {
    operator fun invoke(): Flow<List<ArchitecturePattern>> {
        return repository.getAllArchitectures()
    }
}
