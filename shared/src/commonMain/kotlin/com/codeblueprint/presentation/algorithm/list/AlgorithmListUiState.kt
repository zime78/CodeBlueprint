package com.codeblueprint.presentation.algorithm.list

import com.codeblueprint.domain.model.AlgorithmCategory

/**
 * 알고리즘 목록 화면 UI 상태
 */
sealed class AlgorithmListUiState {
    /**
     * 로딩 상태
     */
    object Loading : AlgorithmListUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val algorithmsByCategory: Map<AlgorithmCategory, List<AlgorithmUiModel>>,
        val learningProgress: Float,
        val completedCount: Int,
        val totalCount: Int
    ) : AlgorithmListUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : AlgorithmListUiState()
}

/**
 * 알고리즘 UI 모델
 */
data class AlgorithmUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: AlgorithmCategory,
    val purpose: String,
    val timeComplexity: String, // average 표시
    val spaceComplexity: String,
    val difficulty: Int,
    val frequency: Int,
    val isBookmarked: Boolean,
    val isCompleted: Boolean
)

/**
 * 알고리즘 목록 화면 이벤트
 */
sealed class AlgorithmListEvent {
    /**
     * 알고리즘 클릭
     */
    data class OnAlgorithmClick(val algorithmId: String) : AlgorithmListEvent()

    /**
     * 북마크 토글
     */
    data class OnBookmarkToggle(val algorithmId: String) : AlgorithmListEvent()

    /**
     * 카테고리 확장/축소 토글
     */
    data class OnCategoryToggle(val category: AlgorithmCategory) : AlgorithmListEvent()

    /**
     * 검색 클릭
     */
    object OnSearchClick : AlgorithmListEvent()

    /**
     * 새로고침
     */
    object OnRefresh : AlgorithmListEvent()
}
