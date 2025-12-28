package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.repository.PatternRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 패턴 검색 UseCase
 */
class SearchPatternsUseCase @Inject constructor(
    private val repository: PatternRepository
) {
    /**
     * 검색어로 패턴 검색
     *
     * @param query 검색어 (패턴명, 목적, 활용 예시에서 검색)
     */
    operator fun invoke(query: String): Flow<List<DesignPattern>> {
        return repository.searchPatterns(query)
    }
}
