package com.codeblueprint.domain.model

/**
 * 패턴/알고리즘의 코드 예시
 *
 * @property id 고유 식별자
 * @property language 프로그래밍 언어
 * @property code 소스 코드
 * @property explanation 코드 설명
 * @property sampleInput 샘플 입력 데이터 목록
 * @property expectedOutput 예상 실행 결과 (미리 정의된 출력값)
 * @property displayOrder 표시 순서
 */
data class CodeExample(
    val id: String = "",
    val language: ProgrammingLanguage,
    val code: String,
    val explanation: String,
    val sampleInput: List<SampleInput> = emptyList(),
    val expectedOutput: String = "",
    val displayOrder: Int = 0
)
