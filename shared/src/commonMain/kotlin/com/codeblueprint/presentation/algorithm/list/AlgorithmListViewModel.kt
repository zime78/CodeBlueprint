package com.codeblueprint.presentation.algorithm.list

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.usecase.GetAlgorithmsUseCase
import com.codeblueprint.domain.usecase.ToggleAlgorithmBookmarkUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 알고리즘 목록 화면 ViewModel
 */
class AlgorithmListViewModel(
    private val getAlgorithmsUseCase: GetAlgorithmsUseCase,
    private val toggleBookmarkUseCase: ToggleAlgorithmBookmarkUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<AlgorithmListUiState>(AlgorithmListUiState.Loading)
    val uiState: StateFlow<AlgorithmListUiState> = _uiState.asStateFlow()

    private val _expandedCategories = MutableStateFlow<Set<AlgorithmCategory>>(
        AlgorithmCategory.entries.toSet()
    )
    val expandedCategories: StateFlow<Set<AlgorithmCategory>> = _expandedCategories.asStateFlow()

    init {
        loadAlgorithms()
    }

    /**
     * 알고리즘 목록 로드
     */
    private fun loadAlgorithms() {
        viewModelScope.launch {
            _uiState.value = AlgorithmListUiState.Loading

            try {
                getAlgorithmsUseCase().collect { algorithms ->
                    _uiState.value = mapToUiState(algorithms)
                }
            } catch (e: Exception) {
                _uiState.value = AlgorithmListUiState.Error(
                    e.message ?: "알고리즘을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: AlgorithmListEvent) {
        when (event) {
            is AlgorithmListEvent.OnBookmarkToggle -> toggleBookmark(event.algorithmId)
            is AlgorithmListEvent.OnCategoryToggle -> toggleCategory(event.category)
            is AlgorithmListEvent.OnRefresh -> loadAlgorithms()
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 북마크 토글
     */
    private fun toggleBookmark(algorithmId: String) {
        viewModelScope.launch {
            try {
                toggleBookmarkUseCase(algorithmId)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 카테고리 확장/축소 토글
     */
    private fun toggleCategory(category: AlgorithmCategory) {
        _expandedCategories.value = if (_expandedCategories.value.contains(category)) {
            _expandedCategories.value - category
        } else {
            _expandedCategories.value + category
        }
    }

    /**
     * Domain 모델을 UI 상태로 변환
     */
    private fun mapToUiState(algorithms: List<Algorithm>): AlgorithmListUiState {
        if (algorithms.isEmpty()) {
            return AlgorithmListUiState.Success(
                algorithmsByCategory = emptyMap()
            )
        }

        val algorithmsByCategory = algorithms
            .groupBy { it.category }
            .mapValues { (_, categoryAlgorithms) ->
                categoryAlgorithms.map { algorithm ->
                    AlgorithmUiModel(
                        id = algorithm.id,
                        name = algorithm.name,
                        koreanName = algorithm.koreanName,
                        category = algorithm.category,
                        purpose = algorithm.purpose,
                        timeComplexity = algorithm.timeComplexity.average,
                        spaceComplexity = algorithm.spaceComplexity,
                        difficulty = algorithm.difficulty.ordinal + 1,
                        frequency = algorithm.frequency,
                        isBookmarked = algorithm.isBookmarked
                    )
                }.sortedBy { it.name }
            }

        return AlgorithmListUiState.Success(
            algorithmsByCategory = algorithmsByCategory
        )
    }
}
