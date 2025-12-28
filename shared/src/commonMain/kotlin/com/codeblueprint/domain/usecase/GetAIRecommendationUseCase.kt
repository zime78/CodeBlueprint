package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.RecommendationResponse
import com.codeblueprint.domain.repository.AIRepository

/**
 * AI 패턴 추천 UseCase
 */
class GetAIRecommendationUseCase(
    private val repository: AIRepository
) {
    suspend operator fun invoke(query: String): RecommendationResponse {
        return repository.getRecommendation(query)
    }
}
