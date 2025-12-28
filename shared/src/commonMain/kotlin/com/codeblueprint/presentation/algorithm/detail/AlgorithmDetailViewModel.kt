package com.codeblueprint.presentation.algorithm.detail

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.usecase.GetAlgorithmDetailUseCase
import com.codeblueprint.domain.usecase.GetAlgorithmLearningProgressUseCase
import com.codeblueprint.domain.usecase.GetAlgorithmsUseCase
import com.codeblueprint.domain.usecase.ToggleAlgorithmBookmarkUseCase
import com.codeblueprint.domain.usecase.ToggleAlgorithmCompleteUseCase
import com.codeblueprint.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 알고리즘 상세 화면 ViewModel
 */
class AlgorithmDetailViewModel(
    private val algorithmId: String,
    private val getAlgorithmDetailUseCase: GetAlgorithmDetailUseCase,
    private val getAlgorithmsUseCase: GetAlgorithmsUseCase,
    private val getLearningProgressUseCase: GetAlgorithmLearningProgressUseCase,
    private val toggleBookmarkUseCase: ToggleAlgorithmBookmarkUseCase,
    private val toggleCompleteUseCase: ToggleAlgorithmCompleteUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<AlgorithmDetailUiState>(AlgorithmDetailUiState.Loading)
    val uiState: StateFlow<AlgorithmDetailUiState> = _uiState.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(ProgrammingLanguage.KOTLIN)
    val selectedLanguage: StateFlow<ProgrammingLanguage> = _selectedLanguage.asStateFlow()

    init {
        loadAlgorithmDetail()
    }

    /**
     * 알고리즘 상세 정보 로드
     */
    private fun loadAlgorithmDetail() {
        viewModelScope.launch {
            _uiState.value = AlgorithmDetailUiState.Loading

            try {
                val algorithm = getAlgorithmDetailUseCase(algorithmId)

                if (algorithm != null) {
                    // 학습 진도와 관련 알고리즘을 함께 로드
                    getLearningProgressUseCase(algorithmId).collect { progress ->
                        val allAlgorithms = getAlgorithmsUseCase().first()
                        val relatedAlgorithms = getRelatedAlgorithms(algorithm, allAlgorithms)

                        _uiState.value = AlgorithmDetailUiState.Success(
                            algorithm = mapToUiModel(algorithm, progress, relatedAlgorithms)
                        )
                    }
                } else {
                    _uiState.value = AlgorithmDetailUiState.Error("알고리즘을 찾을 수 없습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = AlgorithmDetailUiState.Error(
                    e.message ?: "알고리즘을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: AlgorithmDetailEvent) {
        when (event) {
            is AlgorithmDetailEvent.OnBookmarkToggle -> toggleBookmark()
            is AlgorithmDetailEvent.OnCompleteToggle -> toggleComplete()
            is AlgorithmDetailEvent.OnLanguageChange -> changeLanguage(event.language)
            else -> { /* Navigation 이벤트는 Screen에서 처리 */ }
        }
    }

    /**
     * 북마크 토글
     */
    private fun toggleBookmark() {
        viewModelScope.launch {
            try {
                toggleBookmarkUseCase(algorithmId)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    /**
     * 학습 완료 토글
     */
    private fun toggleComplete() {
        viewModelScope.launch {
            try {
                toggleCompleteUseCase(algorithmId)
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
     * 관련 알고리즘 조회
     */
    private fun getRelatedAlgorithms(
        algorithm: Algorithm,
        allAlgorithms: List<Algorithm>
    ): List<RelatedAlgorithmUiModel> {
        val algorithmMap = allAlgorithms.associateBy { it.id }
        return algorithm.relatedAlgorithmIds.mapNotNull { id ->
            algorithmMap[id]?.let { relatedAlgorithm ->
                RelatedAlgorithmUiModel(
                    id = relatedAlgorithm.id,
                    name = relatedAlgorithm.name,
                    koreanName = relatedAlgorithm.koreanName
                )
            }
        }
    }

    /**
     * Domain 모델을 UI 모델로 변환
     */
    private fun mapToUiModel(
        algorithm: Algorithm,
        progress: LearningProgress?,
        relatedAlgorithms: List<RelatedAlgorithmUiModel>
    ): AlgorithmDetailUiModel {
        return AlgorithmDetailUiModel(
            id = algorithm.id,
            name = algorithm.name,
            koreanName = algorithm.koreanName,
            category = algorithm.category,
            purpose = algorithm.purpose,
            timeComplexity = algorithm.timeComplexity,
            spaceComplexity = algorithm.spaceComplexity,
            characteristics = algorithm.characteristics,
            advantages = algorithm.advantages,
            disadvantages = algorithm.disadvantages,
            useCases = algorithm.useCases,
            codeExamples = algorithm.codeExamples,
            relatedAlgorithms = relatedAlgorithms,
            difficulty = algorithm.difficulty,
            frequency = algorithm.frequency,
            isBookmarked = progress?.isBookmarked ?: false,
            isCompleted = progress?.isCompleted ?: false
        )
    }
}
