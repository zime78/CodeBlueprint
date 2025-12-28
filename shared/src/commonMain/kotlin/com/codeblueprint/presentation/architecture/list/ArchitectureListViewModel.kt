package com.codeblueprint.presentation.architecture.list

import com.codeblueprint.domain.model.ArchitecturePattern
import com.codeblueprint.domain.usecase.GetArchitecturesUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 아키텍처 목록 화면 ViewModel
 */
class ArchitectureListViewModel(
    private val getArchitecturesUseCase: GetArchitecturesUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<ArchitectureListUiState>(ArchitectureListUiState.Loading)
    val uiState: StateFlow<ArchitectureListUiState> = _uiState.asStateFlow()

    init {
        loadArchitectures()
    }

    /**
     * 아키텍처 목록 로드
     */
    private fun loadArchitectures() {
        viewModelScope.launch {
            _uiState.value = ArchitectureListUiState.Loading

            try {
                getArchitecturesUseCase().collect { architectures ->
                    _uiState.value = ArchitectureListUiState.Success(
                        architectures = architectures.map { it.toUiModel() }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ArchitectureListUiState.Error(
                    e.message ?: "아키텍처 목록을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: ArchitectureListEvent) {
        when (event) {
            is ArchitectureListEvent.OnRefresh -> loadArchitectures()
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * Domain 모델을 UI 모델로 변환
     */
    private fun ArchitecturePattern.toUiModel() = ArchitectureUiModel(
        id = id,
        name = name,
        koreanName = koreanName,
        description = description,
        comparison = comparison,
        isRecommended = androidRecommendation
    )
}
