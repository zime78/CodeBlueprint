package com.codeblueprint.domain.model

/**
 * 알고리즘 도메인 모델
 *
 * 73개 알고리즘의 핵심 정보를 담는 엔티티
 */
data class Algorithm(
    val id: String,                           // 고유 ID (예: "quick-sort")
    val name: String,                         // 영문명 (예: "Quick Sort")
    val koreanName: String,                   // 한글명 (예: "퀵 정렬")
    val category: AlgorithmCategory,          // 카테고리 (SORTING, GRAPH 등)
    val purpose: String,                      // 알고리즘 목적/설명
    val timeComplexity: TimeComplexity,       // 시간 복잡도 (best/average/worst)
    val spaceComplexity: String,              // 공간 복잡도 (예: "O(log n)")
    val characteristics: List<String>,        // 특징들
    val advantages: List<String>,             // 장점들
    val disadvantages: List<String>,          // 단점들
    val useCases: List<String>,               // 활용 예시
    val codeExamples: List<CodeExample>,      // 언어별 코드 예시
    val relatedAlgorithmIds: List<String>,    // 관련 알고리즘 ID들
    val difficulty: Difficulty,               // 난이도 (LOW/MEDIUM/HIGH)
    val frequency: Int,                       // 사용 빈도 (1-5)
    val isBookmarked: Boolean = false         // 북마크 여부
) {
    /**
     * 난이도를 숫자로 변환 (UI 표시용)
     */
    fun difficultyLevel(): Int = when (difficulty) {
        Difficulty.LOW -> 1
        Difficulty.MEDIUM -> 2
        Difficulty.HIGH -> 3
    }

    /**
     * 카테고리 표시 문자열
     */
    fun categoryDisplayName(): String = category.koreanName

    /**
     * 평균 시간 복잡도 표시
     */
    fun averageTimeComplexity(): String = timeComplexity.average
}
