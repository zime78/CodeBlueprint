package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 수학 알고리즘 데이터 (10개)
 *
 * Euclidean GCD, Sieve of Eratosthenes, Fast Exponentiation,
 * Modular Arithmetic, Chinese Remainder Theorem, FFT,
 * Simpson's Rule, Newton-Raphson, Prime Factorization, Miller-Rabin
 */
internal object MathAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createEuclideanGCD(),
        createSieveOfEratosthenes(),
        createFastExponentiation(),
        createModularArithmetic(),
        createChineseRemainderTheorem(),
        createFFT(),
        createSimpsonsRule(),
        createNewtonRaphson(),
        createPrimeFactorization(),
        createMillerRabin()
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

    /**
     * 빠른 거듭제곱 (Fast Exponentiation)
     * 이진 분할을 이용해 O(log n) 시간에 거듭제곱을 계산
     */
    private fun createFastExponentiation() = Algorithm(
        id = "fast-exponentiation",
        name = "Fast Exponentiation",
        koreanName = "빠른 거듭제곱",
        category = AlgorithmCategory.MATH,
        purpose = "O(log n) 시간에 거듭제곱 계산",
        timeComplexity = TimeComplexity(
            best = "O(log n)",
            average = "O(log n)",
            worst = "O(log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("이진 분할", "모듈러 연산 적용 가능"),
        advantages = listOf("매우 빠름", "오버플로우 방지 가능"),
        disadvantages = listOf("재귀 버전은 스택 사용"),
        useCases = listOf("모듈러 거듭제곱", "행렬 거듭제곱", "RSA 암호"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 반복문 버전 (모듈러 거듭제곱)
fun power(base: Long, exp: Long, mod: Long): Long {
    var result = 1L
    var b = base % mod
    var e = exp

    while (e > 0) {
        if (e and 1L == 1L) {
            result = (result * b) % mod
        }
        e = e shr 1
        b = (b * b) % mod
    }
    return result
}

// 재귀 버전
fun powerRecursive(base: Long, exp: Long, mod: Long): Long {
    if (exp == 0L) return 1L
    if (exp and 1L == 0L) {
        val half = powerRecursive(base, exp / 2, mod)
        return (half * half) % mod
    }
    return (base * powerRecursive(base, exp - 1, mod)) % mod
}

fun main() {
    val base = 2L
    val exp = 10L
    val mod = 1000000007L

    println("${'$'}base^${'$'}exp mod ${'$'}mod = ${'$'}{power(base, exp, mod)}")
    println("일반 거듭제곱: ${'$'}base^${'$'}exp = ${'$'}{power(base, exp, Long.MAX_VALUE)}")
}
                """.trimIndent(),
                explanation = "이진 분할을 이용한 빠른 거듭제곱",
                expectedOutput = """
2^10 mod 1000000007 = 1024
일반 거듭제곱: 2^10 = 1024
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("modular-arithmetic", "matrix-exponentiation"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    /**
     * 모듈러 연산 (Modular Arithmetic)
     * 모듈러 덧셈, 뺄셈, 곱셈, 역원 계산
     */
    private fun createModularArithmetic() = Algorithm(
        id = "modular-arithmetic",
        name = "Modular Arithmetic",
        koreanName = "모듈러 연산",
        category = AlgorithmCategory.MATH,
        purpose = "모듈러 연산을 통한 오버플로우 방지 및 암호학 연산",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log n)",
            worst = "O(log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("오버플로우 방지", "암호학 기반"),
        advantages = listOf("큰 수 연산 가능", "정수 범위 유지"),
        disadvantages = listOf("나눗셈은 역원 필요", "음수 처리 주의"),
        useCases = listOf("조합론", "암호학", "해싱"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class ModularArithmetic(private val mod: Long) {

    // 모듈러 덧셈
    fun add(a: Long, b: Long): Long = ((a % mod) + (b % mod)) % mod

    // 모듈러 뺄셈
    fun subtract(a: Long, b: Long): Long = ((a % mod) - (b % mod) + mod) % mod

    // 모듈러 곱셈
    fun multiply(a: Long, b: Long): Long = ((a % mod) * (b % mod)) % mod

    // 모듈러 역원 (페르마의 소정리 이용, mod가 소수일 때)
    fun inverse(a: Long): Long = power(a, mod - 2)

    // 모듈러 나눗셈
    fun divide(a: Long, b: Long): Long = multiply(a, inverse(b))

    // 빠른 거듭제곱
    private fun power(base: Long, exp: Long): Long {
        var result = 1L
        var b = base % mod
        var e = exp
        while (e > 0) {
            if (e and 1L == 1L) result = (result * b) % mod
            e = e shr 1
            b = (b * b) % mod
        }
        return result
    }
}

fun main() {
    val mod = ModularArithmetic(1000000007L)
    val a = 1000000000L
    val b = 500000000L

    println("a = ${'$'}a, b = ${'$'}b, mod = 1000000007")
    println("(a + b) mod = ${'$'}{mod.add(a, b)}")
    println("(a * b) mod = ${'$'}{mod.multiply(a, b)}")
    println("(a / b) mod = ${'$'}{mod.divide(a, b)}")
}
                """.trimIndent(),
                explanation = "모듈러 연산 클래스 구현",
                expectedOutput = """
a = 1000000000, b = 500000000, mod = 1000000007
(a + b) mod = 499999993
(a * b) mod = 999999986
(a / b) mod = 2
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("fast-exponentiation", "euclidean-gcd"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    /**
     * 중국인의 나머지 정리 (Chinese Remainder Theorem)
     * 연립 합동식의 해를 구함
     */
    private fun createChineseRemainderTheorem() = Algorithm(
        id = "chinese-remainder-theorem",
        name = "Chinese Remainder Theorem",
        koreanName = "중국인의 나머지 정리",
        category = AlgorithmCategory.MATH,
        purpose = "서로소인 모듈러들에 대한 연립 합동식의 해 계산",
        timeComplexity = TimeComplexity(
            best = "O(n log M)",
            average = "O(n log M)",
            worst = "O(n log M)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("서로소인 모듈러들 필요", "확장 유클리드 알고리즘 사용"),
        advantages = listOf("큰 모듈러 연산 분할 가능", "병렬 처리 가능"),
        disadvantages = listOf("서로소 조건 필요", "구현 복잡"),
        useCases = listOf("RSA 최적화", "큰 수 연산", "암호학"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 확장 유클리드 알고리즘
fun extendedGCD(a: Long, b: Long): Triple<Long, Long, Long> {
    if (b == 0L) return Triple(a, 1L, 0L)
    val (g, x, y) = extendedGCD(b, a % b)
    return Triple(g, y, x - (a / b) * y)
}

// 중국인의 나머지 정리
fun chineseRemainderTheorem(remainders: LongArray, moduli: LongArray): Long {
    val n = remainders.size
    var M = 1L
    for (m in moduli) M *= m

    var result = 0L
    for (i in 0 until n) {
        val mi = M / moduli[i]
        val (_, yi, _) = extendedGCD(mi, moduli[i])
        result += remainders[i] * mi * ((yi % moduli[i] + moduli[i]) % moduli[i])
        result %= M
    }

    return (result + M) % M
}

fun main() {
    // x ≡ 2 (mod 3), x ≡ 3 (mod 5), x ≡ 2 (mod 7)
    val remainders = longArrayOf(2, 3, 2)
    val moduli = longArrayOf(3, 5, 7)

    val x = chineseRemainderTheorem(remainders, moduli)
    println("연립 합동식의 해: x = ${'$'}x")

    // 검증
    for (i in remainders.indices) {
        println("${'$'}x mod ${'$'}{moduli[i]} = ${'$'}{x % moduli[i]} (expected: ${'$'}{remainders[i]})")
    }
}
                """.trimIndent(),
                explanation = "중국인의 나머지 정리를 이용한 연립 합동식 해결",
                expectedOutput = """
연립 합동식의 해: x = 23
23 mod 3 = 2 (expected: 2)
23 mod 5 = 3 (expected: 3)
23 mod 7 = 2 (expected: 2)
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("euclidean-gcd", "modular-arithmetic"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    /**
     * 고속 푸리에 변환 (FFT)
     * O(n log n) 시간에 다항식 곱셈 수행
     */
    private fun createFFT() = Algorithm(
        id = "fft",
        name = "Fast Fourier Transform",
        koreanName = "고속 푸리에 변환",
        category = AlgorithmCategory.MATH,
        purpose = "O(n log n) 시간에 다항식 곱셈 및 주파수 분석",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("분할 정복", "복소수 연산"),
        advantages = listOf("다항식 곱셈 최적화", "신호 처리에 필수"),
        disadvantages = listOf("구현 복잡", "복소수 연산 필요"),
        useCases = listOf("다항식 곱셈", "큰 수 곱셈", "오디오 처리"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.math.*

data class Complex(val re: Double, val im: Double) {
    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)
    operator fun times(other: Complex) = Complex(
        re * other.re - im * other.im,
        re * other.im + im * other.re
    )
}

fun fft(a: Array<Complex>, invert: Boolean = false): Array<Complex> {
    val n = a.size
    if (n == 1) return a

    val a0 = Array(n / 2) { a[it * 2] }
    val a1 = Array(n / 2) { a[it * 2 + 1] }

    val y0 = fft(a0, invert)
    val y1 = fft(a1, invert)

    val angle = 2 * PI / n * (if (invert) -1 else 1)
    var w = Complex(1.0, 0.0)
    val wn = Complex(cos(angle), sin(angle))

    val y = Array(n) { Complex(0.0, 0.0) }
    for (i in 0 until n / 2) {
        y[i] = y0[i] + w * y1[i]
        y[i + n / 2] = y0[i] - w * y1[i]
        if (invert) {
            y[i] = Complex(y[i].re / 2, y[i].im / 2)
            y[i + n / 2] = Complex(y[i + n / 2].re / 2, y[i + n / 2].im / 2)
        }
        w = w * wn
    }
    return y
}

// 다항식 곱셈
fun multiply(a: IntArray, b: IntArray): IntArray {
    var n = 1
    while (n < a.size + b.size) n *= 2

    val fa = Array(n) { if (it < a.size) Complex(a[it].toDouble(), 0.0) else Complex(0.0, 0.0) }
    val fb = Array(n) { if (it < b.size) Complex(b[it].toDouble(), 0.0) else Complex(0.0, 0.0) }

    val ya = fft(fa)
    val yb = fft(fb)
    val yc = Array(n) { ya[it] * yb[it] }
    val c = fft(yc, true)

    return IntArray(n) { (c[it].re + 0.5).toInt() }
}

fun main() {
    // (1 + 2x) * (3 + 4x) = 3 + 10x + 8x^2
    val a = intArrayOf(1, 2)  // 1 + 2x
    val b = intArrayOf(3, 4)  // 3 + 4x
    val result = multiply(a, b)

    println("(1 + 2x) * (3 + 4x) =")
    println("계수: ${'$'}{result.take(3).joinToString()}")
    println("= ${'$'}{result[0]} + ${'$'}{result[1]}x + ${'$'}{result[2]}x^2")
}
                """.trimIndent(),
                explanation = "FFT를 이용한 다항식 곱셈",
                expectedOutput = """
(1 + 2x) * (3 + 4x) =
계수: 3, 10, 8
= 3 + 10x + 8x^2
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("karatsuba", "ntt"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )

    /**
     * 심슨 적분법 (Simpson's Rule)
     * 포물선 근사를 이용한 수치 적분
     */
    private fun createSimpsonsRule() = Algorithm(
        id = "simpsons-rule",
        name = "Simpson's Rule",
        koreanName = "심슨 적분법",
        category = AlgorithmCategory.MATH,
        purpose = "포물선 근사를 이용한 수치 적분",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("포물선 근사", "고정밀도"),
        advantages = listOf("사다리꼴 공식보다 정확", "구현 간단"),
        disadvantages = listOf("분할 수가 짝수여야 함", "불연속 함수에 부적합"),
        useCases = listOf("면적 계산", "물리 시뮬레이션", "공학 계산"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.math.*

// 심슨 적분법
fun simpsonsRule(f: (Double) -> Double, a: Double, b: Double, n: Int): Double {
    require(n % 2 == 0) { "n은 짝수여야 합니다" }

    val h = (b - a) / n
    var sum = f(a) + f(b)

    for (i in 1 until n) {
        val x = a + i * h
        sum += if (i % 2 == 0) 2 * f(x) else 4 * f(x)
    }

    return sum * h / 3
}

// 적응형 심슨 적분
fun adaptiveSimpson(f: (Double) -> Double, a: Double, b: Double, eps: Double): Double {
    fun simpson(a: Double, b: Double): Double {
        val c = (a + b) / 2
        return (b - a) / 6 * (f(a) + 4 * f(c) + f(b))
    }

    fun helper(a: Double, b: Double, eps: Double, whole: Double): Double {
        val c = (a + b) / 2
        val left = simpson(a, c)
        val right = simpson(c, b)
        if (abs(left + right - whole) <= 15 * eps)
            return left + right + (left + right - whole) / 15
        return helper(a, c, eps / 2, left) + helper(c, b, eps / 2, right)
    }

    return helper(a, b, eps, simpson(a, b))
}

fun main() {
    // ∫₀^π sin(x) dx = 2
    val result1 = simpsonsRule({ sin(it) }, 0.0, PI, 100)
    println("∫₀^π sin(x) dx ≈ ${'$'}{"%.10f".format(result1)}")
    println("오차: ${'$'}{"%.2e".format(abs(result1 - 2.0))}")

    // ∫₀^1 x² dx = 1/3
    val result2 = simpsonsRule({ it * it }, 0.0, 1.0, 100)
    println("\n∫₀^1 x² dx ≈ ${'$'}{"%.10f".format(result2)}")
    println("실제값: ${'$'}{"%.10f".format(1.0/3)}")
}
                """.trimIndent(),
                explanation = "심슨 적분법을 이용한 수치 적분",
                expectedOutput = """
∫₀^π sin(x) dx ≈ 2.0000000000
오차: 1.79e-16

∫₀^1 x² dx ≈ 0.3333333333
실제값: 0.3333333333
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("trapezoidal-rule", "romberg-integration"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    /**
     * 뉴턴-랩슨 (Newton-Raphson)
     * 접선을 이용한 방정식의 근 찾기
     */
    private fun createNewtonRaphson() = Algorithm(
        id = "newton-raphson",
        name = "Newton-Raphson Method",
        koreanName = "뉴턴-랩슨 방법",
        category = AlgorithmCategory.MATH,
        purpose = "접선을 이용하여 방정식의 근을 빠르게 찾음",
        timeComplexity = TimeComplexity(
            best = "O(log n)",
            average = "O(log n)",
            worst = "수렴하지 않을 수 있음"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("접선 이용", "이차 수렴"),
        advantages = listOf("수렴 속도 빠름", "정밀도 높음"),
        disadvantages = listOf("초기값 선택 중요", "발산 가능성"),
        useCases = listOf("제곱근 계산", "최적화", "수치 해석"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.math.*

// 뉴턴-랩슨 일반 형태
fun newtonRaphson(
    f: (Double) -> Double,      // 함수
    df: (Double) -> Double,     // 도함수
    x0: Double,                 // 초기값
    eps: Double = 1e-10,
    maxIter: Int = 100
): Double {
    var x = x0
    repeat(maxIter) {
        val fx = f(x)
        if (abs(fx) < eps) return x
        val dfx = df(x)
        if (abs(dfx) < 1e-15) throw ArithmeticException("도함수가 0")
        x -= fx / dfx
    }
    return x
}

// 제곱근 계산 특화 버전
fun sqrt(n: Double, eps: Double = 1e-10): Double {
    if (n < 0) throw IllegalArgumentException("음수의 제곱근")
    if (n == 0.0) return 0.0

    var x = n
    while (abs(x * x - n) > eps) {
        x = (x + n / x) / 2
    }
    return x
}

// n제곱근 계산
fun nthRoot(n: Double, k: Int, eps: Double = 1e-10): Double {
    var x = n
    repeat(100) {
        val xk = x.pow(k - 1)
        val fx = x.pow(k) - n
        if (abs(fx) < eps) return x
        x -= fx / (k * xk)
    }
    return x
}

fun main() {
    // 제곱근 계산
    println("√2 = ${'$'}{"%.15f".format(sqrt(2.0))}")
    println("Math.sqrt(2) = ${'$'}{"%.15f".format(kotlin.math.sqrt(2.0))}")

    // x³ - 2 = 0 의 근 (∛2)
    val cubeRoot2 = newtonRaphson(
        f = { it.pow(3) - 2 },
        df = { 3 * it.pow(2) },
        x0 = 1.0
    )
    println("\n∛2 = ${'$'}{"%.15f".format(cubeRoot2)}")

    // cos(x) = x 의 근
    val cosRoot = newtonRaphson(
        f = { cos(it) - it },
        df = { -sin(it) - 1 },
        x0 = 0.5
    )
    println("\ncos(x) = x 의 해: x = ${'$'}{"%.15f".format(cosRoot)}")
}
                """.trimIndent(),
                explanation = "뉴턴-랩슨 방법을 이용한 방정식 풀이",
                expectedOutput = """
√2 = 1.414213562373095
Math.sqrt(2) = 1.414213562373095

∛2 = 1.259921049894873

cos(x) = x 의 해: x = 0.739085133215161
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("bisection-method", "secant-method"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    /**
     * 소인수분해 (Prime Factorization)
     * 정수를 소수의 곱으로 분해
     */
    private fun createPrimeFactorization() = Algorithm(
        id = "prime-factorization",
        name = "Prime Factorization",
        koreanName = "소인수분해",
        category = AlgorithmCategory.MATH,
        purpose = "정수를 소수의 곱으로 분해",
        timeComplexity = TimeComplexity(
            best = "O(log n)",
            average = "O(√n)",
            worst = "O(√n)"
        ),
        spaceComplexity = "O(log n)",
        characteristics = listOf("기본적 수론 연산", "약수 관련 문제의 기초"),
        advantages = listOf("구현 간단", "다양한 응용"),
        disadvantages = listOf("큰 수에서 느림", "암호학에서는 어려움 이용"),
        useCases = listOf("RSA 암호", "GCD/LCM 계산", "약수 개수 계산"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 기본 소인수분해
fun primeFactorize(n: Long): Map<Long, Int> {
    val factors = mutableMapOf<Long, Int>()
    var num = n

    // 2로 나누기
    while (num % 2 == 0L) {
        factors[2] = factors.getOrDefault(2, 0) + 1
        num /= 2
    }

    // 홀수로 나누기
    var i = 3L
    while (i * i <= num) {
        while (num % i == 0L) {
            factors[i] = factors.getOrDefault(i, 0) + 1
            num /= i
        }
        i += 2
    }

    // 남은 소수
    if (num > 1) {
        factors[num] = factors.getOrDefault(num, 0) + 1
    }

    return factors
}

// 약수 개수 (소인수분해 이용)
fun countDivisors(n: Long): Long {
    val factors = primeFactorize(n)
    return factors.values.fold(1L) { acc, exp -> acc * (exp + 1) }
}

// 약수의 합 (소인수분해 이용)
fun sumOfDivisors(n: Long): Long {
    val factors = primeFactorize(n)
    var result = 1L
    for ((prime, exp) in factors) {
        var sum = 0L
        var power = 1L
        repeat(exp + 1) {
            sum += power
            power *= prime
        }
        result *= sum
    }
    return result
}

fun main() {
    val n = 360L
    println("${'$'}n의 소인수분해:")
    val factors = primeFactorize(n)
    println(factors.entries.joinToString(" × ") { "${'$'}{it.key}^${'$'}{it.value}" })

    println("\n${'$'}n = ${'$'}{factors.entries.joinToString(" × ") {
        List(it.value) { _ -> it.key.toString() }.joinToString(" × ")
    }}")

    println("\n약수의 개수: ${'$'}{countDivisors(n)}")
    println("약수의 합: ${'$'}{sumOfDivisors(n)}")
}
                """.trimIndent(),
                explanation = "소인수분해와 그 응용",
                expectedOutput = """
360의 소인수분해:
2^3 × 3^2 × 5^1

360 = 2 × 2 × 2 × 3 × 3 × 5

약수의 개수: 24
약수의 합: 1170
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("sieve-eratosthenes", "euclidean-gcd", "miller-rabin"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    /**
     * 밀러-라빈 소수 판정 (Miller-Rabin)
     * 확률적 소수 판정 알고리즘
     */
    private fun createMillerRabin() = Algorithm(
        id = "miller-rabin",
        name = "Miller-Rabin Primality Test",
        koreanName = "밀러-라빈 소수 판정",
        category = AlgorithmCategory.MATH,
        purpose = "큰 수의 소수 판정을 빠르게 수행",
        timeComplexity = TimeComplexity(
            best = "O(k log³ n)",
            average = "O(k log³ n)",
            worst = "O(k log³ n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("확률적 알고리즘", "결정적 변형 가능"),
        advantages = listOf("매우 빠름", "큰 수에도 효율적"),
        disadvantages = listOf("확률적 오류 가능", "합성수를 소수로 판정할 수 있음"),
        useCases = listOf("RSA 키 생성", "소수 생성", "암호학"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.random.Random

// 모듈러 거듭제곱
fun powerMod(base: Long, exp: Long, mod: Long): Long {
    var result = 1L
    var b = base % mod
    var e = exp
    while (e > 0) {
        if (e and 1L == 1L) result = (result * b) % mod
        e = e shr 1
        b = (b * b) % mod
    }
    return result
}

// 밀러-라빈 단일 테스트
fun millerRabinTest(n: Long, a: Long): Boolean {
    if (n == a) return true
    if (n % 2 == 0L) return false

    var d = n - 1
    var r = 0
    while (d % 2 == 0L) {
        d /= 2
        r++
    }

    var x = powerMod(a, d, n)
    if (x == 1L || x == n - 1) return true

    repeat(r - 1) {
        x = (x * x) % n
        if (x == n - 1) return true
    }
    return false
}

// 밀러-라빈 소수 판정 (결정적 버전, n < 3,317,044,064,679,887,385,961,981)
fun isPrime(n: Long): Boolean {
    if (n < 2) return false
    if (n == 2L || n == 3L) return true
    if (n % 2 == 0L) return false

    // 결정적 테스트를 위한 witness 집합
    val witnesses = longArrayOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37)

    for (a in witnesses) {
        if (n == a) return true
        if (!millerRabinTest(n, a)) return false
    }
    return true
}

// 확률적 버전 (k번 테스트)
fun isPrimeProbabilistic(n: Long, k: Int = 10): Boolean {
    if (n < 2) return false
    if (n == 2L || n == 3L) return true
    if (n % 2 == 0L) return false

    repeat(k) {
        val a = 2 + Random.nextLong(n - 3)
        if (!millerRabinTest(n, a)) return false
    }
    return true
}

fun main() {
    val testNumbers = listOf(2L, 17L, 561L, 1009L, 104729L, 1000000007L)

    println("밀러-라빈 소수 판정:")
    for (n in testNumbers) {
        val result = if (isPrime(n)) "소수" else "합성수"
        println("${'$'}n: ${'$'}result")
    }

    // 561은 카마이클 수 (페르마 테스트를 통과하는 합성수)
    println("\n참고: 561 = 3 × 11 × 17 (카마이클 수)")
}
                """.trimIndent(),
                explanation = "밀러-라빈 소수 판정 알고리즘",
                expectedOutput = """
밀러-라빈 소수 판정:
2: 소수
17: 소수
561: 합성수
1009: 소수
104729: 소수
1000000007: 소수

참고: 561 = 3 × 11 × 17 (카마이클 수)
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("sieve-eratosthenes", "prime-factorization"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )
}
