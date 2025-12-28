package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 백트래킹 알고리즘 데이터 (1개)
 *
 * N-Queens
 */
internal object BacktrackingAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createNQueens()
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
}
