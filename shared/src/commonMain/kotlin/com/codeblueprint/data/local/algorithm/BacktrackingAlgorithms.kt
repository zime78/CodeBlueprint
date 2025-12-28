package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 백트래킹 알고리즘 데이터 (5개)
 *
 * N-Queens, Sudoku Solver, Graph Coloring, Hamiltonian Cycle, Subset Sum
 */
internal object BacktrackingAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createNQueens(),
        createSudokuSolver(),
        createGraphColoring(),
        createHamiltonianCycle(),
        createSubsetSum()
    )

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

    private fun createSudokuSolver() = Algorithm(
        id = "sudoku-solver",
        name = "Sudoku Solver",
        koreanName = "스도쿠 풀이",
        category = AlgorithmCategory.BACKTRACKING,
        purpose = "9×9 스도쿠 퍼즐을 백트래킹으로 해결",
        timeComplexity = TimeComplexity(
            best = "O(1)",
            average = "O(9^m)",
            worst = "O(9^81)"
        ),
        spaceComplexity = "O(81)",
        characteristics = listOf("제약 전파 + 백트래킹", "빈 칸 수(m)에 따라 성능 결정"),
        advantages = listOf("모든 스도쿠 퍼즐 해결 가능", "제약 전파로 탐색 공간 축소"),
        disadvantages = listOf("최악의 경우 지수적 시간", "단순 백트래킹은 비효율적"),
        useCases = listOf("퍼즐 게임", "제약 만족 문제(CSP)", "논리 추론"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun solveSudoku(board: Array<IntArray>): Boolean {
    for (row in 0 until 9) {
        for (col in 0 until 9) {
            if (board[row][col] == 0) {
                for (num in 1..9) {
                    if (isValid(board, row, col, num)) {
                        board[row][col] = num
                        if (solveSudoku(board)) return true
                        board[row][col] = 0  // 백트래킹
                    }
                }
                return false  // 유효한 숫자 없음
            }
        }
    }
    return true  // 모든 칸 채움
}

fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
    // 행 검사
    if (num in board[row]) return false
    // 열 검사
    for (r in 0 until 9) if (board[r][col] == num) return false
    // 3x3 박스 검사
    val boxRow = (row / 3) * 3
    val boxCol = (col / 3) * 3
    for (r in boxRow until boxRow + 3) {
        for (c in boxCol until boxCol + 3) {
            if (board[r][c] == num) return false
        }
    }
    return true
}

fun main() {
    val board = arrayOf(
        intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
        intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
        intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
        intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
        intArrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
        intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
        intArrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
        intArrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
        intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
    )

    if (solveSudoku(board)) {
        println("스도쿠 해결!")
        board.forEach { row -> println(row.joinToString(" ")) }
    } else {
        println("해결 불가")
    }
}
                """.trimIndent(),
                explanation = "백트래킹으로 스도쿠 퍼즐 해결. 빈 칸에 1-9를 시도하며 유효성 검사",
                expectedOutput = """
스도쿠 해결!
5 3 4 6 7 8 9 1 2
6 7 2 1 9 5 3 4 8
1 9 8 3 4 2 5 6 7
8 5 9 7 6 1 4 2 3
4 2 6 8 5 3 7 9 1
7 1 3 9 2 4 8 5 6
9 6 1 5 3 7 2 8 4
2 8 7 4 1 9 6 3 5
3 4 5 2 8 6 1 7 9
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("n-queens", "graph-coloring"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createGraphColoring() = Algorithm(
        id = "graph-coloring",
        name = "Graph Coloring",
        koreanName = "그래프 색칠",
        category = AlgorithmCategory.BACKTRACKING,
        purpose = "인접한 정점이 같은 색을 갖지 않도록 그래프의 정점에 색상 할당",
        timeComplexity = TimeComplexity(
            best = "O(V)",
            average = "O(m^V)",
            worst = "O(m^V)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("NP-Complete 문제", "m: 색상 수, V: 정점 수"),
        advantages = listOf("다양한 스케줄링 문제에 적용", "최소 색상 수 탐색 가능"),
        disadvantages = listOf("지수적 시간 복잡도", "큰 그래프에서 비효율적"),
        useCases = listOf("스케줄링", "레지스터 할당", "지도 색칠", "주파수 할당"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun graphColoring(graph: Array<IntArray>, m: Int): IntArray? {
    val n = graph.size
    val colors = IntArray(n) { 0 }  // 0은 미할당

    fun isSafe(vertex: Int, color: Int): Boolean {
        for (neighbor in 0 until n) {
            if (graph[vertex][neighbor] == 1 && colors[neighbor] == color) {
                return false
            }
        }
        return true
    }

    fun solve(vertex: Int): Boolean {
        if (vertex == n) return true  // 모든 정점 색칠 완료

        for (color in 1..m) {
            if (isSafe(vertex, color)) {
                colors[vertex] = color
                if (solve(vertex + 1)) return true
                colors[vertex] = 0  // 백트래킹
            }
        }
        return false
    }

    return if (solve(0)) colors else null
}

fun main() {
    // 4개 정점의 그래프 (인접 행렬)
    val graph = arrayOf(
        intArrayOf(0, 1, 1, 1),
        intArrayOf(1, 0, 1, 0),
        intArrayOf(1, 1, 0, 1),
        intArrayOf(1, 0, 1, 0)
    )

    val m = 3  // 사용할 색상 수
    val result = graphColoring(graph, m)

    if (result != null) {
        println("${'$'}m개 색상으로 색칠 가능!")
        result.forEachIndexed { v, c -> println("정점 ${'$'}v: 색상 ${'$'}c") }
    } else {
        println("${'$'}m개 색상으로 색칠 불가능")
    }
}
                """.trimIndent(),
                explanation = "백트래킹으로 그래프 색칠. 각 정점에 색상 할당 후 인접 정점과 충돌 검사",
                expectedOutput = """
3개 색상으로 색칠 가능!
정점 0: 색상 1
정점 1: 색상 2
정점 2: 색상 3
정점 3: 색상 2
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("n-queens", "sudoku-solver", "hamiltonian-cycle"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createHamiltonianCycle() = Algorithm(
        id = "hamiltonian-cycle",
        name = "Hamiltonian Cycle",
        koreanName = "해밀턴 순환",
        category = AlgorithmCategory.BACKTRACKING,
        purpose = "그래프의 모든 정점을 정확히 한 번씩 방문하고 시작점으로 돌아오는 경로 탐색",
        timeComplexity = TimeComplexity(
            best = "O(V)",
            average = "O(V!)",
            worst = "O(V!)"
        ),
        spaceComplexity = "O(V)",
        characteristics = listOf("NP-Complete 문제", "TSP(외판원 문제)의 기본 형태"),
        advantages = listOf("경로 존재 여부 확인", "모든 가능한 순환 탐색 가능"),
        disadvantages = listOf("팩토리얼 시간 복잡도", "대규모 그래프에서 사용 불가"),
        useCases = listOf("외판원 문제(TSP)", "경로 계획", "회로 설계", "DNA 서열 분석"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun findHamiltonianCycle(graph: Array<IntArray>): List<Int>? {
    val n = graph.size
    val path = mutableListOf<Int>()
    val visited = BooleanArray(n)

    fun isSafe(v: Int, pos: Int): Boolean {
        // 이전 정점과 연결되어 있는지 확인
        if (graph[path[pos - 1]][v] == 0) return false
        // 이미 방문했는지 확인
        if (visited[v]) return false
        return true
    }

    fun solve(pos: Int): Boolean {
        if (pos == n) {
            // 마지막 정점이 시작 정점과 연결되어 있는지 확인
            return graph[path[pos - 1]][path[0]] == 1
        }

        for (v in 1 until n) {
            if (isSafe(v, pos)) {
                path.add(v)
                visited[v] = true

                if (solve(pos + 1)) return true

                path.removeAt(path.lastIndex)  // 백트래킹
                visited[v] = false
            }
        }
        return false
    }

    // 시작 정점 0 추가
    path.add(0)
    visited[0] = true

    return if (solve(1)) {
        path + 0  // 시작점으로 돌아감
    } else {
        null
    }
}

fun main() {
    // 5개 정점의 그래프 (인접 행렬)
    val graph = arrayOf(
        intArrayOf(0, 1, 0, 1, 0),
        intArrayOf(1, 0, 1, 1, 1),
        intArrayOf(0, 1, 0, 0, 1),
        intArrayOf(1, 1, 0, 0, 1),
        intArrayOf(0, 1, 1, 1, 0)
    )

    val cycle = findHamiltonianCycle(graph)

    if (cycle != null) {
        println("해밀턴 순환 존재!")
        println("경로: ${'$'}{cycle.joinToString(" -> ")}")
    } else {
        println("해밀턴 순환 없음")
    }
}
                """.trimIndent(),
                explanation = "백트래킹으로 해밀턴 순환 탐색. 모든 정점을 한 번씩 방문하고 시작점으로 복귀",
                expectedOutput = """
해밀턴 순환 존재!
경로: 0 -> 1 -> 2 -> 4 -> 3 -> 0
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("graph-coloring", "dfs"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    private fun createSubsetSum() = Algorithm(
        id = "subset-sum",
        name = "Subset Sum",
        koreanName = "부분집합 합",
        category = AlgorithmCategory.BACKTRACKING,
        purpose = "집합에서 합이 목표값이 되는 부분집합 탐색",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(2^n)",
            worst = "O(2^n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("NP-Complete 문제", "DP로도 해결 가능 - O(nS)"),
        advantages = listOf("모든 해 탐색 가능", "가지치기로 최적화 가능"),
        disadvantages = listOf("지수적 시간 복잡도", "목표값이 크면 DP가 비효율적"),
        useCases = listOf("자원 분배", "예산 배분", "암호학", "결정 문제"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun findSubsetSum(
    nums: IntArray,
    target: Int
): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val current = mutableListOf<Int>()

    fun backtrack(index: Int, sum: Int) {
        if (sum == target) {
            result.add(current.toList())
            return
        }
        if (sum > target || index >= nums.size) return

        for (i in index until nums.size) {
            current.add(nums[i])
            backtrack(i + 1, sum + nums[i])
            current.removeAt(current.lastIndex)  // 백트래킹
        }
    }

    backtrack(0, 0)
    return result
}

// DP 방식 (존재 여부만 확인)
fun hasSubsetSumDP(nums: IntArray, target: Int): Boolean {
    val dp = BooleanArray(target + 1)
    dp[0] = true

    for (num in nums) {
        for (j in target downTo num) {
            dp[j] = dp[j] || dp[j - num]
        }
    }
    return dp[target]
}

fun main() {
    val nums = intArrayOf(3, 34, 4, 12, 5, 2)
    val target = 9

    // 백트래킹 방식
    val subsets = findSubsetSum(nums, target)
    println("합이 ${'$'}target인 부분집합:")
    subsets.forEach { println(it) }

    // DP 방식
    val exists = hasSubsetSumDP(nums, target)
    println("\nDP 결과: ${'$'}{if (exists) "존재" else "없음"}")
}
                """.trimIndent(),
                explanation = "백트래킹과 DP 두 가지 방식으로 부분집합 합 문제 해결",
                expectedOutput = """
합이 9인 부분집합:
[3, 4, 2]
[4, 5]

DP 결과: 존재
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("knapsack", "combination-sum"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )
}
