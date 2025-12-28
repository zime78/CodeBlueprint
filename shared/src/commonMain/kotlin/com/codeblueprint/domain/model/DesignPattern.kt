package com.codeblueprint.domain.model

/**
 * 디자인 패턴 도메인 모델
 *
 * GoF(Gang of Four) 디자인 패턴의 정보를 담는 핵심 엔티티입니다.
 *
 * @property id 고유 식별자
 * @property name 패턴 영문명
 * @property koreanName 패턴 한글명
 * @property category 패턴 분류 (생성/구조/행위)
 * @property purpose 패턴의 목적
 * @property characteristics 패턴의 특징 목록
 * @property advantages 장점 목록
 * @property disadvantages 단점 목록
 * @property useCases 활용 예시 목록
 * @property codeExamples 언어별 코드 예시
 * @property diagram Mermaid 형식의 클래스 다이어그램
 * @property relatedPatternIds 관련 패턴 ID 목록
 * @property difficulty 학습 난이도
 * @property frequency 사용 빈도 (1-5)
 */
data class DesignPattern(
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
    val relatedPatternIds: List<String>,
    val difficulty: Difficulty,
    val frequency: Int
) {
    /**
     * 사용 빈도를 별점 형태로 반환
     */
    val frequencyStars: String
        get() = "★".repeat(frequency) + "☆".repeat(5 - frequency)

    /**
     * 기본 코드 예시 (Kotlin 우선)
     */
    val defaultCodeExample: CodeExample?
        get() = codeExamples.find { it.language == ProgrammingLanguage.KOTLIN }
            ?: codeExamples.firstOrNull()
}
