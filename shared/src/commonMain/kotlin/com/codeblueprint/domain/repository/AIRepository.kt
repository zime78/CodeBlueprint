package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.RecommendationResponse

/**
 * AI 추천 Repository 인터페이스
 */
interface AIRepository {

    /**
     * 문제 설명을 기반으로 패턴 추천
     *
     * @param query 사용자의 문제 설명
     * @return 추천 결과
     */
    suspend fun getRecommendation(query: String): RecommendationResponse

    /**
     * 추천 히스토리 조회
     */
    suspend fun getRecommendationHistory(): List<RecommendationResponse>

    /**
     * 히스토리 저장
     */
    suspend fun saveRecommendation(response: RecommendationResponse)

    /**
     * 히스토리 삭제
     */
    suspend fun clearHistory()
}
