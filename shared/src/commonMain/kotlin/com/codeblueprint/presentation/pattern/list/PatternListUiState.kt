package com.codeblueprint.presentation.pattern.list

import com.codeblueprint.domain.model.PatternCategory

/**
 * 패턴 목록 화면 UI 상태
 */
sealed class PatternListUiState {
    /**
     * 로딩 상태
     */
    object Loading : PatternListUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val patternsByCategory: Map<PatternCategory, List<PatternUiModel>>
    ) : PatternListUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : PatternListUiState()
}

/**
 * 패턴 UI 모델
 */
data class PatternUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: PatternCategory,
    val purpose: String,
    val difficulty: Int,
    val frequency: Int,
    val isBookmarked: Boolean
)

/**
 * 패턴 목록 화면 이벤트
 */
sealed class PatternListEvent {
    /**
     * 패턴 클릭
     */
    data class OnPatternClick(val patternId: String) : PatternListEvent()

    /**
     * 북마크 토글
     */
    data class OnBookmarkToggle(val patternId: String) : PatternListEvent()

    /**
     * 카테고리 확장/축소 토글
     */
    data class OnCategoryToggle(val category: PatternCategory) : PatternListEvent()

    /**
     * 검색 클릭
     */
    object OnSearchClick : PatternListEvent()

    /**
     * 새로고침
     */
    object OnRefresh : PatternListEvent()
}
