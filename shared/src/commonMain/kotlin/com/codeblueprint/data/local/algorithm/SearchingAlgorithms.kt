package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 탐색 알고리즘 데이터 (8개)
 *
 * Linear, Binary, Jump, Interpolation, Exponential,
 * Fibonacci, Ternary, Hash Table Search
 */
internal object SearchingAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createLinearSearch(),
        createBinarySearch(),
        createJumpSearch(),
        createInterpolationSearch(),
        createExponentialSearch(),
        createFibonacciSearch(),
        createTernarySearch(),
        createHashTableSearch()
    )

    private fun createLinearSearch() = Algorithm(
        id = "linear-search",
        name = "Linear Search",
        koreanName = "선형 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "처음부터 끝까지 순차적으로 검색",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(n)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("가장 단순한 탐색", "정렬 불필요", "모든 자료구조에 적용 가능"),
        advantages = listOf("구현이 매우 간단", "전처리 불필요"),
        disadvantages = listOf("대규모 데이터에서 비효율적"),
        useCases = listOf("소규모 데이터", "정렬되지 않은 데이터"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun linearSearch(arr: IntArray, target: Int): Int {
    for (i in arr.indices) {
        if (arr[i] == target) return i
    }
    return -1
}

fun main() {
    val arr = intArrayOf(10, 23, 45, 70, 11, 15)
    val target = 70
    val result = linearSearch(arr, target)
    println("배열: ${'$'}{arr.contentToString()}")
    println("찾는 값: ${'$'}target")
    println("결과: ${'$'}{if (result != -1) "인덱스 ${'$'}result" else "찾지 못함"}")
}
                """.trimIndent(),
                explanation = "처음부터 순차적으로 탐색하는 선형 탐색",
                expectedOutput = """
배열: [10, 23, 45, 70, 11, 15]
찾는 값: 70
결과: 인덱스 3
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.LOW,
        frequency = 3
    )

    private fun createBinarySearch() = Algorithm(
        id = "binary-search",
        name = "Binary Search",
        koreanName = "이진 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "정렬된 배열에서 중간값 비교로 탐색 범위 절반씩 축소",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log n)",
            worst = "O(log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("분할 정복", "정렬된 배열 필수", "매우 효율적"),
        advantages = listOf("O(log n) 시간 복잡도", "대규모 데이터에 효율적"),
        disadvantages = listOf("정렬된 배열 필요", "연결 리스트에서 비효율적"),
        useCases = listOf("정렬된 배열 검색", "Lower/Upper Bound 찾기", "이분 탐색 문제"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun binarySearch(arr: IntArray, target: Int): Int {
    var left = 0
    var right = arr.size - 1

    while (left <= right) {
        val mid = left + (right - left) / 2
        when {
            arr[mid] == target -> return mid
            arr[mid] < target -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return -1 // 찾지 못함
}

fun main() {
    val arr = intArrayOf(2, 3, 4, 10, 40)
    val target = 10
    val result = binarySearch(arr, target)
    println("배열: ${'$'}{arr.joinToString()}")
    println("찾는 값: ${'$'}target")
    println("결과: ${'$'}{if (result != -1) "인덱스 ${'$'}result" else "찾지 못함"}")
}
                """.trimIndent(),
                explanation = "중간값과 비교하여 탐색 범위를 절반씩 줄이는 이진 탐색",
                expectedOutput = """
배열: 2, 3, 4, 10, 40
찾는 값: 10
결과: 인덱스 3
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("linear-search", "ternary-search"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createJumpSearch() = Algorithm(
        id = "jump-search",
        name = "Jump Search",
        koreanName = "점프 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "블록 단위로 점프하며 탐색 후 선형 탐색",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(√n)",
            worst = "O(√n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("Linear와 Binary의 중간", "블록 크기는 √n이 최적"),
        advantages = listOf("Binary Search보다 후진 이동 적음", "구현이 비교적 간단"),
        disadvantages = listOf("Binary Search보다 느림"),
        useCases = listOf("후진 이동이 비싼 시스템"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import kotlin.math.sqrt
import kotlin.math.min

fun jumpSearch(arr: IntArray, target: Int): Int {
    val n = arr.size
    val step = sqrt(n.toDouble()).toInt()
    var prev = 0
    var curr = step

    // 블록 단위 점프
    while (curr < n && arr[curr] < target) {
        prev = curr
        curr += step
    }

    // 선형 탐색
    for (i in prev until min(curr + 1, n)) {
        if (arr[i] == target) return i
    }
    return -1
}

fun main() {
    val arr = intArrayOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89)
    val target = 55
    val result = jumpSearch(arr, target)
    println("배열: ${'$'}{arr.contentToString()}")
    println("찾는 값: ${'$'}target, 결과: 인덱스 ${'$'}result")
}
                """.trimIndent(),
                explanation = "블록 단위로 점프 후 선형 탐색하는 점프 탐색",
                expectedOutput = """
배열: [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]
찾는 값: 55, 결과: 인덱스 10
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("binary-search", "linear-search"),
        difficulty = Difficulty.LOW,
        frequency = 1
    )

    private fun createInterpolationSearch() = Algorithm(
        id = "interpolation-search",
        name = "Interpolation Search",
        koreanName = "보간 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "값의 위치를 추정하여 탐색",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log log n)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("균등 분포 데이터에 최적", "Binary Search의 개선"),
        advantages = listOf("균등 분포 시 매우 빠름"),
        disadvantages = listOf("불균등 분포 시 O(n)", "오버플로우 주의 필요"),
        useCases = listOf("균등하게 분포된 정렬 데이터", "사전 검색"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun interpolationSearch(arr: IntArray, target: Int): Int {
    var low = 0
    var high = arr.size - 1

    while (low <= high && target >= arr[low] && target <= arr[high]) {
        if (low == high) {
            return if (arr[low] == target) low else -1
        }

        // 위치 추정
        val pos = low + ((target - arr[low]).toLong() * (high - low) /
                        (arr[high] - arr[low])).toInt()

        when {
            arr[pos] == target -> return pos
            arr[pos] < target -> low = pos + 1
            else -> high = pos - 1
        }
    }
    return -1
}

fun main() {
    val arr = intArrayOf(10, 12, 13, 16, 18, 19, 20, 21, 22, 23, 24, 33, 35, 42, 47)
    val target = 18
    val result = interpolationSearch(arr, target)
    println("찾는 값: ${'$'}target, 결과: 인덱스 ${'$'}result")
}
                """.trimIndent(),
                explanation = "값의 분포를 고려하여 위치를 추정하는 보간 탐색",
                expectedOutput = "찾는 값: 18, 결과: 인덱스 4"
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 1
    )

    private fun createExponentialSearch() = Algorithm(
        id = "exponential-search",
        name = "Exponential Search",
        koreanName = "지수 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "지수적으로 범위를 확장 후 Binary Search",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log n)",
            worst = "O(log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("무한 배열에 적합", "타겟이 앞쪽에 있으면 빠름"),
        advantages = listOf("무한/대규모 배열에 효과적", "타겟이 앞쪽일 때 빠름"),
        disadvantages = listOf("Binary Search보다 약간 느림"),
        useCases = listOf("무한 배열", "크기를 모르는 정렬 배열"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun exponentialSearch(arr: IntArray, target: Int): Int {
    if (arr.isEmpty()) return -1
    if (arr[0] == target) return 0

    var i = 1
    while (i < arr.size && arr[i] <= target) {
        i *= 2
    }

    return binarySearchRange(arr, target, i / 2, minOf(i, arr.size - 1))
}

fun binarySearchRange(arr: IntArray, target: Int, left: Int, right: Int): Int {
    var l = left
    var r = right
    while (l <= r) {
        val mid = l + (r - l) / 2
        when {
            arr[mid] == target -> return mid
            arr[mid] < target -> l = mid + 1
            else -> r = mid - 1
        }
    }
    return -1
}

fun main() {
    val arr = intArrayOf(2, 3, 4, 10, 40, 50, 60, 70, 80, 90, 100)
    val target = 10
    val result = exponentialSearch(arr, target)
    println("찾는 값: ${'$'}target, 결과: 인덱스 ${'$'}result")
}
                """.trimIndent(),
                explanation = "지수적으로 범위를 확장한 후 이진 탐색하는 지수 탐색",
                expectedOutput = "찾는 값: 10, 결과: 인덱스 3"
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createFibonacciSearch() = Algorithm(
        id = "fibonacci-search",
        name = "Fibonacci Search",
        koreanName = "피보나치 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "피보나치 수열을 이용한 분할 탐색",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log n)",
            worst = "O(log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("Binary Search의 변형", "곱셈/나눗셈 대신 덧셈/뺄셈 사용"),
        advantages = listOf("곱셈/나눗셈 없이 구현 가능", "CPU 캐시에 더 친화적"),
        disadvantages = listOf("구현이 복잡", "실제로 Binary Search와 큰 차이 없음"),
        useCases = listOf("하드웨어 최적화", "곱셈이 비싼 시스템"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun fibonacciSearch(arr: IntArray, target: Int): Int {
    val n = arr.size
    var fibM2 = 0  // (m-2)'th Fibonacci
    var fibM1 = 1  // (m-1)'th Fibonacci
    var fibM = fibM2 + fibM1  // m'th Fibonacci

    // fibM >= n 인 가장 작은 피보나치 수 찾기
    while (fibM < n) {
        fibM2 = fibM1
        fibM1 = fibM
        fibM = fibM2 + fibM1
    }

    var offset = -1

    while (fibM > 1) {
        val i = minOf(offset + fibM2, n - 1)

        when {
            arr[i] < target -> {
                fibM = fibM1
                fibM1 = fibM2
                fibM2 = fibM - fibM1
                offset = i
            }
            arr[i] > target -> {
                fibM = fibM2
                fibM1 -= fibM2
                fibM2 = fibM - fibM1
            }
            else -> return i
        }
    }

    if (fibM1 == 1 && offset + 1 < n && arr[offset + 1] == target) {
        return offset + 1
    }

    return -1
}

fun main() {
    val arr = intArrayOf(10, 22, 35, 40, 45, 50, 80, 82, 85, 90, 100)
    val target = 85
    val result = fibonacciSearch(arr, target)
    println("찾는 값: ${'$'}target, 결과: 인덱스 ${'$'}result")
}
                """.trimIndent(),
                explanation = "피보나치 수열을 이용한 분할 탐색",
                expectedOutput = "찾는 값: 85, 결과: 인덱스 8"
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 1
    )

    private fun createTernarySearch() = Algorithm(
        id = "ternary-search",
        name = "Ternary Search",
        koreanName = "삼진 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "배열을 3등분하여 탐색",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(log₃ n)",
            worst = "O(log₃ n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("Binary Search의 변형", "Unimodal 함수의 최대/최소값 찾기에 유용"),
        advantages = listOf("최대/최소값 찾기에 유용", "Unimodal 함수에 적합"),
        disadvantages = listOf("일반 검색은 Binary Search가 더 효율적"),
        useCases = listOf("Unimodal 함수의 극값 찾기", "최적화 문제"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 배열에서 값 검색
fun ternarySearch(arr: IntArray, target: Int): Int {
    var left = 0
    var right = arr.size - 1

    while (left <= right) {
        val mid1 = left + (right - left) / 3
        val mid2 = right - (right - left) / 3

        when {
            arr[mid1] == target -> return mid1
            arr[mid2] == target -> return mid2
            target < arr[mid1] -> right = mid1 - 1
            target > arr[mid2] -> left = mid2 + 1
            else -> {
                left = mid1 + 1
                right = mid2 - 1
            }
        }
    }
    return -1
}

// Unimodal 함수의 최대값 위치 찾기
fun ternarySearchMax(f: (Double) -> Double, left: Double, right: Double): Double {
    var l = left
    var r = right
    val eps = 1e-9

    while (r - l > eps) {
        val m1 = l + (r - l) / 3
        val m2 = r - (r - l) / 3
        if (f(m1) < f(m2)) l = m1 else r = m2
    }
    return (l + r) / 2
}

fun main() {
    val arr = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val target = 5
    val result = ternarySearch(arr, target)
    println("찾는 값: ${'$'}target, 결과: 인덱스 ${'$'}result")
}
                """.trimIndent(),
                explanation = "배열을 3등분하여 탐색하는 삼진 탐색",
                expectedOutput = "찾는 값: 5, 결과: 인덱스 4"
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createHashTableSearch() = Algorithm(
        id = "hash-table-search",
        name = "Hash Table Search",
        koreanName = "해시 테이블 탐색",
        category = AlgorithmCategory.SEARCHING,
        purpose = "해시 함수로 직접 위치 계산",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(1)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("키-값 매핑", "충돌 처리 필요", "상수 시간 접근"),
        advantages = listOf("평균 O(1) 검색", "삽입/삭제도 O(1)"),
        disadvantages = listOf("추가 메모리 필요", "해시 충돌 처리 필요", "정렬 순서 유지 안됨"),
        useCases = listOf("데이터베이스 인덱싱", "캐싱", "중복 검사"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// 간단한 해시 테이블 구현
class SimpleHashTable<K, V>(private val capacity: Int = 16) {
    private val buckets = Array<MutableList<Pair<K, V>>>(capacity) { mutableListOf() }

    private fun hash(key: K): Int = (key.hashCode() and 0x7fffffff) % capacity

    fun put(key: K, value: V) {
        val bucket = buckets[hash(key)]
        val index = bucket.indexOfFirst { it.first == key }
        if (index >= 0) bucket[index] = key to value
        else bucket.add(key to value)
    }

    fun get(key: K): V? {
        return buckets[hash(key)].find { it.first == key }?.second
    }

    fun remove(key: K): Boolean {
        return buckets[hash(key)].removeIf { it.first == key }
    }
}

fun main() {
    val hashTable = SimpleHashTable<String, Int>()
    hashTable.put("apple", 1)
    hashTable.put("banana", 2)
    hashTable.put("cherry", 3)

    println("apple: ${'$'}{hashTable.get("apple")}")
    println("banana: ${'$'}{hashTable.get("banana")}")
    println("grape: ${'$'}{hashTable.get("grape")}")
}
                """.trimIndent(),
                explanation = "해시 함수를 이용한 O(1) 검색",
                expectedOutput = """
apple: 1
banana: 2
grape: null
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("binary-search"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )
}
