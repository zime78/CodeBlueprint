package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 동적 프로그래밍 알고리즘 데이터 (8개)
 *
 * Fibonacci DP, LCS, LIS, Knapsack, Edit Distance,
 * Matrix Chain, Coin Change, Rod Cutting
 */
internal object DynamicProgrammingAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createFibonacciDP(),
        createLCS(),
        createLIS(),
        createKnapsack(),
        createEditDistance(),
        createMatrixChain(),
        createCoinChange(),
        createRodCutting()
    )

    private fun createFibonacciDP() = Algorithm(
        id = "fibonacci-dp",
        name = "Fibonacci DP",
        koreanName = "피보나치 DP",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "피보나치 수열의 n번째 값을 효율적으로 계산",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(n) 또는 O(1)",
        characteristics = listOf("DP 기본 예시", "메모이제이션/타뷸레이션", "중복 계산 제거"),
        advantages = listOf("지수 시간을 선형 시간으로 개선", "DP 입문에 적합"),
        disadvantages = listOf("단순한 문제에서는 오버헤드"),
        useCases = listOf("피보나치 수열", "계단 오르기 문제", "타일링 문제"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 메모이제이션 (Top-Down)
fun fibMemo(n: Int, memo: MutableMap<Int, Long> = mutableMapOf()): Long {
    if (n <= 1) return n.toLong()
    if (n in memo) return memo[n]!!
    memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo)
    return memo[n]!!
}

// 타뷸레이션 (Bottom-Up)
fun fibTabulation(n: Int): Long {
    if (n <= 1) return n.toLong()
    val dp = LongArray(n + 1)
    dp[0] = 0; dp[1] = 1
    for (i in 2..n) dp[i] = dp[i - 1] + dp[i - 2]
    return dp[n]
}

// 공간 최적화 O(1)
fun fibOptimized(n: Int): Long {
    if (n <= 1) return n.toLong()
    var prev2 = 0L; var prev1 = 1L
    for (i in 2..n) {
        val current = prev1 + prev2
        prev2 = prev1; prev1 = current
    }
    return prev1
}

fun main() {
    println("Fibonacci(10) = ${'$'}{fibOptimized(10)}")
    println("Fibonacci(20) = ${'$'}{fibOptimized(20)}")
}
                """.trimIndent(),
                explanation = "메모이제이션과 타뷸레이션을 사용한 피보나치 DP",
                expectedOutput = """
Fibonacci(10) = 55
Fibonacci(20) = 6765
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("lis", "lcs"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createLCS() = Algorithm(
        id = "lcs",
        name = "Longest Common Subsequence",
        koreanName = "최장 공통 부분 수열",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "두 문자열의 가장 긴 공통 부분 수열 찾기",
        timeComplexity = TimeComplexity(
            best = "O(mn)",
            average = "O(mn)",
            worst = "O(mn)"
        ),
        spaceComplexity = "O(mn)",
        characteristics = listOf("2차원 DP 테이블", "부분 수열은 연속일 필요 없음"),
        advantages = listOf("문자열 비교에 효과적", "diff 도구의 기반"),
        disadvantages = listOf("긴 문자열에서 메모리 사용량"),
        useCases = listOf("diff 도구", "DNA 서열 비교", "파일 비교"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun lcs(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 1..m) {
        for (j in 1..n) {
            dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                dp[i - 1][j - 1] + 1
            } else {
                maxOf(dp[i - 1][j], dp[i][j - 1])
            }
        }
    }
    return dp[m][n]
}

fun main() {
    val s1 = "ABCDGH"
    val s2 = "AEDFHR"
    println("문자열1: ${'$'}s1")
    println("문자열2: ${'$'}s2")
    println("LCS 길이: ${'$'}{lcs(s1, s2)}")
}
                """.trimIndent(),
                explanation = "DP를 사용한 최장 공통 부분 수열 길이 계산",
                expectedOutput = """
문자열1: ABCDGH
문자열2: AEDFHR
LCS 길이: 3
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("lis", "edit-distance"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createLIS() = Algorithm(
        id = "lis",
        name = "Longest Increasing Subsequence",
        koreanName = "최장 증가 부분 수열",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "가장 긴 증가하는 부분 수열 찾기",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("부분 수열은 연속일 필요 없음", "이진 탐색으로 최적화 가능"),
        advantages = listOf("O(n log n) 최적화 가능", "다양한 문제에 응용"),
        disadvantages = listOf("O(n²) 기본 구현은 느림"),
        useCases = listOf("주식 투자 전략", "박스 쌓기 문제", "최적 스케줄링"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// O(n²) 기본 구현
fun lisBasic(arr: IntArray): Int {
    val n = arr.size
    if (n == 0) return 0
    val dp = IntArray(n) { 1 }
    for (i in 1 until n) {
        for (j in 0 until i) {
            if (arr[j] < arr[i]) dp[i] = maxOf(dp[i], dp[j] + 1)
        }
    }
    return dp.max()
}

// O(n log n) 이진 탐색 최적화
fun lis(arr: IntArray): Int {
    val tails = mutableListOf<Int>()
    for (num in arr) {
        val pos = tails.binarySearch(num).let { if (it < 0) -(it + 1) else it }
        if (pos == tails.size) tails.add(num) else tails[pos] = num
    }
    return tails.size
}

fun main() {
    val arr = intArrayOf(10, 9, 2, 5, 3, 7, 101, 18)
    println("배열: ${'$'}{arr.contentToString()}")
    println("LIS 길이: ${'$'}{lis(arr)}")
}
                """.trimIndent(),
                explanation = "이진 탐색을 활용한 O(n log n) LIS",
                expectedOutput = """
배열: [10, 9, 2, 5, 3, 7, 101, 18]
LIS 길이: 4
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("lcs", "binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createKnapsack() = Algorithm(
        id = "knapsack",
        name = "Knapsack Problem",
        koreanName = "배낭 문제",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "제한된 용량에서 최대 가치 선택",
        timeComplexity = TimeComplexity(
            best = "O(nW)",
            average = "O(nW)",
            worst = "O(nW)"
        ),
        spaceComplexity = "O(nW)",
        characteristics = listOf("0/1 배낭 문제", "무한 배낭 문제", "NP-Hard 유사 다항 시간"),
        advantages = listOf("최적 부분 구조", "다양한 최적화 문제에 적용"),
        disadvantages = listOf("큰 용량에서 메모리 사용"),
        useCases = listOf("자원 할당", "투자 포트폴리오", "화물 적재"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun knapsack(weights: IntArray, values: IntArray, capacity: Int): Int {
    val n = weights.size
    val dp = Array(n + 1) { IntArray(capacity + 1) }

    for (i in 1..n) {
        for (w in 0..capacity) {
            dp[i][w] = dp[i - 1][w] // 물건 i를 넣지 않는 경우
            if (weights[i - 1] <= w) {
                dp[i][w] = maxOf(
                    dp[i][w],
                    dp[i - 1][w - weights[i - 1]] + values[i - 1]
                )
            }
        }
    }
    return dp[n][capacity]
}

fun main() {
    val weights = intArrayOf(10, 20, 30)
    val values = intArrayOf(60, 100, 120)
    val capacity = 50
    println("무게: ${'$'}{weights.joinToString()}")
    println("가치: ${'$'}{values.joinToString()}")
    println("배낭 용량: ${'$'}capacity")
    println("최대 가치: ${'$'}{knapsack(weights, values, capacity)}")
}
                """.trimIndent(),
                explanation = "DP를 사용한 0/1 배낭 문제 해결",
                expectedOutput = """
무게: 10, 20, 30
가치: 60, 100, 120
배낭 용량: 50
최대 가치: 220
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("coin-change", "subset-sum"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createEditDistance() = Algorithm(
        id = "edit-distance",
        name = "Edit Distance",
        koreanName = "편집 거리",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "한 문자열을 다른 문자열로 변환하는 최소 연산 수",
        timeComplexity = TimeComplexity(
            best = "O(mn)",
            average = "O(mn)",
            worst = "O(mn)"
        ),
        spaceComplexity = "O(mn), 최적화 시 O(min(m,n))",
        characteristics = listOf("삽입/삭제/치환 연산", "LCS와 관련", "Levenshtein Distance"),
        advantages = listOf("문자열 유사도 측정", "오타 교정에 활용"),
        disadvantages = listOf("긴 문자열에서 느림"),
        useCases = listOf("맞춤법 검사", "DNA 서열 정렬", "자연어 처리"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun editDistance(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                dp[i - 1][j - 1]
            } else {
                1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }
    return dp[m][n]
}

fun main() {
    val s1 = "kitten"
    val s2 = "sitting"
    println("'${'$'}s1' -> '${'$'}s2'")
    println("편집 거리: ${'$'}{editDistance(s1, s2)}")
}
                """.trimIndent(),
                explanation = "DP를 사용한 편집 거리(Levenshtein Distance) 계산",
                expectedOutput = """
'kitten' -> 'sitting'
편집 거리: 3
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("lcs", "hamming-distance"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createMatrixChain() = Algorithm(
        id = "matrix-chain",
        name = "Matrix Chain Multiplication",
        koreanName = "행렬 체인 곱셈",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "행렬 곱셈 순서를 최적화하여 최소 연산 수 계산",
        timeComplexity = TimeComplexity(
            best = "O(n³)",
            average = "O(n³)",
            worst = "O(n³)"
        ),
        spaceComplexity = "O(n²)",
        characteristics = listOf("괄호화 최적화", "구간 DP의 대표 문제"),
        advantages = listOf("연산 비용 최소화", "다양한 최적화 문제에 응용"),
        disadvantages = listOf("구현이 복잡"),
        useCases = listOf("행렬 계산 최적화", "파서 최적화", "다각형 삼각분할"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun matrixChainMultiplication(dims: IntArray): Int {
    val n = dims.size - 1
    val dp = Array(n) { IntArray(n) { 0 } }

    for (len in 2..n) {
        for (i in 0..n - len) {
            val j = i + len - 1
            dp[i][j] = Int.MAX_VALUE
            for (k in i until j) {
                val cost = dp[i][k] + dp[k + 1][j] + dims[i] * dims[k + 1] * dims[j + 1]
                dp[i][j] = minOf(dp[i][j], cost)
            }
        }
    }
    return dp[0][n - 1]
}

fun main() {
    val dims = intArrayOf(10, 20, 30, 40, 30)
    println("행렬 차원: ${'$'}{dims.contentToString()}")
    println("최소 곱셈 횟수: ${'$'}{matrixChainMultiplication(dims)}")
}
                """.trimIndent(),
                explanation = "구간 DP를 이용한 행렬 체인 곱셈 최적화",
                expectedOutput = """
행렬 차원: [10, 20, 30, 40, 30]
최소 곱셈 횟수: 30000
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("knapsack", "optimal-bst"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )

    private fun createCoinChange() = Algorithm(
        id = "coin-change",
        name = "Coin Change",
        koreanName = "동전 교환",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "주어진 금액을 만드는 최소 동전 수 또는 방법 수",
        timeComplexity = TimeComplexity(
            best = "O(nS)",
            average = "O(nS)",
            worst = "O(nS)"
        ),
        spaceComplexity = "O(S)",
        characteristics = listOf("무한 배낭 문제의 변형", "최소 개수 또는 조합 수"),
        advantages = listOf("효율적인 해법", "다양한 변형 가능"),
        disadvantages = listOf("큰 금액에서 메모리 사용"),
        useCases = listOf("거스름돈 계산", "환전", "자원 분배"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 최소 동전 개수
fun coinChangeMin(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1) { amount + 1 }
    dp[0] = 0
    for (i in 1..amount) {
        for (coin in coins) {
            if (coin <= i) dp[i] = minOf(dp[i], dp[i - coin] + 1)
        }
    }
    return if (dp[amount] > amount) -1 else dp[amount]
}

// 동전 조합 수
fun coinChangeWays(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1)
    dp[0] = 1
    for (coin in coins) {
        for (i in coin..amount) dp[i] += dp[i - coin]
    }
    return dp[amount]
}

fun main() {
    val coins = intArrayOf(1, 2, 5)
    val amount = 11
    println("동전: ${'$'}{coins.contentToString()}, 금액: ${'$'}amount")
    println("최소 동전 수: ${'$'}{coinChangeMin(coins, amount)}")
    println("조합 수: ${'$'}{coinChangeWays(coins, amount)}")
}
                """.trimIndent(),
                explanation = "DP를 사용한 동전 교환 문제 해결",
                expectedOutput = """
동전: [1, 2, 5], 금액: 11
최소 동전 수: 3
조합 수: 11
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("knapsack", "rod-cutting"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createRodCutting() = Algorithm(
        id = "rod-cutting",
        name = "Rod Cutting",
        koreanName = "막대 자르기",
        category = AlgorithmCategory.DYNAMIC_PROGRAMMING,
        purpose = "막대를 잘라서 최대 이익 얻기",
        timeComplexity = TimeComplexity(
            best = "O(n²)",
            average = "O(n²)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("무한 배낭 문제의 변형", "분할 최적화 문제"),
        advantages = listOf("최적 분할 전략 도출", "직관적인 DP 적용"),
        disadvantages = listOf("큰 길이에서 시간 증가"),
        useCases = listOf("재료 절단 최적화", "자원 분배"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun rodCutting(prices: IntArray, n: Int): Int {
    val dp = IntArray(n + 1)
    for (i in 1..n) {
        for (j in 1..minOf(i, prices.size)) {
            dp[i] = maxOf(dp[i], prices[j - 1] + dp[i - j])
        }
    }
    return dp[n]
}

fun rodCuttingWithCuts(prices: IntArray, n: Int): Pair<Int, List<Int>> {
    val dp = IntArray(n + 1)
    val cuts = IntArray(n + 1)
    for (i in 1..n) {
        for (j in 1..minOf(i, prices.size)) {
            if (prices[j - 1] + dp[i - j] > dp[i]) {
                dp[i] = prices[j - 1] + dp[i - j]
                cuts[i] = j
            }
        }
    }
    val result = mutableListOf<Int>()
    var remaining = n
    while (remaining > 0) { result.add(cuts[remaining]); remaining -= cuts[remaining] }
    return dp[n] to result
}

fun main() {
    val prices = intArrayOf(1, 5, 8, 9, 10, 17, 17, 20)
    val n = 8
    val (maxProfit, cuts) = rodCuttingWithCuts(prices, n)
    println("길이별 가격: ${'$'}{prices.contentToString()}")
    println("막대 길이: ${'$'}n, 최대 이익: ${'$'}maxProfit")
    println("절단 위치: ${'$'}cuts")
}
                """.trimIndent(),
                explanation = "DP를 사용한 막대 자르기 최적화",
                expectedOutput = """
길이별 가격: [1, 5, 8, 9, 10, 17, 17, 20]
막대 길이: 8, 최대 이익: 22
절단 위치: [2, 6]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("knapsack", "coin-change"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )
}
