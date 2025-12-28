package com.codeblueprint.presentation.pattern.detail

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.usecase.GetPatternDetailUseCase
import com.codeblueprint.domain.usecase.GetPatternsUseCase
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 패턴 상세 화면 ViewModel
 */
class PatternDetailViewModel(
    private val patternId: String,
    private val getPatternDetailUseCase: GetPatternDetailUseCase,
    private val getPatternsUseCase: GetPatternsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<PatternDetailUiState>(PatternDetailUiState.Loading)
    val uiState: StateFlow<PatternDetailUiState> = _uiState.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(ProgrammingLanguage.KOTLIN)
    val selectedLanguage: StateFlow<ProgrammingLanguage> = _selectedLanguage.asStateFlow()

    init {
        loadPatternDetail()
    }

    /**
     * 패턴 상세 정보 로드
     */
    private fun loadPatternDetail() {
        viewModelScope.launch {
            _uiState.value = PatternDetailUiState.Loading

            try {
                val pattern = getPatternDetailUseCase(patternId)

                if (pattern != null) {
                    val allPatterns = getPatternsUseCase().first()
                    val relatedPatterns = getRelatedPatterns(pattern, allPatterns)

                    _uiState.value = PatternDetailUiState.Success(
                        pattern = mapToUiModel(pattern, relatedPatterns)
                    )
                } else {
                    _uiState.value = PatternDetailUiState.Error("패턴을 찾을 수 없습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = PatternDetailUiState.Error(
                    e.message ?: "패턴을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: PatternDetailEvent) {
        when (event) {
            is PatternDetailEvent.OnBookmarkToggle -> toggleBookmark()
            is PatternDetailEvent.OnLanguageChange -> changeLanguage(event.language)
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 북마크 토글
     */
    private fun toggleBookmark() {
        viewModelScope.launch {
            try {
                toggleBookmarkUseCase(patternId)
                // 상태 다시 로드
                loadPatternDetail()
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 코드 언어 변경
     */
    private fun changeLanguage(language: ProgrammingLanguage) {
        _selectedLanguage.value = language
    }

    /**
     * 관련 패턴 조회
     */
    private fun getRelatedPatterns(
        pattern: DesignPattern,
        allPatterns: List<DesignPattern>
    ): List<RelatedPatternUiModel> {
        val patternMap = allPatterns.associateBy { it.id }
        return pattern.relatedPatternIds.mapNotNull { id ->
            patternMap[id]?.let { relatedPattern ->
                RelatedPatternUiModel(
                    id = relatedPattern.id,
                    name = relatedPattern.name,
                    koreanName = relatedPattern.koreanName
                )
            }
        }
    }

    /**
     * Domain 모델을 UI 모델로 변환
     */
    private fun mapToUiModel(
        pattern: DesignPattern,
        relatedPatterns: List<RelatedPatternUiModel>
    ): PatternDetailUiModel {
        return PatternDetailUiModel(
            id = pattern.id,
            name = pattern.name,
            koreanName = pattern.koreanName,
            category = pattern.category,
            purpose = pattern.purpose,
            characteristics = pattern.characteristics,
            advantages = pattern.advantages,
            disadvantages = pattern.disadvantages,
            useCases = pattern.useCases,
            codeExamples = pattern.codeExamples,
            diagram = pattern.diagram,
            relatedPatterns = relatedPatterns,
            difficulty = pattern.difficulty,
            frequency = pattern.frequency,
            isBookmarked = pattern.isBookmarked
        )
    }
}
