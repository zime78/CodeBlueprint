package com.codeblueprint.presentation.bookmarks

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.repository.PatternRepository
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * 북마크 화면 ViewModel
 */
class BookmarksViewModel(
    private val patternRepository: PatternRepository,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<BookmarksUiState>(BookmarksUiState.Loading)
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    init {
        loadBookmarks()
    }

    /**
     * 북마크 목록 로드
     */
    private fun loadBookmarks() {
        viewModelScope.launch {
            _uiState.value = BookmarksUiState.Loading

            try {
                combine(
                    patternRepository.getBookmarkedPatterns(),
                    patternRepository.getAllLearningProgress()
                ) { patterns, progressList ->
                    mapToUiState(patterns, progressList)
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = BookmarksUiState.Error(
                    e.message ?: "북마크를 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: BookmarksEvent) {
        when (event) {
            is BookmarksEvent.OnRemoveBookmark -> removeBookmark(event.patternId)
            is BookmarksEvent.OnRefresh -> loadBookmarks()
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 북마크 제거
     */
    private fun removeBookmark(patternId: String) {
        viewModelScope.launch {
            try {
                toggleBookmarkUseCase(patternId)
            } catch (e: Exception) {
                // 에러 처리 - Flow가 자동으로 UI 업데이트
            }
        }
    }

    /**
     * Domain 모델을 UI 상태로 변환
     */
    private fun mapToUiState(
        patterns: List<DesignPattern>,
        progressList: List<LearningProgress>
    ): BookmarksUiState {
        val progressMap = progressList.associateBy { it.patternId }

        val bookmarkedPatterns = patterns.map { pattern ->
            val progress = progressMap[pattern.id]
            BookmarkedPatternUiModel(
                id = pattern.id,
                name = pattern.name,
                koreanName = pattern.koreanName,
                categoryName = getCategoryDisplayName(pattern.category.name),
                purpose = pattern.purpose,
                difficulty = pattern.difficulty.ordinal + 1,
                isCompleted = progress?.isCompleted ?: false
            )
        }.sortedBy { it.name }

        return BookmarksUiState.Success(bookmarkedPatterns = bookmarkedPatterns)
    }

    /**
     * 카테고리 표시명 변환
     */
    private fun getCategoryDisplayName(category: String): String {
        return when (category) {
            "CREATIONAL" -> "생성 패턴"
            "STRUCTURAL" -> "구조 패턴"
            "BEHAVIORAL" -> "행위 패턴"
            else -> category
        }
    }
}
