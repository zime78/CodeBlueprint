package com.codeblueprint.presentation.bookmarks

import com.codeblueprint.domain.model.DesignPattern

/**
 * 북마크 화면 UI 상태
 */
sealed class BookmarksUiState {

    /**
     * 로딩 중
     */
    data object Loading : BookmarksUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val bookmarkedPatterns: List<BookmarkedPatternUiModel>
    ) : BookmarksUiState() {
        val isEmpty: Boolean get() = bookmarkedPatterns.isEmpty()
    }

    /**
     * 에러 상태
     */
    data class Error(val message: String) : BookmarksUiState()
}

/**
 * 북마크된 패턴 UI 모델
 */
data class BookmarkedPatternUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val categoryName: String,
    val purpose: String,
    val difficulty: Int,
    val isCompleted: Boolean
)

/**
 * 북마크 화면 이벤트
 */
sealed class BookmarksEvent {
    data class OnPatternClick(val patternId: String) : BookmarksEvent()
    data class OnRemoveBookmark(val patternId: String) : BookmarksEvent()
    data object OnRefresh : BookmarksEvent()
}
