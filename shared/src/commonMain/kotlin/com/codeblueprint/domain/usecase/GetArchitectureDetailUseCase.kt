package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.ArchitecturePattern
import com.codeblueprint.domain.repository.ArchitectureRepository

/**
 * 아키텍처 패턴 상세 조회 UseCase
 */
class GetArchitectureDetailUseCase(
    private val repository: ArchitectureRepository
) {
    suspend operator fun invoke(id: String): ArchitecturePattern? {
        return repository.getArchitectureById(id)
    }
}
