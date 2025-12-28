package com.codeblueprint.domain.model

/**
 * 패턴의 코드 예시
 *
 * @property language 프로그래밍 언어
 * @property code 소스 코드
 * @property explanation 코드 설명
 */
data class CodeExample(
    val language: ProgrammingLanguage,
    val code: String,
    val explanation: String
)
