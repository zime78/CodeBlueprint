package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 탐욕 알고리즘 데이터 (5개)
 *
 * - Huffman Coding
 * - Activity Selection
 * - Fractional Knapsack
 * - Job Sequencing
 * - Optimal Merge Pattern
 */
internal object GreedyAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createHuffman(),
        createActivitySelection(),
        createFractionalKnapsack(),
        createJobSequencing(),
        createOptimalMerge()
    )

    private fun createHuffman() = Algorithm(
        id = "huffman",
        name = "Huffman Coding",
        koreanName = "허프만 코딩",
        category = AlgorithmCategory.GREEDY,
        purpose = "가변 길이 접두사 코드로 데이터 압축",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("빈도 기반 최적 코딩", "접두사 자유 코드"),
        advantages = listOf("최적 압축률", "무손실 압축"),
        disadvantages = listOf("코드 테이블 저장 필요"),
        useCases = listOf("파일 압축 (ZIP, GZIP)", "JPEG 이미지 압축", "데이터 전송"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class HuffmanNode(
    val char: Char?,
    val freq: Int,
    val left: HuffmanNode? = null,
    val right: HuffmanNode? = null
) : Comparable<HuffmanNode> {
    override fun compareTo(other: HuffmanNode) = freq - other.freq
}

fun buildHuffmanTree(freq: Map<Char, Int>): HuffmanNode? {
    val pq = java.util.PriorityQueue<HuffmanNode>()
    freq.forEach { (c, f) -> pq.add(HuffmanNode(c, f)) }

    while (pq.size > 1) {
        val left = pq.poll()
        val right = pq.poll()
        pq.add(HuffmanNode(null, left.freq + right.freq, left, right))
    }
    return pq.poll()
}

fun getCodes(root: HuffmanNode?, code: String = ""): Map<Char, String> {
    val codes = mutableMapOf<Char, String>()
    fun traverse(node: HuffmanNode?, c: String) {
        node ?: return
        if (node.char != null) codes[node.char] = c
        traverse(node.left, c + "0")
        traverse(node.right, c + "1")
    }
    traverse(root, code)
    return codes
}

fun main() {
    val freq = mapOf('a' to 5, 'b' to 9, 'c' to 12, 'd' to 13, 'e' to 16, 'f' to 45)
    val root = buildHuffmanTree(freq)
    val codes = getCodes(root)
    println("허프만 코드:")
    codes.toSortedMap().forEach { (char, code) ->
        println("${'$'}char: ${'$'}code")
    }
}
                """.trimIndent(),
                explanation = "탐욕 알고리즘을 사용한 허프만 트리 구성",
                expectedOutput = """
허프만 코드:
a: 1100
b: 1101
c: 100
d: 101
e: 111
f: 0
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("shannon-fano", "arithmetic-coding"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    /**
     * 활동 선택 알고리즘 생성
     * 겹치지 않는 최대 활동 수 선택
     */
    private fun createActivitySelection() = Algorithm(
        id = "activity-selection",
        name = "Activity Selection",
        koreanName = "활동 선택",
        category = AlgorithmCategory.GREEDY,
        purpose = "겹치지 않는 최대 활동 수를 선택하는 스케줄링 문제 해결",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("종료 시간 기준 정렬", "탐욕적 선택으로 최적해 보장"),
        advantages = listOf("간단하고 효율적인 알고리즘", "최적해 보장"),
        disadvantages = listOf("특정 조건(겹치지 않는 활동)에서만 최적"),
        useCases = listOf("회의실 배정", "작업 스케줄링", "CPU 스케줄링"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Activity(val start: Int, val end: Int, val name: String)

/**
 * 활동 선택 알고리즘
 * 종료 시간 기준 정렬 후 탐욕적으로 선택
 */
fun selectActivities(activities: List<Activity>): List<Activity> {
    if (activities.isEmpty()) return emptyList()

    // 종료 시간 기준 정렬
    val sorted = activities.sortedBy { it.end }
    val selected = mutableListOf<Activity>()

    selected.add(sorted[0])
    var lastEnd = sorted[0].end

    for (i in 1 until sorted.size) {
        // 현재 활동의 시작 시간이 마지막 선택된 활동의 종료 시간 이후인 경우
        if (sorted[i].start >= lastEnd) {
            selected.add(sorted[i])
            lastEnd = sorted[i].end
        }
    }

    return selected
}

fun main() {
    val activities = listOf(
        Activity(1, 4, "A"),
        Activity(3, 5, "B"),
        Activity(0, 6, "C"),
        Activity(5, 7, "D"),
        Activity(3, 9, "E"),
        Activity(5, 9, "F"),
        Activity(6, 10, "G"),
        Activity(8, 11, "H"),
        Activity(8, 12, "I"),
        Activity(2, 14, "J"),
        Activity(12, 16, "K")
    )

    val selected = selectActivities(activities)
    println("선택된 활동: ${'$'}{selected.map { it.name }}")
    println("총 ${'$'}{selected.size}개 활동 선택")
    selected.forEach { println("  ${'$'}{it.name}: ${'$'}{it.start} ~ ${'$'}{it.end}") }
}
                """.trimIndent(),
                explanation = "종료 시간 기준 정렬 후 겹치지 않는 활동을 탐욕적으로 선택",
                expectedOutput = """
선택된 활동: [A, D, H, K]
총 4개 활동 선택
  A: 1 ~ 4
  D: 5 ~ 7
  H: 8 ~ 11
  K: 12 ~ 16
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("job-sequencing", "interval-scheduling"),
        difficulty = Difficulty.LOW,
        frequency = 4
    )

    /**
     * 분할 배낭 알고리즘 생성
     * 물건을 분할하여 배낭에 담아 최대 가치 획득
     */
    private fun createFractionalKnapsack() = Algorithm(
        id = "fractional-knapsack",
        name = "Fractional Knapsack",
        koreanName = "분할 배낭",
        category = AlgorithmCategory.GREEDY,
        purpose = "물건을 분할하여 배낭에 담아 최대 가치를 획득하는 문제 해결",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("물건 분할 가능", "가치/무게 비율로 정렬"),
        advantages = listOf("간단한 탐욕 해법", "최적해 보장"),
        disadvantages = listOf("0/1 배낭 문제에는 적용 불가", "물건 분할이 가능한 경우에만 사용"),
        useCases = listOf("자원 배분 최적화", "투자 포트폴리오 구성", "화물 적재"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Item(val weight: Double, val value: Double) {
    val ratio: Double get() = value / weight
}

/**
 * 분할 배낭 알고리즘
 * 가치/무게 비율이 높은 순으로 물건을 선택
 */
fun fractionalKnapsack(items: List<Item>, capacity: Double): Double {
    if (items.isEmpty() || capacity <= 0) return 0.0

    // 가치/무게 비율 기준 내림차순 정렬
    val sorted = items.sortedByDescending { it.ratio }

    var totalValue = 0.0
    var remainingCapacity = capacity

    for (item in sorted) {
        if (remainingCapacity <= 0) break

        if (item.weight <= remainingCapacity) {
            // 물건 전체를 담을 수 있음
            totalValue += item.value
            remainingCapacity -= item.weight
            println("물건 전체 추가: 무게=${'$'}{item.weight}, 가치=${'$'}{item.value}")
        } else {
            // 물건의 일부만 담음
            val fraction = remainingCapacity / item.weight
            totalValue += item.value * fraction
            println("물건 일부 추가: 비율=${'$'}{"%.2f".format(fraction)}, 가치=${'$'}{"%.2f".format(item.value * fraction)}")
            remainingCapacity = 0.0
        }
    }

    return totalValue
}

fun main() {
    val items = listOf(
        Item(10.0, 60.0),  // 비율: 6.0
        Item(20.0, 100.0), // 비율: 5.0
        Item(30.0, 120.0)  // 비율: 4.0
    )
    val capacity = 50.0

    println("배낭 용량: ${'$'}capacity")
    println("물건 목록:")
    items.forEachIndexed { i, item ->
        println("  ${'$'}{i+1}. 무게=${'$'}{item.weight}, 가치=${'$'}{item.value}, 비율=${'$'}{item.ratio}")
    }
    println()

    val maxValue = fractionalKnapsack(items, capacity)
    println("\n최대 가치: ${'$'}{"%.2f".format(maxValue)}")
}
                """.trimIndent(),
                explanation = "가치/무게 비율로 정렬 후 높은 비율부터 탐욕적으로 선택",
                expectedOutput = """
배낭 용량: 50.0
물건 목록:
  1. 무게=10.0, 가치=60.0, 비율=6.0
  2. 무게=20.0, 가치=100.0, 비율=5.0
  3. 무게=30.0, 가치=120.0, 비율=4.0

물건 전체 추가: 무게=10.0, 가치=60.0
물건 전체 추가: 무게=20.0, 가치=100.0
물건 일부 추가: 비율=0.67, 가치=80.00

최대 가치: 240.00
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("knapsack-01", "bounded-knapsack"),
        difficulty = Difficulty.LOW,
        frequency = 3
    )

    /**
     * 작업 순서화 알고리즘 생성
     * 마감일 내에 최대 이익을 얻도록 작업 순서 결정
     */
    private fun createJobSequencing() = Algorithm(
        id = "job-sequencing",
        name = "Job Sequencing with Deadlines",
        koreanName = "작업 순서화",
        category = AlgorithmCategory.GREEDY,
        purpose = "마감일 내에 최대 이익을 얻도록 작업 순서를 결정하는 문제 해결",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n²)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("이익 기준 내림차순 정렬", "가능한 가장 늦은 슬롯에 배치"),
        advantages = listOf("최대 이익 보장", "마감일 고려한 스케줄링"),
        disadvantages = listOf("단순 구현 시 O(n²) 시간 복잡도", "Union-Find 사용 시 복잡도 향상 가능"),
        useCases = listOf("작업 스케줄링", "마감일 관리", "자원 할당 최적화"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Job(val id: String, val deadline: Int, val profit: Int)

/**
 * 작업 순서화 알고리즘
 * 이익이 높은 작업부터 가능한 가장 늦은 슬롯에 배치
 */
fun jobSequencing(jobs: List<Job>): Pair<List<Job>, Int> {
    if (jobs.isEmpty()) return Pair(emptyList(), 0)

    // 이익 기준 내림차순 정렬
    val sorted = jobs.sortedByDescending { it.profit }

    // 최대 마감일 찾기
    val maxDeadline = jobs.maxOf { it.deadline }

    // 슬롯 배열 (-1: 빈 슬롯)
    val slots = IntArray(maxDeadline) { -1 }
    val scheduled = mutableListOf<Job>()
    var totalProfit = 0

    for (job in sorted) {
        // 마감일 이전의 가장 늦은 빈 슬롯 찾기
        for (slot in (job.deadline - 1) downTo 0) {
            if (slots[slot] == -1) {
                slots[slot] = scheduled.size
                scheduled.add(job)
                totalProfit += job.profit
                println("작업 ${'$'}{job.id} -> 슬롯 ${'$'}{slot + 1} (이익: ${'$'}{job.profit})")
                break
            }
        }
    }

    return Pair(scheduled, totalProfit)
}

fun main() {
    val jobs = listOf(
        Job("A", 2, 100),
        Job("B", 1, 19),
        Job("C", 2, 27),
        Job("D", 1, 25),
        Job("E", 3, 15)
    )

    println("작업 목록:")
    jobs.forEach { println("  ${'$'}{it.id}: 마감일=${'$'}{it.deadline}, 이익=${'$'}{it.profit}") }
    println()

    val (scheduled, profit) = jobSequencing(jobs)
    println("\n스케줄된 작업: ${'$'}{scheduled.map { it.id }}")
    println("총 이익: ${'$'}profit")
}
                """.trimIndent(),
                explanation = "이익 기준 정렬 후 마감일 내 가장 늦은 슬롯에 탐욕적 배치",
                expectedOutput = """
작업 목록:
  A: 마감일=2, 이익=100
  B: 마감일=1, 이익=19
  C: 마감일=2, 이익=27
  D: 마감일=1, 이익=25
  E: 마감일=3, 이익=15

작업 A -> 슬롯 2 (이익: 100)
작업 C -> 슬롯 1 (이익: 27)
작업 E -> 슬롯 3 (이익: 15)

스케줄된 작업: [A, C, E]
총 이익: 142
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("activity-selection", "task-scheduling"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    /**
     * 최적 병합 패턴 알고리즘 생성
     * 여러 파일을 병합할 때 총 비용을 최소화
     */
    private fun createOptimalMerge() = Algorithm(
        id = "optimal-merge",
        name = "Optimal Merge Pattern",
        koreanName = "최적 병합 패턴",
        category = AlgorithmCategory.GREEDY,
        purpose = "여러 파일을 병합할 때 총 비용을 최소화하는 순서 결정",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("가장 작은 두 파일 먼저 병합", "최소 힙 활용"),
        advantages = listOf("최소 병합 비용 보장", "허프만 코딩과 유사한 원리"),
        disadvantages = listOf("우선순위 큐 구현 필요"),
        useCases = listOf("파일 병합 최적화", "외부 정렬", "정렬된 리스트 병합"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import java.util.PriorityQueue

/**
 * 최적 병합 패턴 알고리즘
 * 가장 작은 두 파일을 반복적으로 병합하여 최소 비용 달성
 */
fun optimalMerge(fileSizes: List<Int>): Int {
    if (fileSizes.isEmpty()) return 0
    if (fileSizes.size == 1) return 0  // 병합 필요 없음

    // 최소 힙 사용
    val minHeap = PriorityQueue<Int>()
    fileSizes.forEach { minHeap.add(it) }

    var totalCost = 0
    var step = 1

    while (minHeap.size > 1) {
        // 가장 작은 두 파일 추출
        val first = minHeap.poll()
        val second = minHeap.poll()

        // 병합 비용 = 두 파일 크기의 합
        val mergeCost = first + second
        totalCost += mergeCost

        println("단계 ${'$'}step: ${'$'}first + ${'$'}second = ${'$'}mergeCost (누적: ${'$'}totalCost)")
        step++

        // 병합된 파일을 다시 힙에 추가
        minHeap.add(mergeCost)
    }

    return totalCost
}

fun main() {
    val fileSizes = listOf(2, 3, 4, 5, 6)

    println("파일 크기: ${'$'}fileSizes")
    println("총 파일 수: ${'$'}{fileSizes.size}")
    println()

    val totalCost = optimalMerge(fileSizes)
    println("\n최소 총 병합 비용: ${'$'}totalCost")
}
                """.trimIndent(),
                explanation = "최소 힙을 사용해 가장 작은 두 파일을 반복 병합",
                expectedOutput = """
파일 크기: [2, 3, 4, 5, 6]
총 파일 수: 5

단계 1: 2 + 3 = 5 (누적: 5)
단계 2: 4 + 5 = 9 (누적: 14)
단계 3: 5 + 6 = 11 (누적: 25)
단계 4: 9 + 11 = 20 (누적: 45)

최소 총 병합 비용: 45
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("huffman", "k-way-merge"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )
}
