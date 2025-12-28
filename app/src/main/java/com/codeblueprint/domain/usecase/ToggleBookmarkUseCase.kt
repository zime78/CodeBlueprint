package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.repository.PatternRepository
import javax.inject.Inject

/**
 * 북마크 토글 UseCase
 */
class ToggleBookmarkUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    /**
     * 패턴의 북마크 상태를 토글
     */
    suspend operator fun invoke(patternId: String) {
        repository.toggleBookmark(patternId)
    }
}
