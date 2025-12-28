package com.codeblueprint.presentation.playground

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage

/**
 * 코드 플레이그라운드 UI 상태
 */
sealed class CodePlaygroundUiState {
    /**
     * 초기 상태 (실행 대기)
     */
    data class Idle(
        val output: String = ""
    ) : CodePlaygroundUiState()

    /**
     * 실행 중
     */
    data object Executing : CodePlaygroundUiState()

    /**
     * 실행 성공
     */
    data class Success(
        val result: CodeExecutionResult
    ) : CodePlaygroundUiState()

    /**
     * 실행 에러
     */
    data class Error(
        val message: String
    ) : CodePlaygroundUiState()
}

/**
 * 코드 플레이그라운드 이벤트
 */
sealed class CodePlaygroundEvent {
    /**
     * 코드 변경
     */
    data class OnCodeChange(val code: String) : CodePlaygroundEvent()

    /**
     * 언어 변경
     */
    data class OnLanguageChange(val language: ProgrammingLanguage) : CodePlaygroundEvent()

    /**
     * 코드 실행
     */
    data object OnExecute : CodePlaygroundEvent()

    /**
     * 출력 초기화
     */
    data object OnClearOutput : CodePlaygroundEvent()

    /**
     * 코드 초기화
     */
    data object OnResetCode : CodePlaygroundEvent()

    /**
     * 뒤로 가기
     */
    data object OnBackClick : CodePlaygroundEvent()
}
