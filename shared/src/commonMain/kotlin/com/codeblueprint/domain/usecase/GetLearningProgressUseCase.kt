package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.repository.PatternRepository
import kotlinx.coroutines.flow.Flow

/**
 * 학습 진도 조회 UseCase
 */
class GetLearningProgressUseCase(
    private val repository: PatternRepository
) {
    /**
     * 특정 패턴의 학습 진도 조회
     */
    operator fun invoke(patternId: String): Flow<LearningProgress?> {
        return repository.getLearningProgress(patternId)
    }

    /**
     * 모든 학습 진도 목록 조회
     */
    fun getAll(): Flow<List<LearningProgress>> {
        return repository.getAllLearningProgress()
    }

    /**
     * 전체 학습 진도율 조회 (0.0 ~ 1.0)
     */
    fun getOverallProgress(): Flow<Float> {
        return repository.getOverallProgress()
    }
}
