package com.codeblueprint.presentation.pattern.detail

import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.model.ProgrammingLanguage

/**
 * 패턴 상세 화면 UI 상태
 */
sealed class PatternDetailUiState {
    /**
     * 로딩 상태
     */
    object Loading : PatternDetailUiState()

    /**
     * 성공 상태
     */
    data class Success(
        val pattern: PatternDetailUiModel
    ) : PatternDetailUiState()

    /**
     * 에러 상태
     */
    data class Error(val message: String) : PatternDetailUiState()
}

/**
 * 패턴 상세 UI 모델
 */
data class PatternDetailUiModel(
    val id: String,
    val name: String,
    val koreanName: String,
    val category: PatternCategory,
    val purpose: String,
    val characteristics: List<String>,
    val advantages: List<String>,
    val disadvantages: List<String>,
    val useCases: List<String>,
    val codeExamples: List<CodeExample>,
    val diagram: String,
    val relatedPatterns: List<RelatedPatternUiModel>,
    val difficulty: Difficulty,
    val frequency: Int,
    val isBookmarked: Boolean
)

/**
 * 관련 패턴 UI 모델
 */
data class RelatedPatternUiModel(
    val id: String,
    val name: String,
    val koreanName: String
)

/**
 * 패턴 상세 화면 이벤트
 */
sealed class PatternDetailEvent {
    /**
     * 북마크 토글
     */
    object OnBookmarkToggle : PatternDetailEvent()

    /**
     * 코드 언어 변경
     */
    data class OnLanguageChange(val language: ProgrammingLanguage) : PatternDetailEvent()

    /**
     * 관련 패턴 클릭
     */
    data class OnRelatedPatternClick(val patternId: String) : PatternDetailEvent()

    /**
     * 코드 실행 클릭
     */
    object OnRunCodeClick : PatternDetailEvent()

    /**
     * 공유 클릭
     */
    object OnShareClick : PatternDetailEvent()
}
