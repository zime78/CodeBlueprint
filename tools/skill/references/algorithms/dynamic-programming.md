# 동적 프로그래밍 (Dynamic Programming)

복잡한 문제를 작은 부분 문제로 나누어 해결하고, 그 결과를 저장하여 재사용하는 알고리즘입니다.

---

## 1. Fibonacci (피보나치)

**목적**: 피보나치 수열의 n번째 값 계산

**시간 복잡도**: O(n)

**공간 복잡도**: O(n) 메모이제이션, O(1) 반복

**특징**:
- DP의 가장 기본적인 예시
- 메모이제이션 또는 타뷸레이션
- 중복 계산 제거

**장점**:
- 지수 시간을 선형 시간으로 개선
- 이해하기 쉬운 DP 입문

**단점**:
- 단순한 문제에서는 오버헤드

**활용 예시**:
- 피보나치 수열
- 계단 오르기 문제

**난이도**: 낮음 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
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
    dp[0] = 0
    dp[1] = 1

    for (i in 2..n) {
        dp[i] = dp[i - 1] + dp[i - 2]
    }

    return dp[n]
}

// 공간 최적화 O(1)
fun fibOptimized(n: Int): Long {
    if (n <= 1) return n.toLong()

    var prev2 = 0L
    var prev1 = 1L

    for (i in 2..n) {
        val current = prev1 + prev2
        prev2 = prev1
        prev1 = current
    }

    return prev1
}

// 행렬 거듭제곱 O(log n)
fun fibMatrix(n: Int): Long {
    if (n <= 1) return n.toLong()

    fun multiply(a: Array<LongArray>, b: Array<LongArray>): Array<LongArray> {
        return arrayOf(
            longArrayOf(
                a[0][0] * b[0][0] + a[0][1] * b[1][0],
                a[0][0] * b[0][1] + a[0][1] * b[1][1]
            ),
            longArrayOf(
                a[1][0] * b[0][0] + a[1][1] * b[1][0],
                a[1][0] * b[0][1] + a[1][1] * b[1][1]
            )
        )
    }

    fun power(matrix: Array<LongArray>, p: Int): Array<LongArray> {
        if (p == 1) return matrix
        if (p % 2 == 0) {
            val half = power(matrix, p / 2)
            return multiply(half, half)
        }
        return multiply(matrix, power(matrix, p - 1))
    }

    val base = arrayOf(longArrayOf(1, 1), longArrayOf(1, 0))
    val result = power(base, n)
    return result[0][1]
}
```

**관련 알고리즘**: LIS, LCS

---

## 2. LCS (Longest Common Subsequence, 최장 공통 부분 수열)

**목적**: 두 문자열의 가장 긴 공통 부분 수열 찾기

**시간 복잡도**: O(mn)

**공간 복잡도**: O(mn), 최적화 시 O(min(m, n))

**특징**:
- 부분 수열은 연속일 필요 없음
- 2차원 DP 테이블

**장점**:
- 문자열 비교에 효과적
- diff 도구의 기반

**단점**:
- 긴 문자열에서 메모리 사용량

**활용 예시**:
- diff 도구 (Git)
- DNA 서열 비교
- 파일 비교

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// LCS 길이
fun lcsLength(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 1..m) {
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1
            } else {
                dp[i][j] = maxOf(dp[i - 1][j], dp[i][j - 1])
            }
        }
    }

    return dp[m][n]
}

// LCS 문자열 복원
fun lcs(s1: String, s2: String): String {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 1..m) {
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1
            } else {
                dp[i][j] = maxOf(dp[i - 1][j], dp[i][j - 1])
            }
        }
    }

    // 역추적으로 LCS 복원
    val result = StringBuilder()
    var i = m
    var j = n

    while (i > 0 && j > 0) {
        when {
            s1[i - 1] == s2[j - 1] -> {
                result.append(s1[i - 1])
                i--
                j--
            }
            dp[i - 1][j] > dp[i][j - 1] -> i--
            else -> j--
        }
    }

    return result.reverse().toString()
}

// 공간 최적화 (길이만)
fun lcsLengthOptimized(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    var prev = IntArray(n + 1)
    var curr = IntArray(n + 1)

    for (i in 1..m) {
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                curr[j] = prev[j - 1] + 1
            } else {
                curr[j] = maxOf(prev[j], curr[j - 1])
            }
        }
        val temp = prev
        prev = curr
        curr = temp
        curr.fill(0)
    }

    return prev[n]
}
```

**관련 알고리즘**: LIS, Edit Distance

---

## 3. LIS (Longest Increasing Subsequence, 최장 증가 부분 수열)

**목적**: 가장 긴 증가하는 부분 수열 찾기

**시간 복잡도**: O(n²) 또는 O(n log n)

**공간 복잡도**: O(n)

**특징**:
- 부분 수열은 연속일 필요 없음
- 이진 탐색으로 최적화 가능

**장점**:
- O(n log n) 최적화 가능
- 다양한 문제에 응용

**단점**:
- O(n²) 기본 구현은 느림

**활용 예시**:
- 주식 투자 전략
- 박스 쌓기 문제
- 최적 스케줄링

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
// O(n²) 기본 구현
fun lisBasic(arr: IntArray): Int {
    val n = arr.size
    if (n == 0) return 0

    val dp = IntArray(n) { 1 }

    for (i in 1 until n) {
        for (j in 0 until i) {
            if (arr[j] < arr[i]) {
                dp[i] = maxOf(dp[i], dp[j] + 1)
            }
        }
    }

    return dp.max()
}

// O(n log n) 이진 탐색 최적화
fun lis(arr: IntArray): Int {
    val n = arr.size
    if (n == 0) return 0

    val tails = mutableListOf<Int>()

    for (num in arr) {
        val pos = tails.binarySearch(num).let {
            if (it < 0) -(it + 1) else it
        }

        if (pos == tails.size) {
            tails.add(num)
        } else {
            tails[pos] = num
        }
    }

    return tails.size
}

// LIS 실제 수열 복원
fun lisWithSequence(arr: IntArray): List<Int> {
    val n = arr.size
    if (n == 0) return emptyList()

    val tails = mutableListOf<Int>()
    val indices = mutableListOf<Int>()
    val prev = IntArray(n) { -1 }

    for (i in arr.indices) {
        val pos = tails.binarySearch(arr[i]).let {
            if (it < 0) -(it + 1) else it
        }

        if (pos == tails.size) {
            tails.add(arr[i])
            indices.add(i)
        } else {
            tails[pos] = arr[i]
            indices[pos] = i
        }

        if (pos > 0) {
            prev[i] = indices[pos - 1]
        }
    }

    // 역추적
    val result = mutableListOf<Int>()
    var idx = indices.last()
    while (idx != -1) {
        result.add(arr[idx])
        idx = prev[idx]
    }

    return result.reversed()
}

// Lower Bound 직접 구현
fun lowerBound(list: List<Int>, target: Int): Int {
    var left = 0
    var right = list.size

    while (left < right) {
        val mid = left + (right - left) / 2
        if (list[mid] < target) {
            left = mid + 1
        } else {
            right = mid
        }
    }

    return left
}
```

**관련 알고리즘**: LCS, 이진 탐색

---

## 4. Knapsack (배낭 문제)

**목적**: 제한된 용량에서 최대 가치 선택

**시간 복잡도**: O(nW) - n: 아이템 수, W: 용량

**공간 복잡도**: O(nW), 최적화 시 O(W)

**특징**:
- 0/1 배낭 문제 (각 아이템 한 번만)
- 무한 배낭 문제 (아이템 무제한)
- NP-Hard 문제의 유사 다항 시간 해법

**장점**:
- 최적 부분 구조
- 다양한 최적화 문제에 적용

**단점**:
- 큰 용량에서 메모리 사용

**활용 예시**:
- 자원 할당
- 투자 포트폴리오
- 화물 적재

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// 0/1 배낭 문제
fun knapsack01(weights: IntArray, values: IntArray, capacity: Int): Int {
    val n = weights.size
    val dp = Array(n + 1) { IntArray(capacity + 1) }

    for (i in 1..n) {
        for (w in 0..capacity) {
            dp[i][w] = dp[i - 1][w] // 아이템 선택 안함

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

// 0/1 배낭 - 공간 최적화
fun knapsack01Optimized(weights: IntArray, values: IntArray, capacity: Int): Int {
    val dp = IntArray(capacity + 1)

    for (i in weights.indices) {
        for (w in capacity downTo weights[i]) {
            dp[w] = maxOf(dp[w], dp[w - weights[i]] + values[i])
        }
    }

    return dp[capacity]
}

// 선택한 아이템 추적
fun knapsack01WithItems(
    weights: IntArray,
    values: IntArray,
    capacity: Int
): Pair<Int, List<Int>> {
    val n = weights.size
    val dp = Array(n + 1) { IntArray(capacity + 1) }

    for (i in 1..n) {
        for (w in 0..capacity) {
            dp[i][w] = dp[i - 1][w]

            if (weights[i - 1] <= w) {
                dp[i][w] = maxOf(
                    dp[i][w],
                    dp[i - 1][w - weights[i - 1]] + values[i - 1]
                )
            }
        }
    }

    // 역추적
    val items = mutableListOf<Int>()
    var w = capacity
    for (i in n downTo 1) {
        if (dp[i][w] != dp[i - 1][w]) {
            items.add(i - 1)
            w -= weights[i - 1]
        }
    }

    return dp[n][capacity] to items.reversed()
}

// 무한 배낭 문제 (아이템 무제한)
fun unboundedKnapsack(weights: IntArray, values: IntArray, capacity: Int): Int {
    val dp = IntArray(capacity + 1)

    for (w in 1..capacity) {
        for (i in weights.indices) {
            if (weights[i] <= w) {
                dp[w] = maxOf(dp[w], dp[w - weights[i]] + values[i])
            }
        }
    }

    return dp[capacity]
}
```

**관련 알고리즘**: Coin Change, Subset Sum

---

## 5. Edit Distance (편집 거리, Levenshtein Distance)

**목적**: 한 문자열을 다른 문자열로 변환하는 최소 연산 수

**시간 복잡도**: O(mn)

**공간 복잡도**: O(mn), 최적화 시 O(min(m, n))

**특징**:
- 삽입, 삭제, 치환 연산
- LCS와 관련

**장점**:
- 문자열 유사도 측정
- 오타 교정에 활용

**단점**:
- 긴 문자열에서 느림

**활용 예시**:
- 맞춤법 검사
- DNA 서열 정렬
- 자연어 처리

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// 기본 편집 거리
fun editDistance(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    // 베이스 케이스
    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = 1 + minOf(
                    dp[i - 1][j],     // 삭제
                    dp[i][j - 1],     // 삽입
                    dp[i - 1][j - 1]  // 치환
                )
            }
        }
    }

    return dp[m][n]
}

// 공간 최적화
fun editDistanceOptimized(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    var prev = IntArray(n + 1) { it }
    var curr = IntArray(n + 1)

    for (i in 1..m) {
        curr[0] = i
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                curr[j] = prev[j - 1]
            } else {
                curr[j] = 1 + minOf(prev[j], curr[j - 1], prev[j - 1])
            }
        }
        val temp = prev
        prev = curr
        curr = temp
    }

    return prev[n]
}

// 연산 추적
sealed class EditOp {
    data class Insert(val char: Char, val pos: Int) : EditOp()
    data class Delete(val pos: Int) : EditOp()
    data class Replace(val char: Char, val pos: Int) : EditOp()
    object Match : EditOp()
}

fun editDistanceWithOps(s1: String, s2: String): Pair<Int, List<EditOp>> {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }

    // 역추적
    val ops = mutableListOf<EditOp>()
    var i = m
    var j = n

    while (i > 0 || j > 0) {
        when {
            i > 0 && j > 0 && s1[i - 1] == s2[j - 1] -> {
                ops.add(EditOp.Match)
                i--
                j--
            }
            i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1 -> {
                ops.add(EditOp.Replace(s2[j - 1], i - 1))
                i--
                j--
            }
            j > 0 && dp[i][j] == dp[i][j - 1] + 1 -> {
                ops.add(EditOp.Insert(s2[j - 1], i))
                j--
            }
            else -> {
                ops.add(EditOp.Delete(i - 1))
                i--
            }
        }
    }

    return dp[m][n] to ops.reversed()
}
```

**관련 알고리즘**: LCS, Hamming Distance

---

## 6. Matrix Chain Multiplication (행렬 체인 곱셈)

**목적**: 행렬 곱셈 순서를 최적화하여 최소 연산 수 계산

**시간 복잡도**: O(n³)

**공간 복잡도**: O(n²)

**특징**:
- 괄호화 최적화
- 구간 DP의 대표 문제

**장점**:
- 연산 비용 최소화
- 다양한 최적화 문제에 응용

**단점**:
- 구현이 복잡

**활용 예시**:
- 행렬 계산 최적화
- 파서 최적화
- 다각형 삼각분할

**난이도**: 높음 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
// 최소 곱셈 횟수 계산
fun matrixChainMultiplication(dims: IntArray): Int {
    val n = dims.size - 1
    val dp = Array(n) { IntArray(n) { 0 } }

    // 길이가 2 이상인 부분 문제
    for (len in 2..n) {
        for (i in 0..n - len) {
            val j = i + len - 1
            dp[i][j] = Int.MAX_VALUE

            for (k in i until j) {
                val cost = dp[i][k] + dp[k + 1][j] +
                           dims[i] * dims[k + 1] * dims[j + 1]
                dp[i][j] = minOf(dp[i][j], cost)
            }
        }
    }

    return dp[0][n - 1]
}

// 최적 괄호화 출력
fun matrixChainWithParenthesis(dims: IntArray): Pair<Int, String> {
    val n = dims.size - 1
    val dp = Array(n) { IntArray(n) { 0 } }
    val split = Array(n) { IntArray(n) { 0 } }

    for (len in 2..n) {
        for (i in 0..n - len) {
            val j = i + len - 1
            dp[i][j] = Int.MAX_VALUE

            for (k in i until j) {
                val cost = dp[i][k] + dp[k + 1][j] +
                           dims[i] * dims[k + 1] * dims[j + 1]
                if (cost < dp[i][j]) {
                    dp[i][j] = cost
                    split[i][j] = k
                }
            }
        }
    }

    fun buildParenthesis(i: Int, j: Int): String {
        if (i == j) return "M${i + 1}"
        return "(${buildParenthesis(i, split[i][j])} × ${buildParenthesis(split[i][j] + 1, j)})"
    }

    return dp[0][n - 1] to buildParenthesis(0, n - 1)
}
```

**관련 알고리즘**: 구간 DP, Optimal BST

---

## 7. Coin Change (동전 교환)

**목적**: 주어진 금액을 만드는 최소 동전 수 또는 방법 수

**시간 복잡도**: O(nS) - n: 동전 종류, S: 목표 금액

**공간 복잡도**: O(S)

**특징**:
- 무한 배낭 문제의 변형
- 최소 개수 또는 조합 수

**장점**:
- 효율적인 해법
- 다양한 변형 가능

**단점**:
- 큰 금액에서 메모리 사용

**활용 예시**:
- 거스름돈 계산
- 환전
- 자원 분배

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// 최소 동전 개수
fun coinChangeMin(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1) { amount + 1 }
    dp[0] = 0

    for (i in 1..amount) {
        for (coin in coins) {
            if (coin <= i) {
                dp[i] = minOf(dp[i], dp[i - coin] + 1)
            }
        }
    }

    return if (dp[amount] > amount) -1 else dp[amount]
}

// 동전 조합 수 (순서 상관 없음)
fun coinChangeWays(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1)
    dp[0] = 1

    for (coin in coins) {
        for (i in coin..amount) {
            dp[i] += dp[i - coin]
        }
    }

    return dp[amount]
}

// 동전 순열 수 (순서 상관 있음)
fun coinChangePermutations(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1)
    dp[0] = 1

    for (i in 1..amount) {
        for (coin in coins) {
            if (coin <= i) {
                dp[i] += dp[i - coin]
            }
        }
    }

    return dp[amount]
}

// 사용된 동전 추적
fun coinChangeWithCoins(coins: IntArray, amount: Int): List<Int>? {
    val dp = IntArray(amount + 1) { amount + 1 }
    val parent = IntArray(amount + 1) { -1 }
    dp[0] = 0

    for (i in 1..amount) {
        for (coin in coins) {
            if (coin <= i && dp[i - coin] + 1 < dp[i]) {
                dp[i] = dp[i - coin] + 1
                parent[i] = coin
            }
        }
    }

    if (dp[amount] > amount) return null

    val result = mutableListOf<Int>()
    var remaining = amount
    while (remaining > 0) {
        result.add(parent[remaining])
        remaining -= parent[remaining]
    }

    return result
}
```

**관련 알고리즘**: Knapsack, Rod Cutting

---

## 8. Rod Cutting (막대 자르기)

**목적**: 막대를 잘라서 최대 이익 얻기

**시간 복잡도**: O(n²)

**공간 복잡도**: O(n)

**특징**:
- 무한 배낭 문제의 변형
- 분할 최적화 문제

**장점**:
- 최적 분할 전략 도출
- 직관적인 DP 적용

**단점**:
- 큰 길이에서 시간 증가

**활용 예시**:
- 재료 절단 최적화
- 자원 분배

**난이도**: 중간 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
// 최대 이익 계산
fun rodCutting(prices: IntArray, n: Int): Int {
    val dp = IntArray(n + 1)

    for (i in 1..n) {
        for (j in 1..i) {
            if (j <= prices.size) {
                dp[i] = maxOf(dp[i], prices[j - 1] + dp[i - j])
            }
        }
    }

    return dp[n]
}

// 절단 위치 추적
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

    // 절단 복원
    val result = mutableListOf<Int>()
    var remaining = n
    while (remaining > 0) {
        result.add(cuts[remaining])
        remaining -= cuts[remaining]
    }

    return dp[n] to result
}

// 절단 비용이 있는 경우
fun rodCuttingWithCost(prices: IntArray, n: Int, cutCost: Int): Int {
    val dp = IntArray(n + 1)

    for (i in 1..n) {
        // 자르지 않는 경우
        if (i <= prices.size) {
            dp[i] = prices[i - 1]
        }

        // 자르는 경우 (비용 차감)
        for (j in 1 until i) {
            dp[i] = maxOf(dp[i], dp[j] + dp[i - j] - cutCost)
        }
    }

    return dp[n]
}
```

**관련 알고리즘**: Knapsack, Coin Change

