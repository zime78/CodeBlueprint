package com.codeblueprint.data.local

import com.codeblueprint.data.mapper.AlgorithmMapper
import com.codeblueprint.data.mapper.CodeExampleMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 알고리즘 초기 데이터 생성기
 *
 * 데이터베이스가 최초 생성될 때 20개의 핵심 알고리즘 데이터를 삽입합니다.
 */
class AlgorithmDataProvider(
    private val database: CodeBlueprintDatabase,
    private val algorithmMapper: AlgorithmMapper,
    private val codeExampleMapper: CodeExampleMapper = CodeExampleMapper()
) {
    private val queries = database.codeBlueprintQueries

    /**
     * 초기 알고리즘 데이터 삽입
     */
    suspend fun initializeIfNeeded() {
        val count = queries.getAlgorithmCount().executeAsOne()
        if (count == 0L) {
            insertInitialAlgorithms()
        }
    }

    private fun insertInitialAlgorithms() {
        val algorithms = createInitialAlgorithms()
        algorithms.forEach { algorithm ->
            // 알고리즘 삽입 (기존 로직 유지)
            val values = algorithmMapper.toEntityValues(algorithm)
            queries.insertAlgorithm(
                id = values.id,
                name = values.name,
                korean_name = values.koreanName,
                category = values.category,
                purpose = values.purpose,
                time_complexity_best = values.timeComplexityBest,
                time_complexity_average = values.timeComplexityAverage,
                time_complexity_worst = values.timeComplexityWorst,
                space_complexity = values.spaceComplexity,
                characteristics = values.characteristics,
                advantages = values.advantages,
                disadvantages = values.disadvantages,
                use_cases = values.useCases,
                code_examples = values.codeExamples,
                related_algorithm_ids = values.relatedAlgorithmIds,
                difficulty = values.difficulty,
                frequency = values.frequency
            )

            // 코드 예제를 새 테이블에 삽입
            algorithm.codeExamples.forEachIndexed { index, codeExample ->
                val exampleValues = codeExampleMapper.toEntityValues(
                    domain = codeExample,
                    patternId = null,
                    algorithmId = algorithm.id,
                    index = index
                )
                queries.insertCodeExample(
                    id = exampleValues.id,
                    pattern_id = exampleValues.patternId,
                    algorithm_id = exampleValues.algorithmId,
                    language = exampleValues.language,
                    code = exampleValues.code,
                    explanation = exampleValues.explanation,
                    sample_input = exampleValues.sampleInput,
                    expected_output = exampleValues.expectedOutput,
                    display_order = exampleValues.displayOrder,
                    created_at = exampleValues.createdAt,
                    updated_at = exampleValues.updatedAt
                )
            }
        }
    }

    private fun createInitialAlgorithms(): List<Algorithm> {
        return listOf(
            // 정렬 알고리즘 (4개)
            createBubbleSort(),
            createQuickSort(),
            createMergeSort(),
            createHeapSort(),
            // 탐색 알고리즘 (1개)
            createBinarySearch(),
            // 그래프 알고리즘 (6개)
            createBFS(),
            createDFS(),
            createDijkstra(),
            createBellmanFord(),
            createAStar(),
            createFloydWarshall(),
            // 동적 프로그래밍 (2개)
            createLCS(),
            createKnapsack(),
            // 문자열 알고리즘 (2개)
            createKMP(),
            createTrie(),
            // 수학 알고리즘 (2개)
            createEuclideanGCD(),
            createSieveOfEratosthenes(),
            // 백트래킹 (1개)
            createNQueens(),
            // 탐욕 알고리즘 (1개)
            createHuffman()
        )
    }

    // ==================== 정렬 알고리즘 ====================

    private fun createBubbleSort() = Algorithm(
        id = "bubble-sort",
        name = "Bubble Sort",
        koreanName = "버블 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "인접 요소를 비교하며 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n²)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("안정 정렬", "제자리 정렬", "단순 구현"),
        advantages = listOf("구현이 매우 간단", "추가 메모리 불필요", "안정 정렬"),
        disadvantages = listOf("매우 느림", "대규모 데이터에 부적합"),
        useCases = listOf("교육용", "거의 정렬된 데이터", "소규모 데이터"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun bubbleSort(arr: IntArray) {
    val n = arr.size
    for (i in 0 until n - 1) {
        var swapped = false
        for (j in 0 until n - i - 1) {
            if (arr[j] > arr[j + 1]) {
                // swap
                val temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
                swapped = true
            }
        }
        // 교환이 없으면 이미 정렬됨
        if (!swapped) break
    }
}

fun main() {
    val arr = intArrayOf(64, 34, 25, 12, 22, 11, 90)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    bubbleSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "인접 요소를 비교하여 교환하는 버블 정렬",
                expectedOutput = """
정렬 전: [64, 34, 25, 12, 22, 11, 90]
정렬 후: [11, 12, 22, 25, 34, 64, 90]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("selection-sort", "insertion-sort"),
        difficulty = Difficulty.LOW,
        frequency = 2
    )

    private fun createQuickSort() = Algorithm(
        id = "quick-sort",
        name = "Quick Sort",
        koreanName = "퀵 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "피벗을 기준으로 분할 정복 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(log n)",
        characteristics = listOf("분할 정복", "제자리 정렬", "불안정 정렬"),
        advantages = listOf("평균적으로 가장 빠름", "캐시 효율적", "메모리 효율적"),
        disadvantages = listOf("최악의 경우 O(n²)", "불안정 정렬"),
        useCases = listOf("범용 정렬", "대규모 데이터", "시스템 정렬 라이브러리"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun quickSort(arr: IntArray, low: Int, high: Int) {
    if (low < high) {
        val pi = partition(arr, low, high)
        quickSort(arr, low, pi - 1)
        quickSort(arr, pi + 1, high)
    }
}

fun partition(arr: IntArray, low: Int, high: Int): Int {
    val pivot = arr[high]
    var i = low - 1
    for (j in low until high) {
        if (arr[j] < pivot) {
            i++
            val temp = arr[i]
            arr[i] = arr[j]
            arr[j] = temp
        }
    }
    val temp = arr[i + 1]
    arr[i + 1] = arr[high]
    arr[high] = temp
    return i + 1
}

fun main() {
    val arr = intArrayOf(10, 7, 8, 9, 1, 5)
    println("정렬 전: ${'$'}{arr.joinToString()}")
    quickSort(arr, 0, arr.size - 1)
    println("정렬 후: ${'$'}{arr.joinToString()}")
}
                """.trimIndent(),
                explanation = "피벗을 기준으로 분할하여 정렬하는 퀵 정렬",
                expectedOutput = """
정렬 전: 10, 7, 8, 9, 1, 5
정렬 후: 1, 5, 7, 8, 9, 10
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("merge-sort", "heap-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createMergeSort() = Algorithm(
        id = "merge-sort",
        name = "Merge Sort",
        koreanName = "병합 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "배열을 분할하고 병합하여 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("분할 정복", "안정 정렬", "일관된 성능"),
        advantages = listOf("항상 O(n log n) 보장", "안정 정렬", "연결 리스트에 효율적"),
        disadvantages = listOf("추가 메모리 O(n) 필요"),
        useCases = listOf("안정 정렬 필요 시", "연결 리스트 정렬", "외부 정렬"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun mergeSort(arr: IntArray, l: Int, r: Int) {
    if (l < r) {
        val m = l + (r - l) / 2
        mergeSort(arr, l, m)
        mergeSort(arr, m + 1, r)
        merge(arr, l, m, r)
    }
}

fun merge(arr: IntArray, l: Int, m: Int, r: Int) {
    val left = arr.copyOfRange(l, m + 1)
    val right = arr.copyOfRange(m + 1, r + 1)
    var i = 0; var j = 0; var k = l
    while (i < left.size && j < right.size) {
        arr[k++] = if (left[i] <= right[j]) left[i++] else right[j++]
    }
    while (i < left.size) arr[k++] = left[i++]
    while (j < right.size) arr[k++] = right[j++]
}

fun main() {
    val arr = intArrayOf(38, 27, 43, 3, 9, 82, 10)
    println("정렬 전: ${'$'}{arr.joinToString()}")
    mergeSort(arr, 0, arr.size - 1)
    println("정렬 후: ${'$'}{arr.joinToString()}")
}
                """.trimIndent(),
                explanation = "분할 후 병합하여 정렬하는 병합 정렬",
                expectedOutput = """
정렬 전: 38, 27, 43, 3, 9, 82, 10
정렬 후: 3, 9, 10, 27, 38, 43, 82
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("quick-sort", "tim-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createHeapSort() = Algorithm(
        id = "heap-sort",
        name = "Heap Sort",
        koreanName = "힙 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "힙 자료구조를 이용한 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("힙 기반", "제자리 정렬", "불안정 정렬"),
        advantages = listOf("항상 O(n log n) 보장", "추가 메모리 불필요"),
        disadvantages = listOf("캐시 비효율적", "불안정 정렬"),
        useCases = listOf("메모리 제한 환경", "우선순위 큐"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun heapSort(arr: IntArray) {
    val n = arr.size
    // 힙 구성
    for (i in n / 2 - 1 downTo 0) {
        heapify(arr, n, i)
    }
    // 정렬
    for (i in n - 1 downTo 1) {
        val temp = arr[0]
        arr[0] = arr[i]
        arr[i] = temp
        heapify(arr, i, 0)
    }
}

fun heapify(arr: IntArray, n: Int, i: Int) {
    var largest = i
    val left = 2 * i + 1
    val right = 2 * i + 2
    if (left < n && arr[left] > arr[largest]) largest = left
    if (right < n && arr[right] > arr[largest]) largest = right
    if (largest != i) {
        val temp = arr[i]; arr[i] = arr[largest]; arr[largest] = temp
        heapify(arr, n, largest)
    }
}

fun main() {
    val arr = intArrayOf(12, 11, 13, 5, 6, 7)
    println("정렬 전: ${'$'}{arr.joinToString()}")
    heapSort(arr)
    println("정렬 후: ${'$'}{arr.joinToString()}")
}
                """.trimIndent(),
                explanation = "힙을 이용한 정렬 알고리즘",
                expectedOutput = """
정렬 전: 12, 11, 13, 5, 6, 7
정렬 후: 5, 6, 7, 11, 12, 13
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("quick-sort", "merge-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    // ==================== 탐색 알고리즘 ====================

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

    // ==================== 그래프 알고리즘 ====================

    private fun createBFS() = Algorithm(
        id = "bfs",
        name = "Breadth-First Search",
        koreanName = "너비 우선 탐색",
        category = AlgorithmCategory.GRAPH,
        purpose = "시작 정점에서 가까운 정점부터 탐색",
        timeComplexity = TimeComplexity(
            best = "O(V+E)",
            average = "O(V+E)",
            worst = "O(V+E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("큐 사용", "레벨 순서 탐색", "최단 경로 보장 (무가중치)"),
        advantages = listOf("최단 경로 찾기에 적합", "목표가 가까우면 빠름"),
        disadvantages = listOf("메모리 사용량 많음", "가중치 그래프에서는 최단 경로 보장 안됨"),
        useCases = listOf("최단 경로 (무가중치)", "소셜 네트워크", "웹 크롤링"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun bfs(graph: Map<Int, List<Int>>, start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val queue = ArrayDeque<Int>()
    val result = mutableListOf<Int>()

    queue.add(start)
    visited.add(start)

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        result.add(node)

        graph[node]?.forEach { neighbor ->
            if (neighbor !in visited) {
                visited.add(neighbor)
                queue.add(neighbor)
            }
        }
    }
    return result
}

fun main() {
    val graph = mapOf(
        0 to listOf(1, 2),
        1 to listOf(0, 3, 4),
        2 to listOf(0, 5, 6),
        3 to listOf(1),
        4 to listOf(1),
        5 to listOf(2),
        6 to listOf(2)
    )
    val result = bfs(graph, 0)
    println("BFS 탐색 순서: ${'$'}{result.joinToString(" -> ")}")
}
                """.trimIndent(),
                explanation = "큐를 사용하여 레벨 순서로 탐색하는 BFS",
                expectedOutput = "BFS 탐색 순서: 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6"
            )
        ),
        relatedAlgorithmIds = listOf("dfs", "dijkstra"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createDFS() = Algorithm(
        id = "dfs",
        name = "Depth-First Search",
        koreanName = "깊이 우선 탐색",
        category = AlgorithmCategory.GRAPH,
        purpose = "한 경로를 끝까지 탐색 후 백트래킹",
        timeComplexity = TimeComplexity(
            best = "O(V+E)",
            average = "O(V+E)",
            worst = "O(V+E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("스택/재귀 사용", "깊이 방향 탐색", "경로 탐색에 적합"),
        advantages = listOf("메모리 효율적", "경로 존재 여부 확인에 적합"),
        disadvantages = listOf("최단 경로 보장 안됨", "무한 루프 가능성"),
        useCases = listOf("미로 탐색", "위상 정렬", "사이클 검출"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun dfs(graph: Map<Int, List<Int>>, start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val result = mutableListOf<Int>()

    fun dfsHelper(node: Int) {
        visited.add(node)
        result.add(node)
        graph[node]?.forEach { neighbor ->
            if (neighbor !in visited) {
                dfsHelper(neighbor)
            }
        }
    }

    dfsHelper(start)
    return result
}

fun main() {
    val graph = mapOf(
        0 to listOf(1, 2),
        1 to listOf(0, 3, 4),
        2 to listOf(0, 5, 6),
        3 to listOf(1),
        4 to listOf(1),
        5 to listOf(2),
        6 to listOf(2)
    )
    val result = dfs(graph, 0)
    println("DFS 탐색 순서: ${'$'}{result.joinToString(" -> ")}")
}
                """.trimIndent(),
                explanation = "재귀를 사용하여 깊이 방향으로 탐색하는 DFS",
                expectedOutput = "DFS 탐색 순서: 0 -> 1 -> 3 -> 4 -> 2 -> 5 -> 6"
            )
        ),
        relatedAlgorithmIds = listOf("bfs", "topological-sort"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createDijkstra() = Algorithm(
        id = "dijkstra",
        name = "Dijkstra's Algorithm",
        koreanName = "다익스트라",
        category = AlgorithmCategory.GRAPH,
        purpose = "단일 출발점에서 모든 정점까지의 최단 경로 (양수 가중치)",
        timeComplexity = TimeComplexity(
            best = "O(E log V)",
            average = "O(E log V)",
            worst = "O(V²)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("우선순위 큐 사용", "그리디 알고리즘", "음수 가중치 불가"),
        advantages = listOf("효율적인 최단 경로 계산", "양수 가중치에서 정확한 결과"),
        disadvantages = listOf("음수 가중치 처리 불가", "우선순위 큐 필요"),
        useCases = listOf("GPS 내비게이션", "네트워크 라우팅", "지도 서비스"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun dijkstra(graph: Map<Int, List<Pair<Int, Int>>>, start: Int): Map<Int, Int> {
    val dist = mutableMapOf<Int, Int>().withDefault { Int.MAX_VALUE }
    val pq = java.util.PriorityQueue<Pair<Int, Int>>(compareBy { it.second })

    dist[start] = 0
    pq.add(start to 0)

    while (pq.isNotEmpty()) {
        val (u, d) = pq.poll()
        if (d > dist.getValue(u)) continue

        graph[u]?.forEach { (v, weight) ->
            val newDist = dist.getValue(u) + weight
            if (newDist < dist.getValue(v)) {
                dist[v] = newDist
                pq.add(v to newDist)
            }
        }
    }
    return dist
}

fun main() {
    val graph = mapOf(
        0 to listOf(1 to 4, 2 to 1),
        1 to listOf(3 to 1),
        2 to listOf(1 to 2, 3 to 5),
        3 to listOf<Pair<Int, Int>>()
    )
    val distances = dijkstra(graph, 0)
    println("시작 노드: 0")
    distances.toSortedMap().forEach { (node, dist) ->
        println("노드 ${'$'}node까지 최단 거리: ${'$'}dist")
    }
}
                """.trimIndent(),
                explanation = "우선순위 큐를 사용한 다익스트라 최단 경로",
                expectedOutput = """
시작 노드: 0
노드 0까지 최단 거리: 0
노드 1까지 최단 거리: 3
노드 2까지 최단 거리: 1
노드 3까지 최단 거리: 4
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("bellman-ford", "a-star", "floyd-warshall"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createBellmanFord() = Algorithm(
        id = "bellman-ford",
        name = "Bellman-Ford Algorithm",
        koreanName = "벨만-포드",
        category = AlgorithmCategory.GRAPH,
        purpose = "단일 출발점에서 모든 정점까지의 최단 경로 (음수 가중치 허용)",
        timeComplexity = TimeComplexity(
            best = "O(VE)",
            average = "O(VE)",
            worst = "O(VE)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("음수 가중치 허용", "음수 사이클 검출 가능", "V-1번 반복"),
        advantages = listOf("음수 가중치 처리 가능", "음수 사이클 검출"),
        disadvantages = listOf("Dijkstra보다 느림"),
        useCases = listOf("음수 가중치 그래프", "차익 거래 감지", "네트워크 라우팅"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Edge(val from: Int, val to: Int, val weight: Int)

fun bellmanFord(n: Int, edges: List<Edge>, start: Int): Map<Int, Int>? {
    val dist = IntArray(n) { Int.MAX_VALUE }
    dist[start] = 0

    // V-1번 반복
    repeat(n - 1) {
        edges.forEach { (u, v, w) ->
            if (dist[u] != Int.MAX_VALUE && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w
            }
        }
    }

    // 음수 사이클 검출
    edges.forEach { (u, v, w) ->
        if (dist[u] != Int.MAX_VALUE && dist[u] + w < dist[v]) {
            return null // 음수 사이클 존재
        }
    }

    return dist.mapIndexed { i, d -> i to d }.toMap()
}

fun main() {
    val edges = listOf(
        Edge(0, 1, -1),
        Edge(0, 2, 4),
        Edge(1, 2, 3),
        Edge(1, 3, 2),
        Edge(3, 2, 5),
        Edge(3, 1, 1)
    )
    val result = bellmanFord(4, edges, 0)
    println("시작 노드: 0")
    result?.toSortedMap()?.forEach { (node, dist) ->
        println("노드 ${'$'}node까지 최단 거리: ${'$'}dist")
    }
}
                """.trimIndent(),
                explanation = "음수 가중치를 허용하는 벨만-포드 알고리즘",
                expectedOutput = """
시작 노드: 0
노드 0까지 최단 거리: 0
노드 1까지 최단 거리: -1
노드 2까지 최단 거리: 2
노드 3까지 최단 거리: 1
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("dijkstra", "spfa"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createAStar() = Algorithm(
        id = "a-star",
        name = "A* Search",
        koreanName = "A* 탐색",
        category = AlgorithmCategory.GRAPH,
        purpose = "휴리스틱을 이용한 효율적인 경로 탐색",
        timeComplexity = TimeComplexity(
            best = "O(E)",
            average = "O(E)",
            worst = "O(V²)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("f(n) = g(n) + h(n)", "휴리스틱 기반", "최적 경로 보장"),
        advantages = listOf("Dijkstra보다 빠름", "최단 경로 보장"),
        disadvantages = listOf("휴리스틱 설계 필요", "메모리 사용량"),
        useCases = listOf("게임 AI 경로 찾기", "로봇 내비게이션", "지도 서비스"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Node(val x: Int, val y: Int)

fun reconstructPath(cameFrom: Map<Node, Node>, current: Node): List<Node> {
    val path = mutableListOf(current)
    var node = current
    while (cameFrom.containsKey(node)) {
        node = cameFrom[node]!!
        path.add(0, node)
    }
    return path
}

fun aStar(
    start: Node, goal: Node,
    neighbors: (Node) -> List<Node>,
    heuristic: (Node, Node) -> Int
): List<Node>? {
    val openSet = java.util.PriorityQueue<Pair<Node, Int>>(compareBy { it.second })
    val cameFrom = mutableMapOf<Node, Node>()
    val gScore = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }

    gScore[start] = 0
    openSet.add(start to heuristic(start, goal))

    while (openSet.isNotEmpty()) {
        val (current, _) = openSet.poll()
        if (current == goal) return reconstructPath(cameFrom, current)

        neighbors(current).forEach { neighbor ->
            val tentativeG = gScore.getValue(current) + 1
            if (tentativeG < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeG
                val f = tentativeG + heuristic(neighbor, goal)
                openSet.add(neighbor to f)
            }
        }
    }
    return null
}

fun main() {
    val start = Node(0, 0)
    val goal = Node(2, 2)
    val neighbors: (Node) -> List<Node> = { node ->
        listOf(Node(node.x+1, node.y), Node(node.x, node.y+1))
            .filter { it.x <= 2 && it.y <= 2 }
    }
    val heuristic: (Node, Node) -> Int = { a, b ->
        kotlin.math.abs(a.x - b.x) + kotlin.math.abs(a.y - b.y)
    }
    val path = aStar(start, goal, neighbors, heuristic)
    println("시작: ${'$'}start, 목표: ${'$'}goal")
    println("경로: ${'$'}{path?.joinToString(" -> ")}")
}
                """.trimIndent(),
                explanation = "휴리스틱을 활용한 A* 경로 탐색",
                expectedOutput = """
시작: Node(x=0, y=0), 목표: Node(x=2, y=2)
경로: Node(x=0, y=0) -> Node(x=1, y=0) -> Node(x=2, y=0) -> Node(x=2, y=1) -> Node(x=2, y=2)
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("dijkstra", "bfs"),
        difficulty = Difficulty.HIGH,
        frequency = 4
    )

    private fun createFloydWarshall() = Algorithm(
        id = "floyd-warshall",
        name = "Floyd-Warshall Algorithm",
        koreanName = "플로이드-워셜",
        category = AlgorithmCategory.GRAPH,
        purpose = "모든 정점 쌍 간의 최단 경로",
        timeComplexity = TimeComplexity(
            best = "O(V³)",
            average = "O(V³)",
            worst = "O(V³)"
        ),
        spaceComplexity = "O(V²)",
        characteristics = listOf("동적 프로그래밍", "모든 쌍 최단 경로", "음수 가중치 허용"),
        advantages = listOf("구현이 간단", "모든 쌍 한번에 계산"),
        disadvantages = listOf("O(V³) 시간 복잡도", "큰 그래프에서 비효율적"),
        useCases = listOf("모든 쌍 최단 경로", "그래프 도달 가능성", "전이 폐쇄"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun floydWarshall(n: Int, graph: Array<IntArray>): Array<IntArray> {
    val dist = Array(n) { i -> graph[i].copyOf() }

    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (dist[i][k] != Int.MAX_VALUE &&
                    dist[k][j] != Int.MAX_VALUE &&
                    dist[i][k] + dist[k][j] < dist[i][j]) {
                    dist[i][j] = dist[i][k] + dist[k][j]
                }
            }
        }
    }
    return dist
}

fun main() {
    val INF = Int.MAX_VALUE
    val graph = arrayOf(
        intArrayOf(0, 3, INF, 5),
        intArrayOf(2, 0, INF, 4),
        intArrayOf(INF, 1, 0, INF),
        intArrayOf(INF, INF, 2, 0)
    )
    val result = floydWarshall(4, graph)
    println("모든 쌍 최단 경로:")
    result.forEachIndexed { i, row ->
        println("${'$'}i -> ${'$'}{row.map { if (it == INF) "INF" else it.toString() }}")
    }
}
                """.trimIndent(),
                explanation = "모든 정점 쌍 간의 최단 경로를 구하는 플로이드-워셜",
                expectedOutput = """
모든 쌍 최단 경로:
0 -> [0, 3, 7, 5]
1 -> [2, 0, 6, 4]
2 -> [3, 1, 0, 5]
3 -> [5, 3, 2, 0]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("dijkstra", "johnson"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    // ==================== 동적 프로그래밍 ====================

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

    // ==================== 문자열 알고리즘 ====================

    private fun createKMP() = Algorithm(
        id = "kmp",
        name = "KMP Algorithm",
        koreanName = "KMP 알고리즘",
        category = AlgorithmCategory.STRING,
        purpose = "패턴 문자열을 텍스트에서 효율적으로 검색",
        timeComplexity = TimeComplexity(
            best = "O(n+m)",
            average = "O(n+m)",
            worst = "O(n+m)"
        ),
        spaceComplexity = "O(m)",
        characteristics = listOf("실패 함수 사용", "중복 비교 없음"),
        advantages = listOf("선형 시간 보장", "전처리로 효율적 검색"),
        disadvantages = listOf("실패 함수 이해 어려움"),
        useCases = listOf("텍스트 편집기 검색", "DNA 서열 매칭", "문서 검색"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun computeLPS(pattern: String): IntArray {
    val lps = IntArray(pattern.length)
    var len = 0
    var i = 1
    while (i < pattern.length) {
        if (pattern[i] == pattern[len]) {
            lps[i++] = ++len
        } else if (len != 0) {
            len = lps[len - 1]
        } else {
            lps[i++] = 0
        }
    }
    return lps
}

fun kmpSearch(text: String, pattern: String): List<Int> {
    val lps = computeLPS(pattern)
    val result = mutableListOf<Int>()
    var i = 0; var j = 0
    while (i < text.length) {
        if (text[i] == pattern[j]) { i++; j++ }
        if (j == pattern.length) {
            result.add(i - j)
            j = lps[j - 1]
        } else if (i < text.length && text[i] != pattern[j]) {
            j = if (j != 0) lps[j - 1] else 0.also { i++ }
        }
    }
    return result
}

fun main() {
    val text = "ABABDABACDABABCABAB"
    val pattern = "ABABCABAB"
    println("텍스트: ${'$'}text")
    println("패턴: ${'$'}pattern")
    val positions = kmpSearch(text, pattern)
    println("패턴 발견 위치: ${'$'}{positions.joinToString()}")
}
                """.trimIndent(),
                explanation = "실패 함수를 사용한 KMP 문자열 검색",
                expectedOutput = """
텍스트: ABABDABACDABABCABAB
패턴: ABABCABAB
패턴 발견 위치: 10
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("rabin-karp", "boyer-moore"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createTrie() = Algorithm(
        id = "trie",
        name = "Trie",
        koreanName = "트라이",
        category = AlgorithmCategory.STRING,
        purpose = "문자열 집합의 효율적인 저장과 검색",
        timeComplexity = TimeComplexity(
            best = "O(m)",
            average = "O(m)",
            worst = "O(m)"
        ),
        spaceComplexity = "O(ALPHABET * n * m)",
        characteristics = listOf("접두사 트리", "공통 접두사 공유"),
        advantages = listOf("빠른 검색 및 접두사 매칭", "자동 완성에 적합"),
        disadvantages = listOf("메모리 사용량 많음"),
        useCases = listOf("자동 완성", "사전 구현", "IP 라우팅"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class TrieNode {
    val children = mutableMapOf<Char, TrieNode>()
    var isEndOfWord = false
}

class Trie {
    private val root = TrieNode()

    fun insert(word: String) {
        var node = root
        for (ch in word) {
            node = node.children.getOrPut(ch) { TrieNode() }
        }
        node.isEndOfWord = true
    }

    fun search(word: String): Boolean {
        var node = root
        for (ch in word) {
            node = node.children[ch] ?: return false
        }
        return node.isEndOfWord
    }

    fun startsWith(prefix: String): Boolean {
        var node = root
        for (ch in prefix) {
            node = node.children[ch] ?: return false
        }
        return true
    }
}

fun main() {
    val trie = Trie()
    trie.insert("apple")
    trie.insert("app")
    trie.insert("application")
    println("search('apple'): ${'$'}{trie.search("apple")}")
    println("search('app'): ${'$'}{trie.search("app")}")
    println("search('appl'): ${'$'}{trie.search("appl")}")
    println("startsWith('app'): ${'$'}{trie.startsWith("app")}")
}
                """.trimIndent(),
                explanation = "접두사 트리를 구현한 Trie 자료구조",
                expectedOutput = """
search('apple'): true
search('app'): true
search('appl'): false
startsWith('app'): true
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("suffix-tree", "aho-corasick"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    // ==================== 수학 알고리즘 ====================

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

    // ==================== 백트래킹 ====================

    private fun createNQueens() = Algorithm(
        id = "n-queens",
        name = "N-Queens",
        koreanName = "N-퀸",
        category = AlgorithmCategory.BACKTRACKING,
        purpose = "N×N 체스판에 N개의 퀸을 서로 공격하지 않게 배치",
        timeComplexity = TimeComplexity(
            best = "O(N!)",
            average = "O(N!)",
            worst = "O(N!)"
        ),
        spaceComplexity = "O(N)",
        characteristics = listOf("대표적인 백트래킹 문제", "행 단위로 퀸 배치"),
        advantages = listOf("가지치기로 탐색 공간 축소", "다양한 최적화 가능"),
        disadvantages = listOf("지수적 시간 복잡도"),
        useCases = listOf("조합 최적화", "제약 만족 문제", "스케줄링"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun solveNQueens(n: Int): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val board = Array(n) { CharArray(n) { '.' } }

    fun isSafe(row: Int, col: Int): Boolean {
        for (i in 0 until row) {
            if (board[i][col] == 'Q') return false
            if (col - (row - i) >= 0 && board[i][col - (row - i)] == 'Q') return false
            if (col + (row - i) < n && board[i][col + (row - i)] == 'Q') return false
        }
        return true
    }

    fun backtrack(row: Int) {
        if (row == n) {
            result.add(board.map { it.joinToString("") })
            return
        }
        for (col in 0 until n) {
            if (isSafe(row, col)) {
                board[row][col] = 'Q'
                backtrack(row + 1)
                board[row][col] = '.'
            }
        }
    }

    backtrack(0)
    return result
}

fun main() {
    val n = 4
    val solutions = solveNQueens(n)
    println("${'$'}n-Queens 해답 개수: ${'$'}{solutions.size}")
    println("첫 번째 해답:")
    solutions.firstOrNull()?.forEach { println(it) }
}
                """.trimIndent(),
                explanation = "백트래킹으로 N-Queens 문제 해결",
                expectedOutput = """
4-Queens 해답 개수: 2
첫 번째 해답:
.Q..
...Q
Q...
..Q.
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("sudoku-solver", "graph-coloring"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    // ==================== 탐욕 알고리즘 ====================

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
}
