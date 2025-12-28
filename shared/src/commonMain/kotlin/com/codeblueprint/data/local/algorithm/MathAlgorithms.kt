package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 수학 알고리즘 데이터 (2개)
 *
 * Euclidean GCD, Sieve of Eratosthenes
 */
internal object MathAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createEuclideanGCD(),
        createSieveOfEratosthenes()
    )

    private fun createEuclideanGCD() = Algorithm(
        id = "euclidean-gcd",
        name = "Euclidean GCD",
        koreanName = "유클리드 최대공약수",
        category = AlgorithmCategory.MATH,
        purpose = "두 수의 최대공약수 계산",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log min(a,b))",
            worst = "O(log min(a,b))"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("가장 오래된 알고리즘", "나눗셈 기반"),
        advantages = listOf("매우 효율적", "구현 간단"),
        disadvantages = listOf("큰 수에서 오버플로우 주의"),
        useCases = listOf("분수 약분", "암호학 (RSA)", "모듈러 역원"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 반복문 버전
fun gcd(a: Int, b: Int): Int {
    var x = a
    var y = b
    while (y != 0) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}

// 재귀 버전
fun gcdRecursive(a: Int, b: Int): Int =
    if (b == 0) a else gcdRecursive(b, a % b)

// 최소공배수
fun lcm(a: Int, b: Int): Int = a / gcd(a, b) * b

fun main() {
    val a = 48
    val b = 18
    println("a = ${'$'}a, b = ${'$'}b")
    println("GCD(${'$'}a, ${'$'}b) = ${'$'}{gcd(a, b)}")
    println("LCM(${'$'}a, ${'$'}b) = ${'$'}{lcm(a, b)}")
}
                """.trimIndent(),
                explanation = "유클리드 호제법을 이용한 GCD 계산",
                expectedOutput = """
a = 48, b = 18
GCD(48, 18) = 6
LCM(48, 18) = 144
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("extended-gcd", "lcm"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createSieveOfEratosthenes() = Algorithm(
        id = "sieve-eratosthenes",
        name = "Sieve of Eratosthenes",
        koreanName = "에라토스테네스의 체",
        category = AlgorithmCategory.MATH,
        purpose = "특정 범위 내 모든 소수 찾기",
        timeComplexity = TimeComplexity(
            best = "O(n log log n)",
            average = "O(n log log n)",
            worst = "O(n log log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("고대 그리스 알고리즘", "배수 제거 방식"),
        advantages = listOf("매우 효율적", "구현 간단"),
        disadvantages = listOf("메모리 사용량"),
        useCases = listOf("소수 목록 생성", "소인수분해", "암호학"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun sieveOfEratosthenes(n: Int): List<Int> {
    val isPrime = BooleanArray(n + 1) { true }
    isPrime[0] = false
    isPrime[1] = false

    var i = 2
    while (i * i <= n) {
        if (isPrime[i]) {
            var j = i * i
            while (j <= n) {
                isPrime[j] = false
                j += i
            }
        }
        i++
    }

    return isPrime.indices.filter { isPrime[it] }
}

fun main() {
    val n = 30
    val primes = sieveOfEratosthenes(n)
    println("1부터 ${'$'}n까지의 소수:")
    println(primes.joinToString())
    println("소수 개수: ${'$'}{primes.size}")
}
                """.trimIndent(),
                explanation = "배수를 제거하여 소수를 찾는 에라토스테네스의 체",
                expectedOutput = """
1부터 30까지의 소수:
2, 3, 5, 7, 11, 13, 17, 19, 23, 29
소수 개수: 10
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("miller-rabin", "prime-factorization"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )
}
