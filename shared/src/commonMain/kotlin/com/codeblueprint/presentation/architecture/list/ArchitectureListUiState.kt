package com.codeblueprint.presentation.architecture.list

import com.codeblueprint.domain.model.ArchitectureComparison

/**
 * 아키텍처 목록 화면 UI 상태
 */
sealed class ArchitectureListUiState {

    /**
     * 로딩 중
     */
    data object Loading : ArchitectureListUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val architectures: List<ArchitectureUiModel>
    ) : ArchitectureListUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : ArchitectureListUiState()
}

/**
 * 아키텍처 UI 모델
 */
data class ArchitectureUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val description: String,
    val comparison: ArchitectureComparison,
    val isRecommended: Boolean
)

/**
 * 아키텍처 목록 화면 이벤트
 */
sealed class ArchitectureListEvent {
    data class OnArchitectureClick(val id: String) : ArchitectureListEvent()
    data object OnRefresh : ArchitectureListEvent()
}
