package com.codeblueprint.domain.model

/**
 * 디자인 패턴 분류
 *
 * GoF(Gang of Four)의 23가지 디자인 패턴을 3가지 카테고리로 분류합니다.
 */
enum class PatternCategory(
    val displayName: String,
    val description: String,
    val patternCount: Int
) {
    /**
     * 생성 패턴 (Creational Patterns)
     *
     * 객체 생성 메커니즘과 관련된 패턴들입니다.
     * 객체를 직접 생성하는 대신, 상황에 맞게 객체를 생성하는 방법을 제공합니다.
     */
    CREATIONAL(
        displayName = "생성 패턴",
        description = "객체 생성 메커니즘을 다루며, 상황에 적합한 방식으로 객체를 생성합니다.",
        patternCount = 5
    ),

    /**
     * 구조 패턴 (Structural Patterns)
     *
     * 클래스와 객체의 조합을 통해 더 큰 구조를 형성하는 패턴들입니다.
     * 인터페이스를 사용하여 클래스 간의 관계를 정의합니다.
     */
    STRUCTURAL(
        displayName = "구조 패턴",
        description = "클래스와 객체를 조합하여 더 큰 구조를 형성합니다.",
        patternCount = 7
    ),

    /**
     * 행위 패턴 (Behavioral Patterns)
     *
     * 객체 간의 알고리즘과 책임 분배에 관련된 패턴들입니다.
     * 객체 간의 통신 패턴을 정의합니다.
     */
    BEHAVIORAL(
        displayName = "행위 패턴",
        description = "객체 간의 알고리즘과 책임 분배를 다룹니다.",
        patternCount = 11
    )
}
