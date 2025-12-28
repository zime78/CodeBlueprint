package com.codeblueprint.presentation.search

import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.PatternCategory

/**
 * 검색 화면 UI 상태
 */
sealed class SearchUiState {
    /**
     * 초기 상태 (검색 전)
     */
    object Initial : SearchUiState()

    /**
     * 검색 중
     */
    object Searching : SearchUiState()

    /**
     * 검색 결과
     */
    data class Results(
        val query: String,
        val patterns: List<SearchResultUiModel>,
        val algorithms: List<AlgorithmSearchResultUiModel>,
        val totalCount: Int
    ) : SearchUiState()

    /**
     * 결과 없음
     */
    data class Empty(val query: String) : SearchUiState()

    /**
     * 에러
     */
    data class Error(val message: String) : SearchUiState()
}

/**
 * 패턴 검색 결과 UI 모델
 */
data class SearchResultUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: PatternCategory,
    val purpose: String,
    val matchedField: MatchedField,
    val isBookmarked: Boolean
)

/**
 * 알고리즘 검색 결과 UI 모델
 */
data class AlgorithmSearchResultUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: AlgorithmCategory,
    val purpose: String,
    val timeComplexity: String,
    val matchedField: MatchedField,
    val isBookmarked: Boolean
)

/**
 * 일치한 필드 타입
 */
enum class MatchedField {
    NAME,
    KOREAN_NAME,
    PURPOSE,
    CHARACTERISTICS,
    USE_CASES
}

/**
 * 검색 화면 이벤트
 */
sealed class SearchEvent {
    /**
     * 검색어 변경
     */
    data class OnQueryChange(val query: String) : SearchEvent()

    /**
     * 검색 실행
     */
    object OnSearch : SearchEvent()

    /**
     * 검색어 삭제
     */
    object OnClearQuery : SearchEvent()

    /**
     * 패턴 클릭
     */
    data class OnPatternClick(val patternId: String) : SearchEvent()

    /**
     * 북마크 토글
     */
    data class OnBookmarkToggle(val patternId: String) : SearchEvent()

    /**
     * 알고리즘 클릭
     */
    data class OnAlgorithmClick(val algorithmId: String) : SearchEvent()

    /**
     * 알고리즘 북마크 토글
     */
    data class OnAlgorithmBookmarkToggle(val algorithmId: String) : SearchEvent()
}
