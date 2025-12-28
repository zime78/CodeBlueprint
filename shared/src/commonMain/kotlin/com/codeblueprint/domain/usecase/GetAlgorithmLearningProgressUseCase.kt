package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.Flow

/**
 * 알고리즘 학습 진도 조회 UseCase
 */
class GetAlgorithmLearningProgressUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * 특정 알고리즘의 학습 진도 조회
     */
    operator fun invoke(algorithmId: String): Flow<LearningProgress?> {
        return repository.getLearningProgress(algorithmId)
    }

    /**
     * 모든 알고리즘 학습 진도 목록 조회
     */
    fun getAll(): Flow<List<LearningProgress>> {
        return repository.getAllAlgorithmLearningProgress()
    }

    /**
     * 전체 학습 진도율 조회 (0.0 ~ 1.0)
     */
    fun getOverallProgress(): Flow<Float> {
        return repository.getOverallProgress()
    }
}
