package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 그래프 알고리즘 데이터 (12개)
 *
 * BFS, DFS, Dijkstra, Bellman-Ford, A*, Floyd-Warshall,
 * Prim, Kruskal, Topological Sort, Tarjan SCC, Kosaraju, Johnson
 */
internal object GraphAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createBFS(),
        createDFS(),
        createDijkstra(),
        createBellmanFord(),
        createAStar(),
        createFloydWarshall(),
        createPrim(),
        createKruskal(),
        createTopologicalSort(),
        createTarjanSCC(),
        createKosaraju(),
        createJohnson()
    )

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

    private fun createPrim() = Algorithm(
        id = "prim",
        name = "Prim's Algorithm",
        koreanName = "프림 알고리즘",
        category = AlgorithmCategory.GRAPH,
        purpose = "최소 신장 트리(MST) 구성",
        timeComplexity = TimeComplexity(
            best = "O(E log V)",
            average = "O(E log V)",
            worst = "O(E log V)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("정점 기반 MST", "우선순위 큐 사용", "밀집 그래프에 적합"),
        advantages = listOf("밀집 그래프에서 효율적", "하나의 트리에서 시작"),
        disadvantages = listOf("희소 그래프에서 Kruskal보다 느림"),
        useCases = listOf("네트워크 설계", "클러스터링", "근사 TSP"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Edge(val to: Int, val weight: Int)

fun prim(graph: Map<Int, List<Edge>>, n: Int): Int {
    val visited = BooleanArray(n)
    val pq = java.util.PriorityQueue<Pair<Int, Int>>(compareBy { it.second }) // (node, weight)
    pq.add(0 to 0)
    var totalWeight = 0

    while (pq.isNotEmpty()) {
        val (node, weight) = pq.poll()
        if (visited[node]) continue
        visited[node] = true
        totalWeight += weight
        graph[node]?.forEach { edge ->
            if (!visited[edge.to]) pq.add(edge.to to edge.weight)
        }
    }
    return totalWeight
}

fun main() {
    val graph = mapOf(
        0 to listOf(Edge(1, 4), Edge(7, 8)),
        1 to listOf(Edge(0, 4), Edge(2, 8), Edge(7, 11)),
        2 to listOf(Edge(1, 8), Edge(3, 7), Edge(5, 4)),
        3 to listOf(Edge(2, 7), Edge(4, 9), Edge(5, 14)),
        4 to listOf(Edge(3, 9), Edge(5, 10)),
        5 to listOf(Edge(2, 4), Edge(3, 14), Edge(4, 10), Edge(6, 2)),
        6 to listOf(Edge(5, 2), Edge(7, 1)),
        7 to listOf(Edge(0, 8), Edge(1, 11), Edge(6, 1))
    )
    println("MST 총 가중치: ${'$'}{prim(graph, 8)}")
}
                """.trimIndent(),
                explanation = "우선순위 큐를 사용한 프림 알고리즘",
                expectedOutput = """
MST 총 가중치: 37
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("kruskal", "dijkstra"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createKruskal() = Algorithm(
        id = "kruskal",
        name = "Kruskal's Algorithm",
        koreanName = "크루스칼 알고리즘",
        category = AlgorithmCategory.GRAPH,
        purpose = "최소 신장 트리(MST) 구성",
        timeComplexity = TimeComplexity(
            best = "O(E log E)",
            average = "O(E log E)",
            worst = "O(E log E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("간선 기반 MST", "Union-Find 사용", "희소 그래프에 적합"),
        advantages = listOf("희소 그래프에서 효율적", "구현이 직관적"),
        disadvantages = listOf("간선 정렬 필요"),
        useCases = listOf("네트워크 설계", "이미지 분할", "클러스터링"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class KEdge(val from: Int, val to: Int, val weight: Int)

class UnionFind(n: Int) {
    private val parent = IntArray(n) { it }
    private val rank = IntArray(n) { 0 }
    fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }
    fun union(x: Int, y: Int): Boolean {
        val px = find(x); val py = find(y)
        if (px == py) return false
        when { rank[px] < rank[py] -> parent[px] = py
               rank[px] > rank[py] -> parent[py] = px
               else -> { parent[py] = px; rank[px]++ } }
        return true
    }
}

fun kruskal(edges: List<KEdge>, n: Int): Int {
    val sortedEdges = edges.sortedBy { it.weight }
    val uf = UnionFind(n)
    var totalWeight = 0; var edgeCount = 0
    for (edge in sortedEdges) {
        if (uf.union(edge.from, edge.to)) {
            totalWeight += edge.weight
            if (++edgeCount == n - 1) break
        }
    }
    return totalWeight
}

fun main() {
    val edges = listOf(
        KEdge(0, 1, 4), KEdge(0, 7, 8), KEdge(1, 2, 8), KEdge(1, 7, 11),
        KEdge(2, 3, 7), KEdge(2, 5, 4), KEdge(3, 4, 9), KEdge(3, 5, 14),
        KEdge(4, 5, 10), KEdge(5, 6, 2), KEdge(6, 7, 1)
    )
    println("MST 총 가중치: ${'$'}{kruskal(edges, 8)}")
}
                """.trimIndent(),
                explanation = "Union-Find를 사용한 크루스칼 알고리즘",
                expectedOutput = """
MST 총 가중치: 37
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("prim", "union-find"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createTopologicalSort() = Algorithm(
        id = "topological-sort",
        name = "Topological Sort",
        koreanName = "위상 정렬",
        category = AlgorithmCategory.GRAPH,
        purpose = "DAG(방향 비순환 그래프)의 정점을 선형 순서로 정렬",
        timeComplexity = TimeComplexity(
            best = "O(V + E)",
            average = "O(V + E)",
            worst = "O(V + E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("DAG에서만 가능", "의존성 순서 결정", "여러 유효한 순서 가능"),
        advantages = listOf("의존성 해결에 적합", "선형 시간 복잡도"),
        disadvantages = listOf("DAG에서만 사용 가능"),
        useCases = listOf("빌드 시스템", "작업 스케줄링", "과목 선수 과목 결정"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
import java.util.LinkedList

// Kahn's Algorithm (BFS 기반)
fun topologicalSort(graph: Map<Int, List<Int>>, n: Int): List<Int>? {
    val inDegree = IntArray(n)
    graph.values.flatten().forEach { inDegree[it]++ }

    val queue = LinkedList<Int>()
    for (i in 0 until n) if (inDegree[i] == 0) queue.add(i)

    val result = mutableListOf<Int>()
    while (queue.isNotEmpty()) {
        val node = queue.poll()
        result.add(node)
        graph[node]?.forEach { neighbor ->
            if (--inDegree[neighbor] == 0) queue.add(neighbor)
        }
    }
    return if (result.size == n) result else null // null = 사이클 존재
}

fun main() {
    val graph = mapOf(
        0 to listOf(1, 2),
        1 to listOf(3),
        2 to listOf(3, 4),
        3 to listOf(5),
        4 to listOf(5),
        5 to emptyList()
    )
    println("위상 정렬 결과: ${'$'}{topologicalSort(graph, 6)}")
}
                """.trimIndent(),
                explanation = "Kahn's 알고리즘을 사용한 위상 정렬",
                expectedOutput = """
위상 정렬 결과: [0, 1, 2, 3, 4, 5]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("dfs", "bfs"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createTarjanSCC() = Algorithm(
        id = "tarjan-scc",
        name = "Tarjan's SCC",
        koreanName = "타잔 강결합 요소",
        category = AlgorithmCategory.GRAPH,
        purpose = "방향 그래프에서 강결합 요소(SCC) 찾기",
        timeComplexity = TimeComplexity(
            best = "O(V + E)",
            average = "O(V + E)",
            worst = "O(V + E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("DFS 기반", "단일 패스로 SCC 탐지", "low-link 값 활용"),
        advantages = listOf("효율적인 SCC 탐지", "한 번의 DFS로 완료"),
        disadvantages = listOf("구현이 복잡"),
        useCases = listOf("소셜 네트워크 분석", "2-SAT 문제", "데드락 감지"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class TarjanSCC(private val n: Int, private val graph: Map<Int, List<Int>>) {
    private var index = 0
    private val indices = IntArray(n) { -1 }
    private val lowlink = IntArray(n)
    private val onStack = BooleanArray(n)
    private val stack = ArrayDeque<Int>()
    private val sccs = mutableListOf<List<Int>>()

    fun findSCCs(): List<List<Int>> {
        for (v in 0 until n) if (indices[v] == -1) strongconnect(v)
        return sccs
    }

    private fun strongconnect(v: Int) {
        indices[v] = index; lowlink[v] = index; index++
        stack.addLast(v); onStack[v] = true
        graph[v]?.forEach { w ->
            if (indices[w] == -1) { strongconnect(w); lowlink[v] = minOf(lowlink[v], lowlink[w]) }
            else if (onStack[w]) lowlink[v] = minOf(lowlink[v], indices[w])
        }
        if (lowlink[v] == indices[v]) {
            val scc = mutableListOf<Int>()
            do { val w = stack.removeLast(); onStack[w] = false; scc.add(w) } while (w != v)
            sccs.add(scc)
        }
    }
}

fun main() {
    val graph = mapOf(0 to listOf(1), 1 to listOf(2), 2 to listOf(0, 3), 3 to listOf(4), 4 to listOf(5), 5 to listOf(3))
    val sccs = TarjanSCC(6, graph).findSCCs()
    println("SCCs: ${'$'}sccs")
}
                """.trimIndent(),
                explanation = "타잔 알고리즘을 사용한 강결합 요소 탐지",
                expectedOutput = """
SCCs: [[5, 4, 3], [2, 1, 0]]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("kosaraju", "dfs"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )

    private fun createKosaraju() = Algorithm(
        id = "kosaraju",
        name = "Kosaraju's Algorithm",
        koreanName = "코사라주 알고리즘",
        category = AlgorithmCategory.GRAPH,
        purpose = "방향 그래프에서 강결합 요소(SCC) 찾기",
        timeComplexity = TimeComplexity(
            best = "O(V + E)",
            average = "O(V + E)",
            worst = "O(V + E)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("두 번의 DFS 사용", "역방향 그래프 활용", "이해하기 쉬운 구조"),
        advantages = listOf("구현이 직관적", "이해하기 쉬움"),
        disadvantages = listOf("두 번의 DFS 필요", "역방향 그래프 구성 필요"),
        useCases = listOf("SCC 찾기", "그래프 축약"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun kosarajuSCC(graph: Map<Int, List<Int>>, n: Int): List<List<Int>> {
    val visited = BooleanArray(n)
    val finishOrder = mutableListOf<Int>()

    fun dfs1(v: Int) {
        visited[v] = true
        graph[v]?.forEach { w -> if (!visited[w]) dfs1(w) }
        finishOrder.add(v)
    }
    for (i in 0 until n) if (!visited[i]) dfs1(i)

    // 역방향 그래프 구성
    val reverseGraph = mutableMapOf<Int, MutableList<Int>>()
    graph.forEach { (from, neighbors) -> neighbors.forEach { to ->
        reverseGraph.getOrPut(to) { mutableListOf() }.add(from)
    }}

    visited.fill(false)
    val sccs = mutableListOf<List<Int>>()
    fun dfs2(v: Int, scc: MutableList<Int>) {
        visited[v] = true; scc.add(v)
        reverseGraph[v]?.forEach { w -> if (!visited[w]) dfs2(w, scc) }
    }
    for (v in finishOrder.reversed()) {
        if (!visited[v]) { val scc = mutableListOf<Int>(); dfs2(v, scc); sccs.add(scc) }
    }
    return sccs
}

fun main() {
    val graph = mapOf(0 to listOf(1), 1 to listOf(2), 2 to listOf(0, 3), 3 to listOf(4), 4 to listOf(5), 5 to listOf(3))
    println("SCCs: ${'$'}{kosarajuSCC(graph, 6)}")
}
                """.trimIndent(),
                explanation = "두 번의 DFS를 사용한 코사라주 알고리즘",
                expectedOutput = """
SCCs: [[0, 2, 1], [3, 5, 4]]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("tarjan-scc", "dfs"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createJohnson() = Algorithm(
        id = "johnson",
        name = "Johnson's Algorithm",
        koreanName = "존슨 알고리즘",
        category = AlgorithmCategory.GRAPH,
        purpose = "희소 그래프에서 모든 쌍 최단 경로 (음수 가중치 허용)",
        timeComplexity = TimeComplexity(
            best = "O(V² log V + VE)",
            average = "O(V² log V + VE)",
            worst = "O(V² log V + VE)"
        ),
        spaceComplexity = "O(V²)",
        characteristics = listOf("Bellman-Ford + Dijkstra 조합", "가중치 재조정으로 음수 제거"),
        advantages = listOf("희소 그래프에서 Floyd-Warshall보다 빠름", "음수 가중치 허용"),
        disadvantages = listOf("구현이 복잡", "밀집 그래프에서는 비효율적"),
        useCases = listOf("희소 그래프의 모든 쌍 최단 경로", "음수 가중치가 있는 네트워크"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// Johnson's Algorithm 개요
// 1. 새 정점 s 추가, 모든 정점에 가중치 0 간선 연결
// 2. Bellman-Ford로 s에서 각 정점까지 거리 h[v] 계산
// 3. 모든 간선 가중치 재조정: w'(u,v) = w(u,v) + h[u] - h[v]
// 4. 각 정점에서 Dijkstra 실행
// 5. 원래 거리로 복원: d(u,v) = d'(u,v) - h[u] + h[v]

fun johnsonExample() {
    // 단순화된 예시 (실제 구현은 Bellman-Ford + Dijkstra 조합)
    val INF = Int.MAX_VALUE
    val graph = arrayOf(
        intArrayOf(0, -5, INF, 10),
        intArrayOf(INF, 0, 3, INF),
        intArrayOf(INF, INF, 0, 1),
        intArrayOf(INF, INF, INF, 0)
    )
    println("Johnson's Algorithm 적용:")
    println("희소 그래프의 모든 쌍 최단 경로에 적합")
    println("복잡도: O(V² log V + VE)")
}

fun main() {
    johnsonExample()
}
                """.trimIndent(),
                explanation = "Bellman-Ford와 Dijkstra를 결합한 Johnson 알고리즘",
                expectedOutput = """
Johnson's Algorithm 적용:
희소 그래프의 모든 쌍 최단 경로에 적합
복잡도: O(V² log V + VE)
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("floyd-warshall", "bellman-ford", "dijkstra"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )
}
