package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.Flow

/**
 * 알고리즘 검색 UseCase
 */
class SearchAlgorithmsUseCase(
    private val repository: AlgorithmRepository
) {
    /**
     * 키워드로 알고리즘 검색
     *
     * @param query 검색 키워드 (이름, 한글명, 목적, 활용 예시에서 검색)
     * @return 검색 결과 알고리즘 목록
     */
    operator fun invoke(query: String): Flow<List<Algorithm>> {
        return repository.searchAlgorithms(query)
    }
}
