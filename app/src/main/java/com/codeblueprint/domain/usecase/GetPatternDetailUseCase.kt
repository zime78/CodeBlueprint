package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.repository.PatternRepository
import javax.inject.Inject

/**
 * 패턴 상세 정보 조회 UseCase
 */
class GetPatternDetailUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    /**
     * 패턴 ID로 상세 정보 조회
     */
    suspend operator fun invoke(patternId: String): DesignPattern? {
        return repository.getPatternById(patternId)
    }
}
