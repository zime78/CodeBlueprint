package com.codeblueprint.presentation.ai

import com.codeblueprint.domain.model.PatternRecommendation

/**
 * AI Advisor 화면 UI 상태
 */
sealed class AIAdvisorUiState {

    /**
     * 초기 상태 (입력 대기)
     */
    data object Idle : AIAdvisorUiState()

    /**
     * 추천 로딩 중
     */
    data object Loading : AIAdvisorUiState()

    /**
     * 추천 결과 성공
     */
    data class Success(
        val query: String,
        val recommendations: List<PatternRecommendation>
    ) : AIAdvisorUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : AIAdvisorUiState()
}

/**
 * AI Advisor 화면 이벤트
 */
sealed class AIAdvisorEvent {
    data class OnQueryChange(val query: String) : AIAdvisorEvent()
    data object OnSubmit : AIAdvisorEvent()
    data class OnPatternClick(val patternId: String) : AIAdvisorEvent()
    data object OnClearQuery : AIAdvisorEvent()
}
