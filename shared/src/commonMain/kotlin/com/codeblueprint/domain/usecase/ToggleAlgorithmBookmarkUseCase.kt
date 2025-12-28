package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.repository.AlgorithmRepository

/**
 * 알고리즘 북마크 토글 UseCase
 */
class ToggleAlgorithmBookmarkUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * 알고리즘의 북마크 상태를 토글
     */
    suspend operator fun invoke(algorithmId: String) {
        repository.toggleBookmark(algorithmId)
    }
}
