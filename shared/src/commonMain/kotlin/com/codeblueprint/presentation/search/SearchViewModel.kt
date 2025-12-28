package com.codeblueprint.presentation.search

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.usecase.SearchAlgorithmsUseCase
import com.codeblueprint.domain.usecase.SearchPatternsUseCase
import com.codeblueprint.domain.usecase.ToggleAlgorithmBookmarkUseCase
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * 검색 화면 ViewModel
 */
class SearchViewModel(
    private val searchPatternsUseCase: SearchPatternsUseCase,
    private val searchAlgorithmsUseCase: SearchAlgorithmsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val toggleAlgorithmBookmarkUseCase: ToggleAlgorithmBookmarkUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    /**
     * 이벤트 처리
     */
    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                _query.value = event.query
                performDebouncedSearch(event.query)
            }
            is SearchEvent.OnSearch -> {
                performSearch(_query.value)
            }
            is SearchEvent.OnClearQuery -> {
                _query.value = ""
                _uiState.value = SearchUiState.Initial
            }
            is SearchEvent.OnBookmarkToggle -> {
                toggleBookmark(event.patternId)
            }
            is SearchEvent.OnAlgorithmBookmarkToggle -> {
                toggleAlgorithmBookmark(event.algorithmId)
            }
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 디바운스 적용 검색
     */
    private fun performDebouncedSearch(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // 300ms 디바운스
            performSearch(query)
        }
    }

    /**
     * 검색 실행
     */
    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Searching

            try {
                combine(
                    searchPatternsUseCase(query),
                    searchAlgorithmsUseCase(query)
                ) { patterns, algorithms ->
                    mapToUiState(query, patterns, algorithms)
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(
                    e.message ?: "검색 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 패턴 북마크 토글
     */
    private fun toggleBookmark(patternId: String) {
        viewModelScope.launch {
            try {
                toggleBookmarkUseCase(patternId)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 알고리즘 북마크 토글
     */
    private fun toggleAlgorithmBookmark(algorithmId: String) {
        viewModelScope.launch {
            try {
                toggleAlgorithmBookmarkUseCase(algorithmId)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 검색 결과를 UI 상태로 변환
     */
    private fun mapToUiState(
        query: String,
        patterns: List<DesignPattern>,
        algorithms: List<Algorithm>
    ): SearchUiState {
        if (patterns.isEmpty() && algorithms.isEmpty()) {
            return SearchUiState.Empty(query)
        }

        val queryLower = query.lowercase()

        val patternResults = patterns.map { pattern ->
            val matchedField = findPatternMatchedField(pattern, queryLower)

            SearchResultUiModel(
                id = pattern.id,
                name = pattern.name,
                koreanName = pattern.koreanName,
                category = pattern.category,
                purpose = pattern.purpose,
                matchedField = matchedField,
                isBookmarked = pattern.isBookmarked
            )
        }

        val algorithmResults = algorithms.map { algorithm ->
            val matchedField = findAlgorithmMatchedField(algorithm, queryLower)

            AlgorithmSearchResultUiModel(
                id = algorithm.id,
                name = algorithm.name,
                koreanName = algorithm.koreanName,
                category = algorithm.category,
                purpose = algorithm.purpose,
                timeComplexity = algorithm.timeComplexity.average,
                matchedField = matchedField,
                isBookmarked = algorithm.isBookmarked
            )
        }

        return SearchUiState.Results(
            query = query,
            patterns = patternResults,
            algorithms = algorithmResults,
            totalCount = patternResults.size + algorithmResults.size
        )
    }

    /**
     * 패턴에서 일치한 필드 찾기
     */
    private fun findPatternMatchedField(pattern: DesignPattern, queryLower: String): MatchedField {
        return when {
            pattern.name.lowercase().contains(queryLower) -> MatchedField.NAME
            pattern.koreanName.contains(queryLower) -> MatchedField.KOREAN_NAME
            pattern.purpose.lowercase().contains(queryLower) -> MatchedField.PURPOSE
            pattern.characteristics.any { it.lowercase().contains(queryLower) } -> MatchedField.CHARACTERISTICS
            pattern.useCases.any { it.lowercase().contains(queryLower) } -> MatchedField.USE_CASES
            else -> MatchedField.PURPOSE
        }
    }

    /**
     * 알고리즘에서 일치한 필드 찾기
     */
    private fun findAlgorithmMatchedField(algorithm: Algorithm, queryLower: String): MatchedField {
        return when {
            algorithm.name.lowercase().contains(queryLower) -> MatchedField.NAME
            algorithm.koreanName.contains(queryLower) -> MatchedField.KOREAN_NAME
            algorithm.purpose.lowercase().contains(queryLower) -> MatchedField.PURPOSE
            algorithm.characteristics.any { it.lowercase().contains(queryLower) } -> MatchedField.CHARACTERISTICS
            algorithm.useCases.any { it.lowercase().contains(queryLower) } -> MatchedField.USE_CASES
            else -> MatchedField.PURPOSE
        }
    }
}
