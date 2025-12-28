package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.repository.PatternRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 패턴 목록 조회 UseCase
 *
 * 카테고리별 또는 전체 패턴 목록을 조회합니다.
 */
class GetPatternsUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    /**
     * 전체 패턴 목록 조회
     */
    operator fun invoke(): Flow<List<DesignPattern>> {
        return repository.getAllPatterns()
    }

    /**
     * 카테고리별 패턴 목록 조회
     */
    operator fun invoke(category: PatternCategory): Flow<List<DesignPattern>> {
        return repository.getPatternsByCategory(category)
    }
}
