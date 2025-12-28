package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 탐욕 알고리즘 데이터 (1개)
 *
 * Huffman Coding
 */
internal object GreedyAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createHuffman()
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
}
