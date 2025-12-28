package com.codeblueprint.domain.model

/**
 * 패턴 학습 난이도
 */
enum class Difficulty(
    val displayName: String,
    val level: Int
) {
    LOW(displayName = "쉬움", level = 1),
    MEDIUM(displayName = "보통", level = 2),
    HIGH(displayName = "어려움", level = 3)
}
