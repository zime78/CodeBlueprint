package com.codeblueprint.domain.model

/**
 * 알고리즘 카테고리
 *
 * 9개 카테고리로 73개 알고리즘 분류
 */
enum class AlgorithmCategory(
    val displayName: String,
    val koreanName: String,
    val description: String
) {
    SORTING(
        displayName = "Sorting",
        koreanName = "정렬",
        description = "데이터를 특정 순서로 정렬하는 알고리즘"
    ),
    SEARCHING(
        displayName = "Searching",
        koreanName = "탐색",
        description = "데이터에서 특정 값을 찾는 알고리즘"
    ),
    GRAPH(
        displayName = "Graph",
        koreanName = "그래프",
        description = "그래프 자료구조를 다루는 알고리즘"
    ),
    DYNAMIC_PROGRAMMING(
        displayName = "Dynamic Programming",
        koreanName = "동적 프로그래밍",
        description = "부분 문제의 해를 저장하여 전체 문제를 해결하는 알고리즘"
    ),
    DIVIDE_CONQUER(
        displayName = "Divide & Conquer",
        koreanName = "분할 정복",
        description = "문제를 작은 부분으로 분할하여 해결하는 알고리즘"
    ),
    GREEDY(
        displayName = "Greedy",
        koreanName = "탐욕",
        description = "각 단계에서 최적의 선택을 하는 알고리즘"
    ),
    BACKTRACKING(
        displayName = "Backtracking",
        koreanName = "백트래킹",
        description = "해를 찾다가 막히면 되돌아가는 알고리즘"
    ),
    STRING(
        displayName = "String",
        koreanName = "문자열",
        description = "문자열 처리 및 패턴 매칭 알고리즘"
    ),
    MATH(
        displayName = "Math",
        koreanName = "수학",
        description = "수학적 계산 및 수론 알고리즘"
    );

    companion object {
        /**
         * 문자열로부터 카테고리 변환
         */
        fun fromString(value: String): AlgorithmCategory {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
            } ?: SORTING
        }
    }
}
