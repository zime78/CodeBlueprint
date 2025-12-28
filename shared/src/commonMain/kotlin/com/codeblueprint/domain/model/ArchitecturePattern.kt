package com.codeblueprint.domain.model

/**
 * 아키텍처 패턴 도메인 모델
 */
data class ArchitecturePattern(
    val id: String,
    val name: String,
    val koreanName: String,
    val description: String,
    val layers: List<ArchitectureLayer>,
    val diagram: String,
    val pros: List<String>,
    val cons: List<String>,
    val useCases: List<String>,
    val comparison: ArchitectureComparison,
    val androidRecommendation: Boolean = false
)

/**
 * 아키텍처 레이어
 */
data class ArchitectureLayer(
    val name: String,
    val koreanName: String,
    val description: String,
    val responsibilities: List<String>
)

/**
 * 아키텍처 비교 지표
 */
data class ArchitectureComparison(
    val complexity: Int,           // 1-5 복잡도
    val testability: Int,          // 1-5 테스트 용이성
    val scalability: Int,          // 1-5 확장성
    val maintainability: Int,      // 1-5 유지보수성
    val learningCurve: Int         // 1-5 학습 곡선 (1: 쉬움, 5: 어려움)
)
