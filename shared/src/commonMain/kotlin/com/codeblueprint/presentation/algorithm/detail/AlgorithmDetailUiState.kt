package com.codeblueprint.presentation.algorithm.detail

import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 알고리즘 상세 화면 UI 상태
 */
sealed class AlgorithmDetailUiState {
    /**
     * 로딩 상태
     */
    object Loading : AlgorithmDetailUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val algorithm: AlgorithmDetailUiModel
    ) : AlgorithmDetailUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : AlgorithmDetailUiState()
}

/**
 * 알고리즘 상세 UI 모델
 */
data class AlgorithmDetailUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: AlgorithmCategory,
    val purpose: String,
    val timeComplexity: TimeComplexity,
    val spaceComplexity: String,
    val characteristics: List<String>,
    val advantages: List<String>,
    val disadvantages: List<String>,
    val useCases: List<String>,
    val codeExamples: List<CodeExample>,
    val relatedAlgorithms: List<RelatedAlgorithmUiModel>,
    val difficulty: Difficulty,
    val frequency: Int,
    val isBookmarked: Boolean
)

/**
 * 관련 알고리즘 UI 모델
 */
data class RelatedAlgorithmUiModel(
    val id: String,
    val name: String,
    val koreanName: String
)

/**
 * 알고리즘 상세 화면 이벤트
 */
sealed class AlgorithmDetailEvent {
    /**
     * 북마크 토글
     */
    object OnBookmarkToggle : AlgorithmDetailEvent()

    /**
     * 코드 언어 변경
     */
    data class OnLanguageChange(val language: ProgrammingLanguage) : AlgorithmDetailEvent()

    /**
     * 관련 알고리즘 클릭
     */
    data class OnRelatedAlgorithmClick(val algorithmId: String) : AlgorithmDetailEvent()

    /**
     * 코드 실행 클릭
     */
    object OnRunCodeClick : AlgorithmDetailEvent()

    /**
     * 공유 클릭
     */
    object OnShareClick : AlgorithmDetailEvent()
}
