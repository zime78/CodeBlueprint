package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.Flow

/**
 * 알고리즘 목록 조회 UseCase
 */
class GetAlgorithmsUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * 전체 알고리즘 목록 조회
     */
    operator fun invoke(): Flow<List<Algorithm>> {
        return repository.getAllAlgorithms()
    }

    /**
     * 카테고리별 알고리즘 목록 조회
     */
    operator fun invoke(category: AlgorithmCategory): Flow<List<Algorithm>> {
        return repository.getAlgorithmsByCategory(category)
    }
}
