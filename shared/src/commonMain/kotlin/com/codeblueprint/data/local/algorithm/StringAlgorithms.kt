package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 문자열 알고리즘 데이터 (2개)
 *
 * KMP, Trie
 */
internal object StringAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createKMP(),
        createTrie()
    )

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
}
