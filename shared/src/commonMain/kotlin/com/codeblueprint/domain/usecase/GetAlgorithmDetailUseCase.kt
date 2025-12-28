package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.repository.AlgorithmRepository

/**
 * 알고리즘 상세 조회 UseCase
 */
class GetAlgorithmDetailUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * ID로 알고리즘 상세 정보 조회
     */
    suspend operator fun invoke(algorithmId: String): Algorithm? {
        return repository.getAlgorithmById(algorithmId)
    }

    /**
     * 관련 알고리즘 목록 조회
     */
    suspend fun getRelatedAlgorithms(ids: List<String>): List<Algorithm> {
        return repository.getRelatedAlgorithms(ids)
    }
}
