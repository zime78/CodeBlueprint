package com.codeblueprint.domain.model

/**
 * 시간 복잡도 모델
 *
 * 알고리즘의 최선, 평균, 최악 시간 복잡도를 표현
 */
data class TimeComplexity(
    val best: String,    // 최선 시간 복잡도 (예: "O(n)")
    val average: String, // 평균 시간 복잡도 (예: "O(n log n)")
    val worst: String    // 최악 시간 복잡도 (예: "O(n²)")
) {
    companion object {
        /**
         * 동일한 복잡도를 가진 경우 간편하게 생성
         */
        fun uniform(complexity: String): TimeComplexity {
            return TimeComplexity(
                best = complexity,
                average = complexity,
                worst = complexity
            )
        }

        /**
         * 일반적인 정렬 알고리즘용 (n log n)
         */
        val NLOGN = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        )

        /**
         * 선형 시간 복잡도
         */
        val LINEAR = uniform("O(n)")

        /**
         * 로그 시간 복잡도
         */
        val LOGARITHMIC = uniform("O(log n)")

        /**
         * 상수 시간 복잡도
         */
        val CONSTANT = uniform("O(1)")
    }

    /**
     * 표시용 문자열 (평균 복잡도 기준)
     */
    fun displayString(): String = average

    /**
     * 상세 표시 문자열
     */
    fun detailedString(): String = "최선: $best / 평균: $average / 최악: $worst"
}
