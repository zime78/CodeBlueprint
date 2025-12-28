package com.codeblueprint.data.repository

import com.codeblueprint.domain.model.PatternRecommendation
import com.codeblueprint.domain.model.RecommendationResponse
import com.codeblueprint.domain.repository.AIRepository
import kotlinx.coroutines.delay

/**
 * AI Repository Mock 구현
 *
 * TODO: 실제 AI API 연동 시 이 클래스를 수정하거나 새로운 구현체 생성
 * - Claude API: https://docs.anthropic.com/
 * - OpenAI API: https://platform.openai.com/docs/
 */
class AIRepositoryImpl : AIRepository {

    private val history = mutableListOf<RecommendationResponse>()

    /**
     * Mock 추천 데이터
     * 키워드 기반으로 관련 패턴 추천
     */
    private val mockRecommendations = mapOf(
        // 생성 패턴 키워드
        listOf("싱글톤", "singleton", "하나", "인스턴스", "전역", "global") to listOf(
            PatternRecommendation(
                patternId = "singleton",
                patternName = "Singleton",
                koreanName = "싱글톤",
                matchRate = 0.95f,
                reasoning = "전역적으로 하나의 인스턴스만 필요할 때 적합합니다. 데이터베이스 연결, 설정 관리 등에 활용됩니다."
            )
        ),
        listOf("팩토리", "factory", "생성", "객체 생성", "서브클래스") to listOf(
            PatternRecommendation(
                patternId = "factory_method",
                patternName = "Factory Method",
                koreanName = "팩토리 메서드",
                matchRate = 0.90f,
                reasoning = "객체 생성 로직을 서브클래스에 위임하고 싶을 때 적합합니다."
            ),
            PatternRecommendation(
                patternId = "abstract_factory",
                patternName = "Abstract Factory",
                koreanName = "추상 팩토리",
                matchRate = 0.75f,
                reasoning = "관련된 객체 군을 생성해야 할 때 고려해볼 수 있습니다."
            )
        ),
        listOf("빌더", "builder", "복잡한 객체", "단계별", "옵션") to listOf(
            PatternRecommendation(
                patternId = "builder",
                patternName = "Builder",
                koreanName = "빌더",
                matchRate = 0.92f,
                reasoning = "복잡한 객체를 단계별로 생성하거나 다양한 옵션이 있을 때 적합합니다."
            )
        ),

        // 구조 패턴 키워드
        listOf("어댑터", "adapter", "인터페이스 변환", "호환", "래퍼") to listOf(
            PatternRecommendation(
                patternId = "adapter",
                patternName = "Adapter",
                koreanName = "어댑터",
                matchRate = 0.93f,
                reasoning = "호환되지 않는 인터페이스를 함께 사용해야 할 때 적합합니다."
            )
        ),
        listOf("데코레이터", "decorator", "기능 추가", "확장", "래핑") to listOf(
            PatternRecommendation(
                patternId = "decorator",
                patternName = "Decorator",
                koreanName = "데코레이터",
                matchRate = 0.88f,
                reasoning = "객체에 동적으로 기능을 추가하고 싶을 때 적합합니다."
            )
        ),
        listOf("파사드", "facade", "단순화", "인터페이스", "복잡한 시스템") to listOf(
            PatternRecommendation(
                patternId = "facade",
                patternName = "Facade",
                koreanName = "파사드",
                matchRate = 0.90f,
                reasoning = "복잡한 서브시스템에 대한 단순한 인터페이스를 제공하고 싶을 때 적합합니다."
            )
        ),

        // 행위 패턴 키워드
        listOf("옵저버", "observer", "이벤트", "구독", "알림", "변경 감지") to listOf(
            PatternRecommendation(
                patternId = "observer",
                patternName = "Observer",
                koreanName = "옵저버",
                matchRate = 0.94f,
                reasoning = "객체의 상태 변화를 다른 객체들에게 알려야 할 때 적합합니다. 이벤트 시스템에 활용됩니다."
            )
        ),
        listOf("전략", "strategy", "알고리즘", "교체", "런타임") to listOf(
            PatternRecommendation(
                patternId = "strategy",
                patternName = "Strategy",
                koreanName = "전략",
                matchRate = 0.91f,
                reasoning = "알고리즘을 런타임에 교체해야 하거나 다양한 변형이 필요할 때 적합합니다."
            )
        ),
        listOf("커맨드", "command", "명령", "실행 취소", "undo", "redo") to listOf(
            PatternRecommendation(
                patternId = "command",
                patternName = "Command",
                koreanName = "커맨드",
                matchRate = 0.89f,
                reasoning = "요청을 객체로 캡슐화하거나 실행 취소 기능이 필요할 때 적합합니다."
            )
        ),
        listOf("상태", "state", "상태 머신", "상태 전이") to listOf(
            PatternRecommendation(
                patternId = "state",
                patternName = "State",
                koreanName = "상태",
                matchRate = 0.87f,
                reasoning = "객체의 상태에 따라 행동이 변경되어야 할 때 적합합니다."
            )
        )
    )

    override suspend fun getRecommendation(query: String): RecommendationResponse {
        // API 호출 시뮬레이션
        delay(1000)

        val queryLower = query.lowercase()
        val recommendations = mutableListOf<PatternRecommendation>()

        // 키워드 매칭으로 추천
        for ((keywords, patterns) in mockRecommendations) {
            if (keywords.any { queryLower.contains(it) }) {
                recommendations.addAll(patterns)
            }
        }

        // 매칭되는 게 없으면 기본 추천
        if (recommendations.isEmpty()) {
            recommendations.addAll(getDefaultRecommendations())
        }

        // 중복 제거 및 매칭률 순 정렬
        val uniqueRecommendations = recommendations
            .distinctBy { it.patternId }
            .sortedByDescending { it.matchRate }
            .take(5)

        val response = RecommendationResponse(
            query = query,
            recommendations = uniqueRecommendations
        )

        // 히스토리에 저장
        saveRecommendation(response)

        return response
    }

    private fun getDefaultRecommendations(): List<PatternRecommendation> {
        return listOf(
            PatternRecommendation(
                patternId = "observer",
                patternName = "Observer",
                koreanName = "옵저버",
                matchRate = 0.60f,
                reasoning = "이벤트 기반 시스템에서 자주 사용되는 패턴입니다."
            ),
            PatternRecommendation(
                patternId = "strategy",
                patternName = "Strategy",
                koreanName = "전략",
                matchRate = 0.55f,
                reasoning = "다양한 알고리즘을 교체할 수 있는 유연한 패턴입니다."
            ),
            PatternRecommendation(
                patternId = "factory_method",
                patternName = "Factory Method",
                koreanName = "팩토리 메서드",
                matchRate = 0.50f,
                reasoning = "객체 생성 로직을 분리하는 기본적인 생성 패턴입니다."
            )
        )
    }

    override suspend fun getRecommendationHistory(): List<RecommendationResponse> {
        return history.toList().reversed()
    }

    override suspend fun saveRecommendation(response: RecommendationResponse) {
        history.add(response)
        // 최대 20개까지만 저장
        if (history.size > 20) {
            history.removeAt(0)
        }
    }

    override suspend fun clearHistory() {
        history.clear()
    }
}
