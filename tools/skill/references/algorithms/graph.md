# 그래프 알고리즘 (Graph Algorithms)

그래프 자료구조에서 탐색, 최단 경로, 최소 신장 트리 등을 구하는 알고리즘입니다.

---

## 1. BFS (Breadth-First Search, 너비 우선 탐색)

**목적**: 시작 정점에서 가까운 정점부터 탐색

**시간 복잡도**: O(V + E)

**공간 복잡도**: O(V)

**특징**:
- 큐(Queue) 사용
- 레벨 순서대로 탐색
- 최단 경로 보장 (무가중치 그래프)

**장점**:
- 최단 경로 찾기에 적합 (무가중치)
- 목표가 가까우면 빠름

**단점**:
- 메모리 사용량이 많음
- 가중치 그래프에서는 최단 경로 보장 안됨

**활용 예시**:
- 최단 경로 (무가중치)
- 소셜 네트워크 친구 추천
- 웹 크롤링

**난이도**: 낮음 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
import java.util.LinkedList
import java.util.Queue

fun bfs(graph: Map<Int, List<Int>>, start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val result = mutableListOf<Int>()
    val queue: Queue<Int> = LinkedList()

    queue.add(start)
    visited.add(start)

    while (queue.isNotEmpty()) {
        val node = queue.poll()
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

// 최단 거리 계산
fun bfsShortestPath(graph: Map<Int, List<Int>>, start: Int, end: Int): Int {
    val visited = mutableSetOf<Int>()
    val queue: Queue<Pair<Int, Int>> = LinkedList() // (node, distance)

    queue.add(start to 0)
    visited.add(start)

    while (queue.isNotEmpty()) {
        val (node, dist) = queue.poll()
        if (node == end) return dist

        graph[node]?.forEach { neighbor ->
            if (neighbor !in visited) {
                visited.add(neighbor)
                queue.add(neighbor to dist + 1)
            }
        }
    }

    return -1 // 경로 없음
}
```

**관련 알고리즘**: DFS, Dijkstra

---

## 2. DFS (Depth-First Search, 깊이 우선 탐색)

**목적**: 한 경로를 끝까지 탐색 후 백트래킹

**시간 복잡도**: O(V + E)

**공간 복잡도**: O(V)

**특징**:
- 스택(Stack) 또는 재귀 사용
- 깊이 방향으로 탐색
- 경로 탐색에 적합

**장점**:
- 메모리 효율적 (깊이만큼만 사용)
- 경로 존재 여부 확인에 적합

**단점**:
- 최단 경로 보장 안됨
- 무한 루프 가능성 (사이클 처리 필요)

**활용 예시**:
- 미로 탐색
- 위상 정렬
- 사이클 검출
- 연결 요소 찾기

**난이도**: 낮음 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// 재귀 방식
fun dfsRecursive(
    graph: Map<Int, List<Int>>,
    node: Int,
    visited: MutableSet<Int> = mutableSetOf(),
    result: MutableList<Int> = mutableListOf()
): List<Int> {
    visited.add(node)
    result.add(node)

    graph[node]?.forEach { neighbor ->
        if (neighbor !in visited) {
            dfsRecursive(graph, neighbor, visited, result)
        }
    }

    return result
}

// 스택 방식 (반복)
fun dfsIterative(graph: Map<Int, List<Int>>, start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val result = mutableListOf<Int>()
    val stack = ArrayDeque<Int>()

    stack.addLast(start)

    while (stack.isNotEmpty()) {
        val node = stack.removeLast()
        if (node in visited) continue

        visited.add(node)
        result.add(node)

        graph[node]?.reversed()?.forEach { neighbor ->
            if (neighbor !in visited) {
                stack.addLast(neighbor)
            }
        }
    }

    return result
}

// 사이클 검출
fun hasCycle(graph: Map<Int, List<Int>>, n: Int): Boolean {
    val visited = mutableSetOf<Int>()
    val recStack = mutableSetOf<Int>()

    fun dfs(node: Int): Boolean {
        visited.add(node)
        recStack.add(node)

        graph[node]?.forEach { neighbor ->
            if (neighbor !in visited && dfs(neighbor)) return true
            if (neighbor in recStack) return true
        }

        recStack.remove(node)
        return false
    }

    for (i in 0 until n) {
        if (i !in visited && dfs(i)) return true
    }
    return false
}
```

**관련 알고리즘**: BFS, Topological Sort, Tarjan SCC

---

## 3. Dijkstra (다익스트라)

**목적**: 단일 출발점에서 모든 정점까지의 최단 경로 (양수 가중치)

**시간 복잡도**: O((V + E) log V) - 우선순위 큐 사용

**공간 복잡도**: O(V)

**특징**:
- 우선순위 큐(최소 힙) 사용
- 그리디 알고리즘
- 음수 가중치 불가

**장점**:
- 효율적인 최단 경로 계산
- 양수 가중치에서 정확한 결과

**단점**:
- 음수 가중치 처리 불가
- 우선순위 큐 필요

**활용 예시**:
- GPS 내비게이션
- 네트워크 라우팅
- 지도 서비스

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
import java.util.PriorityQueue

data class Edge(val to: Int, val weight: Int)

fun dijkstra(graph: Map<Int, List<Edge>>, start: Int, n: Int): IntArray {
    val dist = IntArray(n) { Int.MAX_VALUE }
    dist[start] = 0

    // (distance, node)
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
    pq.add(0 to start)

    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d > dist[u]) continue

        graph[u]?.forEach { edge ->
            val newDist = dist[u] + edge.weight
            if (newDist < dist[edge.to]) {
                dist[edge.to] = newDist
                pq.add(newDist to edge.to)
            }
        }
    }

    return dist
}

// 경로 복원
fun dijkstraWithPath(
    graph: Map<Int, List<Edge>>,
    start: Int,
    end: Int,
    n: Int
): Pair<Int, List<Int>> {
    val dist = IntArray(n) { Int.MAX_VALUE }
    val prev = IntArray(n) { -1 }
    dist[start] = 0

    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
    pq.add(0 to start)

    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d > dist[u]) continue

        graph[u]?.forEach { edge ->
            val newDist = dist[u] + edge.weight
            if (newDist < dist[edge.to]) {
                dist[edge.to] = newDist
                prev[edge.to] = u
                pq.add(newDist to edge.to)
            }
        }
    }

    // 경로 복원
    val path = mutableListOf<Int>()
    var current = end
    while (current != -1) {
        path.add(current)
        current = prev[current]
    }

    return dist[end] to path.reversed()
}
```

**관련 알고리즘**: Bellman-Ford, A*, Floyd-Warshall

---

## 4. Bellman-Ford (벨만-포드)

**목적**: 단일 출발점에서 모든 정점까지의 최단 경로 (음수 가중치 허용)

**시간 복잡도**: O(VE)

**공간 복잡도**: O(V)

**특징**:
- 음수 가중치 허용
- 음수 사이클 검출 가능
- 모든 간선을 V-1번 반복

**장점**:
- 음수 가중치 처리 가능
- 음수 사이클 검출

**단점**:
- Dijkstra보다 느림

**활용 예시**:
- 음수 가중치 그래프
- 차익 거래 감지 (금융)
- 네트워크 라우팅 (RIP)

**난이도**: 중간 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
data class EdgeBF(val from: Int, val to: Int, val weight: Int)

fun bellmanFord(edges: List<EdgeBF>, n: Int, start: Int): IntArray? {
    val dist = IntArray(n) { Int.MAX_VALUE }
    dist[start] = 0

    // V-1번 반복
    repeat(n - 1) {
        edges.forEach { edge ->
            if (dist[edge.from] != Int.MAX_VALUE) {
                val newDist = dist[edge.from] + edge.weight
                if (newDist < dist[edge.to]) {
                    dist[edge.to] = newDist
                }
            }
        }
    }

    // 음수 사이클 검출
    edges.forEach { edge ->
        if (dist[edge.from] != Int.MAX_VALUE &&
            dist[edge.from] + edge.weight < dist[edge.to]) {
            return null // 음수 사이클 존재
        }
    }

    return dist
}

// 음수 사이클이 영향을 미치는 정점 찾기
fun bellmanFordWithNegativeCycle(
    edges: List<EdgeBF>,
    n: Int,
    start: Int
): Pair<LongArray, Set<Int>> {
    val dist = LongArray(n) { Long.MAX_VALUE }
    dist[start] = 0L
    val negCycleNodes = mutableSetOf<Int>()

    repeat(n - 1) {
        edges.forEach { edge ->
            if (dist[edge.from] != Long.MAX_VALUE) {
                val newDist = dist[edge.from] + edge.weight
                if (newDist < dist[edge.to]) {
                    dist[edge.to] = newDist
                }
            }
        }
    }

    // 음수 사이클 영향받는 노드 찾기
    repeat(n) {
        edges.forEach { edge ->
            if (dist[edge.from] != Long.MAX_VALUE &&
                dist[edge.from] + edge.weight < dist[edge.to]) {
                dist[edge.to] = Long.MIN_VALUE
                negCycleNodes.add(edge.to)
            }
        }
    }

    return dist to negCycleNodes
}
```

**관련 알고리즘**: Dijkstra, SPFA

---

## 5. Floyd-Warshall (플로이드-워셜)

**목적**: 모든 정점 쌍 간의 최단 경로

**시간 복잡도**: O(V³)

**공간 복잡도**: O(V²)

**특징**:
- 동적 프로그래밍
- 모든 쌍 최단 경로
- 음수 가중치 허용 (음수 사이클 제외)

**장점**:
- 구현이 간단
- 모든 쌍 한번에 계산

**단점**:
- O(V³) 시간 복잡도
- 큰 그래프에서 비효율적

**활용 예시**:
- 모든 쌍 최단 경로
- 그래프 도달 가능성
- 전이 폐쇄(Transitive Closure)

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
fun floydWarshall(graph: Array<IntArray>): Array<IntArray> {
    val n = graph.size
    val dist = Array(n) { i -> graph[i].copyOf() }

    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (dist[i][k] != Int.MAX_VALUE &&
                    dist[k][j] != Int.MAX_VALUE) {
                    val newDist = dist[i][k] + dist[k][j]
                    if (newDist < dist[i][j]) {
                        dist[i][j] = newDist
                    }
                }
            }
        }
    }

    return dist
}

// 경로 복원 포함
fun floydWarshallWithPath(
    graph: Array<IntArray>
): Pair<Array<IntArray>, Array<IntArray>> {
    val n = graph.size
    val dist = Array(n) { i -> graph[i].copyOf() }
    val next = Array(n) { i -> IntArray(n) { j -> if (graph[i][j] != Int.MAX_VALUE) j else -1 } }

    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (dist[i][k] != Int.MAX_VALUE &&
                    dist[k][j] != Int.MAX_VALUE) {
                    val newDist = dist[i][k] + dist[k][j]
                    if (newDist < dist[i][j]) {
                        dist[i][j] = newDist
                        next[i][j] = next[i][k]
                    }
                }
            }
        }
    }

    return dist to next
}

fun reconstructPath(next: Array<IntArray>, start: Int, end: Int): List<Int> {
    if (next[start][end] == -1) return emptyList()
    val path = mutableListOf(start)
    var current = start
    while (current != end) {
        current = next[current][end]
        path.add(current)
    }
    return path
}
```

**관련 알고리즘**: Dijkstra, Johnson

---

## 6. A* (A-star, 에이스타)

**목적**: 휴리스틱을 이용한 효율적인 경로 탐색

**시간 복잡도**: O(E) - 휴리스틱에 따라 다름

**공간 복잡도**: O(V)

**특징**:
- f(n) = g(n) + h(n)
- g(n): 시작점에서 현재까지 비용
- h(n): 현재에서 목표까지 추정 비용 (휴리스틱)

**장점**:
- Dijkstra보다 빠름 (좋은 휴리스틱 시)
- 최단 경로 보장 (허용 가능 휴리스틱)

**단점**:
- 휴리스틱 설계 필요
- 메모리 사용량 증가

**활용 예시**:
- 게임 AI 경로 찾기
- 로봇 내비게이션
- 지도 서비스

**난이도**: 높음 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
import java.util.PriorityQueue
import kotlin.math.abs

data class Node(val x: Int, val y: Int)
data class AStarNode(
    val node: Node,
    val g: Int,     // 시작점에서 현재까지 비용
    val f: Int      // g + h (총 추정 비용)
)

// 맨해튼 거리 휴리스틱
fun heuristic(a: Node, b: Node): Int {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

fun aStar(
    grid: Array<IntArray>,
    start: Node,
    goal: Node
): List<Node>? {
    val rows = grid.size
    val cols = grid[0].size
    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    val openSet = PriorityQueue<AStarNode>(compareBy { it.f })
    val cameFrom = mutableMapOf<Node, Node>()
    val gScore = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }

    gScore[start] = 0
    openSet.add(AStarNode(start, 0, heuristic(start, goal)))

    while (openSet.isNotEmpty()) {
        val current = openSet.poll()

        if (current.node == goal) {
            // 경로 복원
            val path = mutableListOf<Node>()
            var node: Node? = goal
            while (node != null) {
                path.add(node)
                node = cameFrom[node]
            }
            return path.reversed()
        }

        for ((dx, dy) in directions) {
            val nx = current.node.x + dx
            val ny = current.node.y + dy
            val neighbor = Node(nx, ny)

            if (nx !in 0 until rows || ny !in 0 until cols) continue
            if (grid[nx][ny] == 1) continue // 장애물

            val tentativeG = gScore.getValue(current.node) + 1

            if (tentativeG < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current.node
                gScore[neighbor] = tentativeG
                val fScore = tentativeG + heuristic(neighbor, goal)
                openSet.add(AStarNode(neighbor, tentativeG, fScore))
            }
        }
    }

    return null // 경로 없음
}
```

**관련 알고리즘**: Dijkstra, BFS, IDA*

---

## 7. Prim (프림)

**목적**: 최소 신장 트리 (MST) 구성

**시간 복잡도**: O(E log V) - 우선순위 큐 사용

**공간 복잡도**: O(V)

**특징**:
- 정점 기반 MST
- 우선순위 큐 사용
- 밀집 그래프에 적합

**장점**:
- 밀집 그래프에서 효율적
- 하나의 트리에서 시작

**단점**:
- 희소 그래프에서 Kruskal보다 느림

**활용 예시**:
- 네트워크 설계
- 클러스터링
- 근사 TSP

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
import java.util.PriorityQueue

data class PrimEdge(val to: Int, val weight: Int)

fun prim(graph: Map<Int, List<PrimEdge>>, n: Int): List<Pair<Int, Int>> {
    val mst = mutableListOf<Pair<Int, Int>>() // (from, to)
    val visited = BooleanArray(n)
    val pq = PriorityQueue<Triple<Int, Int, Int>>(compareBy { it.third }) // (from, to, weight)

    // 0번 정점에서 시작
    visited[0] = true
    graph[0]?.forEach { edge ->
        pq.add(Triple(0, edge.to, edge.weight))
    }

    var totalWeight = 0

    while (pq.isNotEmpty() && mst.size < n - 1) {
        val (from, to, weight) = pq.poll()
        if (visited[to]) continue

        visited[to] = true
        mst.add(from to to)
        totalWeight += weight

        graph[to]?.forEach { edge ->
            if (!visited[edge.to]) {
                pq.add(Triple(to, edge.to, edge.weight))
            }
        }
    }

    return mst
}

// MST 총 가중치 반환
fun primMSTWeight(graph: Map<Int, List<PrimEdge>>, n: Int): Int {
    val visited = BooleanArray(n)
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second }) // (node, weight)

    pq.add(0 to 0)
    var totalWeight = 0

    while (pq.isNotEmpty()) {
        val (node, weight) = pq.poll()
        if (visited[node]) continue

        visited[node] = true
        totalWeight += weight

        graph[node]?.forEach { edge ->
            if (!visited[edge.to]) {
                pq.add(edge.to to edge.weight)
            }
        }
    }

    return totalWeight
}
```

**관련 알고리즘**: Kruskal, Dijkstra

---

## 8. Kruskal (크루스칼)

**목적**: 최소 신장 트리 (MST) 구성

**시간 복잡도**: O(E log E)

**공간 복잡도**: O(V)

**특징**:
- 간선 기반 MST
- Union-Find 사용
- 희소 그래프에 적합

**장점**:
- 희소 그래프에서 효율적
- 구현이 직관적

**단점**:
- 간선 정렬 필요

**활용 예시**:
- 네트워크 설계
- 이미지 분할
- 클러스터링

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
data class KruskalEdge(val from: Int, val to: Int, val weight: Int)

class UnionFind(n: Int) {
    private val parent = IntArray(n) { it }
    private val rank = IntArray(n) { 0 }

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x]) // 경로 압축
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val px = find(x)
        val py = find(y)
        if (px == py) return false

        // 랭크 기반 합치기
        when {
            rank[px] < rank[py] -> parent[px] = py
            rank[px] > rank[py] -> parent[py] = px
            else -> {
                parent[py] = px
                rank[px]++
            }
        }
        return true
    }
}

fun kruskal(edges: List<KruskalEdge>, n: Int): List<KruskalEdge> {
    val mst = mutableListOf<KruskalEdge>()
    val sortedEdges = edges.sortedBy { it.weight }
    val uf = UnionFind(n)

    for (edge in sortedEdges) {
        if (uf.union(edge.from, edge.to)) {
            mst.add(edge)
            if (mst.size == n - 1) break
        }
    }

    return mst
}

// MST 총 가중치 반환
fun kruskalMSTWeight(edges: List<KruskalEdge>, n: Int): Int {
    val sortedEdges = edges.sortedBy { it.weight }
    val uf = UnionFind(n)
    var totalWeight = 0
    var edgeCount = 0

    for (edge in sortedEdges) {
        if (uf.union(edge.from, edge.to)) {
            totalWeight += edge.weight
            edgeCount++
            if (edgeCount == n - 1) break
        }
    }

    return totalWeight
}
```

**관련 알고리즘**: Prim, Union-Find

---

## 9. Topological Sort (위상 정렬)

**목적**: DAG(방향 비순환 그래프)의 정점을 선형 순서로 정렬

**시간 복잡도**: O(V + E)

**공간 복잡도**: O(V)

**특징**:
- DAG에서만 가능
- 의존성 순서 결정
- 여러 유효한 순서 가능

**장점**:
- 의존성 해결에 적합
- 선형 시간 복잡도

**단점**:
- DAG에서만 사용 가능

**활용 예시**:
- 빌드 시스템 (의존성 해결)
- 작업 스케줄링
- 과목 선수 과목 결정

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
import java.util.LinkedList
import java.util.Queue

// Kahn's Algorithm (BFS 기반)
fun topologicalSortKahn(graph: Map<Int, List<Int>>, n: Int): List<Int>? {
    val inDegree = IntArray(n)
    graph.values.flatten().forEach { inDegree[it]++ }

    val queue: Queue<Int> = LinkedList()
    for (i in 0 until n) {
        if (inDegree[i] == 0) queue.add(i)
    }

    val result = mutableListOf<Int>()

    while (queue.isNotEmpty()) {
        val node = queue.poll()
        result.add(node)

        graph[node]?.forEach { neighbor ->
            inDegree[neighbor]--
            if (inDegree[neighbor] == 0) {
                queue.add(neighbor)
            }
        }
    }

    return if (result.size == n) result else null // null = 사이클 존재
}

// DFS 기반
fun topologicalSortDFS(graph: Map<Int, List<Int>>, n: Int): List<Int>? {
    val visited = IntArray(n) // 0: 미방문, 1: 방문중, 2: 완료
    val result = mutableListOf<Int>()

    fun dfs(node: Int): Boolean {
        if (visited[node] == 1) return false // 사이클 발견
        if (visited[node] == 2) return true

        visited[node] = 1
        graph[node]?.forEach { neighbor ->
            if (!dfs(neighbor)) return false
        }
        visited[node] = 2
        result.add(node)
        return true
    }

    for (i in 0 until n) {
        if (visited[i] == 0 && !dfs(i)) return null
    }

    return result.reversed()
}
```

**관련 알고리즘**: DFS, Kahn's Algorithm

---

## 10. Tarjan's SCC (타잔 강결합 요소)

**목적**: 방향 그래프에서 강결합 요소(SCC) 찾기

**시간 복잡도**: O(V + E)

**공간 복잡도**: O(V)

**특징**:
- DFS 기반
- 단일 패스로 SCC 탐지
- low-link 값 활용

**장점**:
- 효율적인 SCC 탐지
- 한 번의 DFS로 완료

**단점**:
- 구현이 복잡

**활용 예시**:
- 소셜 네트워크 분석
- 2-SAT 문제
- 데드락 감지

**난이도**: 높음 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
class TarjanSCC(private val n: Int, private val graph: Map<Int, List<Int>>) {
    private var index = 0
    private val indices = IntArray(n) { -1 }
    private val lowlink = IntArray(n) { -1 }
    private val onStack = BooleanArray(n)
    private val stack = ArrayDeque<Int>()
    private val sccs = mutableListOf<List<Int>>()

    fun findSCCs(): List<List<Int>> {
        for (v in 0 until n) {
            if (indices[v] == -1) {
                strongconnect(v)
            }
        }
        return sccs
    }

    private fun strongconnect(v: Int) {
        indices[v] = index
        lowlink[v] = index
        index++
        stack.addLast(v)
        onStack[v] = true

        graph[v]?.forEach { w ->
            if (indices[w] == -1) {
                strongconnect(w)
                lowlink[v] = minOf(lowlink[v], lowlink[w])
            } else if (onStack[w]) {
                lowlink[v] = minOf(lowlink[v], indices[w])
            }
        }

        // SCC의 루트인 경우
        if (lowlink[v] == indices[v]) {
            val scc = mutableListOf<Int>()
            do {
                val w = stack.removeLast()
                onStack[w] = false
                scc.add(w)
            } while (w != v)
            sccs.add(scc)
        }
    }
}

fun findSCCs(graph: Map<Int, List<Int>>, n: Int): List<List<Int>> {
    return TarjanSCC(n, graph).findSCCs()
}
```

**관련 알고리즘**: Kosaraju, DFS

---

## 11. Kosaraju (코사라주)

**목적**: 방향 그래프에서 강결합 요소(SCC) 찾기

**시간 복잡도**: O(V + E)

**공간 복잡도**: O(V)

**특징**:
- 두 번의 DFS 사용
- 역방향 그래프 활용
- 이해하기 쉬운 구조

**장점**:
- 구현이 직관적
- 이해하기 쉬움

**단점**:
- 두 번의 DFS 필요
- 역방향 그래프 구성 필요

**활용 예시**:
- SCC 찾기
- 그래프 축약

**난이도**: 중간 | **사용 빈도**: ★★☆☆☆

**Kotlin 코드**:
```kotlin
fun kosarajuSCC(graph: Map<Int, List<Int>>, n: Int): List<List<Int>> {
    // 1. 정방향 DFS로 종료 순서 기록
    val visited = BooleanArray(n)
    val finishOrder = mutableListOf<Int>()

    fun dfs1(v: Int) {
        visited[v] = true
        graph[v]?.forEach { w ->
            if (!visited[w]) dfs1(w)
        }
        finishOrder.add(v)
    }

    for (i in 0 until n) {
        if (!visited[i]) dfs1(i)
    }

    // 2. 역방향 그래프 구성
    val reverseGraph = mutableMapOf<Int, MutableList<Int>>()
    graph.forEach { (from, neighbors) ->
        neighbors.forEach { to ->
            reverseGraph.getOrPut(to) { mutableListOf() }.add(from)
        }
    }

    // 3. 역방향 DFS로 SCC 찾기
    visited.fill(false)
    val sccs = mutableListOf<List<Int>>()

    fun dfs2(v: Int, scc: MutableList<Int>) {
        visited[v] = true
        scc.add(v)
        reverseGraph[v]?.forEach { w ->
            if (!visited[w]) dfs2(w, scc)
        }
    }

    for (v in finishOrder.reversed()) {
        if (!visited[v]) {
            val scc = mutableListOf<Int>()
            dfs2(v, scc)
            sccs.add(scc)
        }
    }

    return sccs
}
```

**관련 알고리즘**: Tarjan SCC, DFS

---

## 12. Johnson (존슨)

**목적**: 희소 그래프에서 모든 쌍 최단 경로 (음수 가중치 허용)

**시간 복잡도**: O(V² log V + VE)

**공간 복잡도**: O(V²)

**특징**:
- Bellman-Ford + Dijkstra 조합
- 가중치 재조정(reweighting)으로 음수 가중치 제거

**장점**:
- 희소 그래프에서 Floyd-Warshall보다 빠름
- 음수 가중치 허용

**단점**:
- 구현이 복잡
- 밀집 그래프에서는 비효율적

**활용 예시**:
- 희소 그래프의 모든 쌍 최단 경로
- 음수 가중치가 있는 네트워크

**난이도**: 높음 | **사용 빈도**: ★★☆☆☆

**Kotlin 코드**:
```kotlin
import java.util.PriorityQueue

fun johnson(graph: Map<Int, List<Edge>>, n: Int): Array<IntArray>? {
    // 1. 새 정점 추가하고 모든 정점으로 가중치 0인 간선 연결
    val edges = mutableListOf<EdgeBF>()
    graph.forEach { (from, neighbors) ->
        neighbors.forEach { edge ->
            edges.add(EdgeBF(from, edge.to, edge.weight))
        }
    }
    for (v in 0 until n) {
        edges.add(EdgeBF(n, v, 0)) // 새 정점 n에서 모든 정점으로
    }

    // 2. Bellman-Ford로 h 값 계산
    val h = bellmanFord(edges, n + 1, n) ?: return null // 음수 사이클

    // 3. 가중치 재조정
    val reweightedGraph = mutableMapOf<Int, MutableList<Edge>>()
    graph.forEach { (from, neighbors) ->
        neighbors.forEach { edge ->
            val newWeight = edge.weight + h[from] - h[edge.to]
            reweightedGraph.getOrPut(from) { mutableListOf() }
                .add(Edge(edge.to, newWeight))
        }
    }

    // 4. 각 정점에서 Dijkstra 실행
    val dist = Array(n) { IntArray(n) }
    for (u in 0 until n) {
        val d = dijkstra(reweightedGraph, u, n)
        for (v in 0 until n) {
            if (d[v] == Int.MAX_VALUE) {
                dist[u][v] = Int.MAX_VALUE
            } else {
                // 원래 가중치로 복원
                dist[u][v] = d[v] - h[u] + h[v]
            }
        }
    }

    return dist
}
```

**관련 알고리즘**: Floyd-Warshall, Bellman-Ford, Dijkstra

