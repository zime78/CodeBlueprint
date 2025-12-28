package com.codeblueprint.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.usecase.GetLearningProgressUseCase
import com.codeblueprint.domain.usecase.SearchPatternsUseCase
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 검색 화면 ViewModel
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPatternsUseCase: SearchPatternsUseCase,
    private val getLearningProgressUseCase: GetLearningProgressUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

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
                // 디바운스 적용
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
                    getLearningProgressUseCase()
                ) { patterns, progressList ->
                    mapToUiState(query, patterns, progressList)
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
     * 북마크 토글
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
     * 검색 결과를 UI 상태로 변환
     */
    private fun mapToUiState(
        query: String,
        patterns: List<DesignPattern>,
        progressList: List<LearningProgress>
    ): SearchUiState {
        if (patterns.isEmpty()) {
            return SearchUiState.Empty(query)
        }

        val progressMap = progressList.associateBy { it.patternId }
        val queryLower = query.lowercase()

        val results = patterns.map { pattern ->
            val progress = progressMap[pattern.id]
            val matchedField = findMatchedField(pattern, queryLower)

            SearchResultUiModel(
                id = pattern.id,
                name = pattern.name,
                koreanName = pattern.koreanName,
                category = pattern.category,
                purpose = pattern.purpose,
                matchedField = matchedField,
                isBookmarked = progress?.isBookmarked ?: false,
                isCompleted = progress?.isCompleted ?: false
            )
        }

        return SearchUiState.Results(
            query = query,
            patterns = results,
            totalCount = results.size
        )
    }

    /**
     * 일치한 필드 찾기
     */
    private fun findMatchedField(pattern: DesignPattern, queryLower: String): MatchedField {
        return when {
            pattern.name.lowercase().contains(queryLower) -> MatchedField.NAME
            pattern.koreanName.contains(queryLower) -> MatchedField.KOREAN_NAME
            pattern.purpose.lowercase().contains(queryLower) -> MatchedField.PURPOSE
            pattern.characteristics.any { it.lowercase().contains(queryLower) } -> MatchedField.CHARACTERISTICS
            pattern.useCases.any { it.lowercase().contains(queryLower) } -> MatchedField.USE_CASES
            else -> MatchedField.PURPOSE
        }
    }
}
