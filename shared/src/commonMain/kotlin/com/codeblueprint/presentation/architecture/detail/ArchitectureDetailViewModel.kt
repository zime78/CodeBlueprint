package com.codeblueprint.presentation.architecture.detail

import com.codeblueprint.domain.model.ArchitecturePattern
import com.codeblueprint.domain.usecase.GetArchitectureDetailUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 아키텍처 상세 화면 ViewModel
 */
class ArchitectureDetailViewModel(
    private val architectureId: String,
    private val getArchitectureDetailUseCase: GetArchitectureDetailUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<ArchitectureDetailUiState>(ArchitectureDetailUiState.Loading)
    val uiState: StateFlow<ArchitectureDetailUiState> = _uiState.asStateFlow()

    init {
        loadArchitectureDetail()
    }

    /**
     * 아키텍처 상세 정보 로드
     */
    private fun loadArchitectureDetail() {
        viewModelScope.launch {
            _uiState.value = ArchitectureDetailUiState.Loading

            try {
                val architecture = getArchitectureDetailUseCase(architectureId)
                if (architecture != null) {
                    _uiState.value = architecture.toUiState()
                } else {
                    _uiState.value = ArchitectureDetailUiState.Error(
                        "아키텍처를 찾을 수 없습니다."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ArchitectureDetailUiState.Error(
                    e.message ?: "아키텍처 정보를 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * Domain 모델을 UI 상태로 변환
     */
    private fun ArchitecturePattern.toUiState() = ArchitectureDetailUiState.Success(
        id = id,
        name = name,
        koreanName = koreanName,
        description = description,
        layers = layers,
        diagram = diagram,
        pros = pros,
        cons = cons,
        useCases = useCases,
        comparison = comparison,
        isRecommended = androidRecommendation
    )
}
