package com.codeblueprint.domain.model

/**
 * AI 패턴 추천 결과
 */
data class PatternRecommendation(
    val patternId: String,
    val patternName: String,
    val koreanName: String,
    val matchRate: Float,      // 0.0 ~ 1.0
    val reasoning: String
)

/**
 * AI 추천 요청
 */
data class RecommendationRequest(
    val query: String
)

/**
 * AI 추천 응답
 */
data class RecommendationResponse(
    val query: String,
    val recommendations: List<PatternRecommendation>,
    val timestamp: Long = System.currentTimeMillis()
)
