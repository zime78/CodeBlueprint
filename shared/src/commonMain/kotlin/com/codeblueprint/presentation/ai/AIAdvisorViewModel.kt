package com.codeblueprint.presentation.ai

import com.codeblueprint.domain.usecase.GetAIRecommendationUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * AI Advisor 화면 ViewModel
 */
class AIAdvisorViewModel(
    private val getAIRecommendationUseCase: GetAIRecommendationUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<AIAdvisorUiState>(AIAdvisorUiState.Idle)
    val uiState: StateFlow<AIAdvisorUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /**
     * 이벤트 처리
     */
    fun onEvent(event: AIAdvisorEvent) {
        when (event) {
            is AIAdvisorEvent.OnQueryChange -> {
                _query.value = event.query
            }
            is AIAdvisorEvent.OnSubmit -> {
                submitQuery()
            }
            is AIAdvisorEvent.OnClearQuery -> {
                _query.value = ""
                _uiState.value = AIAdvisorUiState.Idle
            }
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 쿼리 제출 및 추천 요청
     */
    private fun submitQuery() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isBlank()) return

        viewModelScope.launch {
            _uiState.value = AIAdvisorUiState.Loading

            try {
                val response = getAIRecommendationUseCase(currentQuery)
                _uiState.value = AIAdvisorUiState.Success(
                    query = response.query,
                    recommendations = response.recommendations
                )
            } catch (e: Exception) {
                _uiState.value = AIAdvisorUiState.Error(
                    e.message ?: "추천을 가져오는 중 오류가 발생했습니다."
                )
            }
        }
    }
}
