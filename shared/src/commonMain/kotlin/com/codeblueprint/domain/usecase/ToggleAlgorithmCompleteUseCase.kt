package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.repository.AlgorithmRepository

/**
 * 알고리즘 학습 완료 토글 UseCase
 */
class ToggleAlgorithmCompleteUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * 알고리즘의 학습 완료 상태를 토글
     */
    suspend operator fun invoke(algorithmId: String) {
        repository.toggleComplete(algorithmId)
    }
}
