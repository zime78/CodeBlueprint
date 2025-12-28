package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 분할정복 알고리즘 데이터 (3개)
 *
 * Strassen Matrix, Closest Pair, Karatsuba
 */
internal object DivideConquerAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createStrassenMatrix(),
        createClosestPair(),
        createKaratsuba()
    )

    private fun createStrassenMatrix() = Algorithm(
        id = "strassen-matrix",
        name = "Strassen's Matrix Multiplication",
        koreanName = "스트라센 행렬 곱셈",
        category = AlgorithmCategory.DIVIDE_CONQUER,
        purpose = "행렬 곱셈을 더 빠르게 수행",
        timeComplexity = TimeComplexity(
            best = "O(n^2.807)",
            average = "O(n^2.807)",
            worst = "O(n^2.807)"
        ),
        spaceComplexity = "O(n²)",
        characteristics = listOf("7번의 곱셈으로 2×2 행렬 곱 수행", "일반적인 8번 → 7번으로 감소"),
        advantages = listOf("큰 행렬에서 효율적", "이론적으로 중요"),
        disadvantages = listOf("작은 행렬에서 오버헤드", "수치적 불안정성"),
        useCases = listOf("대규모 행렬 연산", "과학 계산", "컴퓨터 그래픽스"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
typealias Matrix = Array<IntArray>

fun strassen(a: Matrix, b: Matrix): Matrix {
    val n = a.size
    if (n == 1) return arrayOf(intArrayOf(a[0][0] * b[0][0]))

    val half = n / 2

    // 행렬 분할
    fun sub(m: Matrix, r: Int, c: Int) = Array(half) { i ->
        IntArray(half) { j -> m[r + i][c + j] }
    }
    fun add(x: Matrix, y: Matrix) = Array(half) { i ->
        IntArray(half) { j -> x[i][j] + y[i][j] }
    }
    fun sub(x: Matrix, y: Matrix) = Array(half) { i ->
        IntArray(half) { j -> x[i][j] - y[i][j] }
    }

    val a11 = sub(a, 0, 0); val a12 = sub(a, 0, half)
    val a21 = sub(a, half, 0); val a22 = sub(a, half, half)
    val b11 = sub(b, 0, 0); val b12 = sub(b, 0, half)
    val b21 = sub(b, half, 0); val b22 = sub(b, half, half)

    // 7개의 곱셈 (핵심)
    val m1 = strassen(add(a11, a22), add(b11, b22))
    val m2 = strassen(add(a21, a22), b11)
    val m3 = strassen(a11, sub(b12, b22))
    val m4 = strassen(a22, sub(b21, b11))
    val m5 = strassen(add(a11, a12), b22)
    val m6 = strassen(sub(a21, a11), add(b11, b12))
    val m7 = strassen(sub(a12, a22), add(b21, b22))

    // 결과 조합
    val c = Array(n) { IntArray(n) }
    for (i in 0 until half) for (j in 0 until half) {
        c[i][j] = m1[i][j] + m4[i][j] - m5[i][j] + m7[i][j]
        c[i][j + half] = m3[i][j] + m5[i][j]
        c[i + half][j] = m2[i][j] + m4[i][j]
        c[i + half][j + half] = m1[i][j] - m2[i][j] + m3[i][j] + m6[i][j]
    }
    return c
}

fun main() {
    println("스트라센 알고리즘: O(n^2.807) vs 일반 O(n³)")
    println("큰 행렬에서 약 10% 성능 향상")
}
                """.trimIndent(),
                explanation = "7번의 곱셈으로 행렬 곱을 수행하는 스트라센 알고리즘",
                expectedOutput = """
스트라센 알고리즘: O(n^2.807) vs 일반 O(n³)
큰 행렬에서 약 10% 성능 향상
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("matrix-chain"),
        difficulty = Difficulty.HIGH,
        frequency = 1
    )

    private fun createClosestPair() = Algorithm(
        id = "closest-pair",
        name = "Closest Pair of Points",
        koreanName = "최근접 점 쌍",
        category = AlgorithmCategory.DIVIDE_CONQUER,
        purpose = "2D 평면에서 가장 가까운 두 점 찾기",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("분할 정복으로 O(n log n) 달성", "Brute Force O(n²)보다 효율적"),
        advantages = listOf("효율적인 기하 알고리즘", "실제 응용에 유용"),
        disadvantages = listOf("구현이 복잡"),
        useCases = listOf("충돌 감지", "클러스터링", "컴퓨터 그래픽스"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.math.sqrt
import kotlin.math.min
import kotlin.math.abs

data class Point(val x: Double, val y: Double)

fun distance(p1: Point, p2: Point): Double {
    val dx = p1.x - p2.x; val dy = p1.y - p2.y
    return sqrt(dx * dx + dy * dy)
}

fun closestPair(points: List<Point>): Double {
    val sortedByX = points.sortedBy { it.x }
    return closestPairRec(sortedByX)
}

fun closestPairRec(points: List<Point>): Double {
    val n = points.size
    if (n <= 3) {
        var minD = Double.MAX_VALUE
        for (i in 0 until n) for (j in i + 1 until n)
            minD = min(minD, distance(points[i], points[j]))
        return minD
    }

    val mid = n / 2
    val midX = points[mid].x

    var minD = min(
        closestPairRec(points.subList(0, mid)),
        closestPairRec(points.subList(mid, n))
    )

    val strip = points.filter { abs(it.x - midX) < minD }.sortedBy { it.y }
    for (i in strip.indices) {
        var j = i + 1
        while (j < strip.size && strip[j].y - strip[i].y < minD) {
            minD = min(minD, distance(strip[i], strip[j]))
            j++
        }
    }
    return minD
}

fun main() {
    val points = listOf(Point(2.0, 3.0), Point(12.0, 30.0),
        Point(40.0, 50.0), Point(5.0, 1.0), Point(12.0, 10.0))
    println("최근접 점 쌍 거리: ${'$'}{"%.4f".format(closestPair(points))}")
}
                """.trimIndent(),
                explanation = "분할정복을 사용한 최근접 점 쌍 알고리즘",
                expectedOutput = """
최근접 점 쌍 거리: 5.0000
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("convex-hull"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    private fun createKaratsuba() = Algorithm(
        id = "karatsuba",
        name = "Karatsuba Multiplication",
        koreanName = "카라츠바 곱셈",
        category = AlgorithmCategory.DIVIDE_CONQUER,
        purpose = "큰 정수의 곱셈을 효율적으로 수행",
        timeComplexity = TimeComplexity(
            best = "O(n^1.585)",
            average = "O(n^1.585)",
            worst = "O(n^1.585)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("3번의 곱셈으로 계산", "일반적인 4번 → 3번으로 감소"),
        advantages = listOf("큰 수 곱셈에 효율적", "분할 정복의 좋은 예"),
        disadvantages = listOf("작은 수에서 오버헤드"),
        useCases = listOf("암호학", "대수 시스템", "임의 정밀도 연산"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import java.math.BigInteger
import kotlin.math.max

fun karatsuba(x: BigInteger, y: BigInteger): BigInteger {
    val n = max(x.bitLength(), y.bitLength())

    // 베이스 케이스
    if (n <= 32) return x.multiply(y)

    val half = (n + 1) / 2

    // x = x1 * 2^half + x0
    val x1 = x.shiftRight(half)
    val x0 = x.subtract(x1.shiftLeft(half))
    val y1 = y.shiftRight(half)
    val y0 = y.subtract(y1.shiftLeft(half))

    // 3번의 재귀 곱셈
    val z2 = karatsuba(x1, y1)           // x1 * y1
    val z0 = karatsuba(x0, y0)           // x0 * y0
    val z1 = karatsuba(x1.add(x0), y1.add(y0))
        .subtract(z2).subtract(z0)        // (x1+x0)(y1+y0) - z2 - z0

    // 결과: z2 * 2^(2*half) + z1 * 2^half + z0
    return z2.shiftLeft(2 * half).add(z1.shiftLeft(half)).add(z0)
}

fun main() {
    val a = BigInteger("12345678901234567890")
    val b = BigInteger("98765432109876543210")
    println("a = ${'$'}a")
    println("b = ${'$'}b")
    println("a * b = ${'$'}{karatsuba(a, b)}")
}
                """.trimIndent(),
                explanation = "3번의 곱셈으로 큰 수를 곱하는 카라츠바 알고리즘",
                expectedOutput = """
a = 12345678901234567890
b = 98765432109876543210
a * b = 1219326311370217952237463801111263526900
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("fft"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )
}
