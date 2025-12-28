package com.codeblueprint.presentation.architecture.detail

import com.codeblueprint.domain.model.ArchitectureComparison
import com.codeblueprint.domain.model.ArchitectureLayer

/**
 * 아키텍처 상세 화면 UI 상태
 */
sealed class ArchitectureDetailUiState {

    /**
     * 로딩 중
     */
    data object Loading : ArchitectureDetailUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val id: String,
        val name: String,
        val koreanName: String,
        val description: String,
        val layers: List<ArchitectureLayer>,
        val diagram: String,
        val pros: List<String>,
        val cons: List<String>,
        val useCases: List<String>,
        val comparison: ArchitectureComparison,
        val isRecommended: Boolean
    ) : ArchitectureDetailUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : ArchitectureDetailUiState()
}

/**
 * 아키텍처 상세 화면 이벤트
 */
sealed class ArchitectureDetailEvent {
    data object OnBackClick : ArchitectureDetailEvent()
}
