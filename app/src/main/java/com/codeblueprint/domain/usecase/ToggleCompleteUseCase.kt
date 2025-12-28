package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.repository.PatternRepository
import javax.inject.Inject

/**
 * 학습 완료 토글 UseCase
 */
class ToggleCompleteUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    /**
     * 패턴의 학습 완료 상태를 토글
     */
    suspend operator fun invoke(patternId: String) {
        repository.toggleComplete(patternId)
    }
}
