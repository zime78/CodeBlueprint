package com.codeblueprint.presentation.pattern.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.usecase.GetLearningProgressUseCase
import com.codeblueprint.domain.usecase.GetPatternsUseCase
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 패턴 목록 화면 ViewModel
 */
@HiltViewModel
class PatternListViewModel @Inject constructor(
    private val getPatternsUseCase: GetPatternsUseCase,
    private val getLearningProgressUseCase: GetLearningProgressUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PatternListUiState>(PatternListUiState.Loading)
    val uiState: StateFlow<PatternListUiState> = _uiState.asStateFlow()

    private val _expandedCategories = MutableStateFlow<Set<PatternCategory>>(
        setOf(PatternCategory.CREATIONAL, PatternCategory.STRUCTURAL, PatternCategory.BEHAVIORAL)
    )
    val expandedCategories: StateFlow<Set<PatternCategory>> = _expandedCategories.asStateFlow()

    init {
        loadPatterns()
    }

    /**
     * 패턴 목록 로드
     */
    private fun loadPatterns() {
        viewModelScope.launch {
            _uiState.value = PatternListUiState.Loading

            try {
                combine(
                    getPatternsUseCase(),
                    getLearningProgressUseCase()
                ) { patterns, progressList ->
                    mapToUiState(patterns, progressList)
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = PatternListUiState.Error(
                    e.message ?: "패턴을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: PatternListEvent) {
        when (event) {
            is PatternListEvent.OnBookmarkToggle -> toggleBookmark(event.patternId)
            is PatternListEvent.OnCategoryToggle -> toggleCategory(event.category)
            is PatternListEvent.OnRefresh -> loadPatterns()
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
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
                // 에러 처리 - 현재는 무시
            }
        }
    }

    /**
     * 카테고리 확장/축소 토글
     */
    private fun toggleCategory(category: PatternCategory) {
        _expandedCategories.value = if (_expandedCategories.value.contains(category)) {
            _expandedCategories.value - category
        } else {
            _expandedCategories.value + category
        }
    }

    /**
     * Domain 모델을 UI 상태로 변환
     */
    private fun mapToUiState(
        patterns: List<DesignPattern>,
        progressList: List<LearningProgress>
    ): PatternListUiState {
        if (patterns.isEmpty()) {
            return PatternListUiState.Success(
                patternsByCategory = emptyMap(),
                learningProgress = 0f,
                completedCount = 0,
                totalCount = 0
            )
        }

        val progressMap = progressList.associateBy { it.patternId }

        val patternsByCategory = patterns
            .groupBy { it.category }
            .mapValues { (_, categoryPatterns) ->
                categoryPatterns.map { pattern ->
                    val progress = progressMap[pattern.id]
                    PatternUiModel(
                        id = pattern.id,
                        name = pattern.name,
                        koreanName = pattern.koreanName,
                        category = pattern.category,
                        purpose = pattern.purpose,
                        difficulty = pattern.difficulty.ordinal + 1,
                        frequency = pattern.frequency,
                        isBookmarked = progress?.isBookmarked ?: false,
                        isCompleted = progress?.isCompleted ?: false
                    )
                }.sortedBy { it.name }
            }

        val completedCount = progressList.count { it.isCompleted }
        val totalCount = patterns.size
        val learningProgress = if (totalCount > 0) {
            completedCount.toFloat() / totalCount.toFloat()
        } else {
            0f
        }

        return PatternListUiState.Success(
            patternsByCategory = patternsByCategory,
            learningProgress = learningProgress,
            completedCount = completedCount,
            totalCount = totalCount
        )
    }
}
