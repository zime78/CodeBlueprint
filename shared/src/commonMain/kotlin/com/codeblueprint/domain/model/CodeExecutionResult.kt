package com.codeblueprint.domain.model

/**
 * 코드 실행 결과 모델
 */
data class CodeExecutionResult(
    val success: Boolean,
    val output: String,
    val errorMessage: String? = null,
    val executionTimeMs: Long = 0
)

/**
 * 코드 실행 요청 모델
 */
data class CodeExecutionRequest(
    val code: String,
    val language: ProgrammingLanguage
)

/**
 * 코드 샘플 모델
 */
data class CodeSample(
    val language: ProgrammingLanguage,
    val code: String,
    val description: String
)
