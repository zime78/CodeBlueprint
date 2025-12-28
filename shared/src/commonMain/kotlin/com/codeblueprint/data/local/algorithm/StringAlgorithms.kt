package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 문자열 알고리즘 데이터 (7개)
 *
 * KMP, Trie, Rabin-Karp, Boyer-Moore, Z Algorithm, Suffix Array, Aho-Corasick
 */
internal object StringAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createKMP(),
        createTrie(),
        createRabinKarp(),
        createBoyerMoore(),
        createZAlgorithm(),
        createSuffixArray(),
        createAhoCorasick()
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

    private fun createRabinKarp() = Algorithm(
        id = "rabin-karp",
        name = "Rabin-Karp Algorithm",
        koreanName = "라빈-카프 알고리즘",
        category = AlgorithmCategory.STRING,
        purpose = "롤링 해시를 사용한 효율적인 문자열 패턴 매칭",
        timeComplexity = TimeComplexity(
            best = "O(n+m)",
            average = "O(n+m)",
            worst = "O(nm)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("롤링 해시 사용", "다중 패턴 매칭 가능"),
        advantages = listOf("다중 패턴 검색에 효율적", "구현이 비교적 간단"),
        disadvantages = listOf("해시 충돌 시 최악 성능", "좋은 해시 함수 필요"),
        useCases = listOf("표절 검사", "다중 패턴 매칭", "DNA 서열 분석"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun rabinKarp(text: String, pattern: String): List<Int> {
    val result = mutableListOf<Int>()
    val n = text.length
    val m = pattern.length
    if (m > n) return result

    val d = 256  // 알파벳 크기
    val q = 101  // 소수 (해시 충돌 방지)
    var h = 1    // d^(m-1) % q

    // h 계산: d^(m-1) % q
    for (i in 0 until m - 1) {
        h = (h * d) % q
    }

    // 초기 해시값 계산
    var patternHash = 0
    var textHash = 0
    for (i in 0 until m) {
        patternHash = (d * patternHash + pattern[i].code) % q
        textHash = (d * textHash + text[i].code) % q
    }

    // 슬라이딩 윈도우로 검색
    for (i in 0..n - m) {
        // 해시가 일치하면 실제 문자열 비교
        if (patternHash == textHash) {
            if (text.substring(i, i + m) == pattern) {
                result.add(i)
            }
        }

        // 다음 윈도우의 해시 계산 (롤링 해시)
        if (i < n - m) {
            textHash = (d * (textHash - text[i].code * h) + text[i + m].code) % q
            if (textHash < 0) textHash += q
        }
    }
    return result
}

fun main() {
    val text = "ABABDABACDABABCABAB"
    val pattern = "ABAB"
    println("텍스트: ${'$'}text")
    println("패턴: ${'$'}pattern")
    val positions = rabinKarp(text, pattern)
    println("패턴 발견 위치: ${'$'}{positions.joinToString()}")
}
                """.trimIndent(),
                explanation = "롤링 해시를 사용한 Rabin-Karp 문자열 검색",
                expectedOutput = """
텍스트: ABABDABACDABABCABAB
패턴: ABAB
패턴 발견 위치: 0, 10, 15
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("kmp", "boyer-moore"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createBoyerMoore() = Algorithm(
        id = "boyer-moore",
        name = "Boyer-Moore Algorithm",
        koreanName = "보이어-무어 알고리즘",
        category = AlgorithmCategory.STRING,
        purpose = "오른쪽에서 왼쪽으로 비교하는 효율적인 문자열 검색",
        timeComplexity = TimeComplexity(
            best = "O(n/m)",
            average = "O(n)",
            worst = "O(nm)"
        ),
        spaceComplexity = "O(k)",
        characteristics = listOf("Bad Character Rule", "Good Suffix Rule", "오른쪽에서 왼쪽 비교"),
        advantages = listOf("실제 텍스트에서 매우 빠름", "긴 패턴에서 효율적"),
        disadvantages = listOf("전처리 테이블 필요", "구현 복잡도 높음"),
        useCases = listOf("텍스트 편집기", "grep 유틸리티", "대용량 텍스트 검색"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun boyerMoore(text: String, pattern: String): List<Int> {
    val result = mutableListOf<Int>()
    val n = text.length
    val m = pattern.length
    if (m > n) return result

    // Bad Character 테이블 생성
    val badChar = IntArray(256) { -1 }
    for (i in 0 until m) {
        badChar[pattern[i].code] = i
    }

    var s = 0  // 텍스트에서 패턴 시작 위치
    while (s <= n - m) {
        var j = m - 1

        // 오른쪽에서 왼쪽으로 비교
        while (j >= 0 && pattern[j] == text[s + j]) {
            j--
        }

        if (j < 0) {
            // 패턴 발견
            result.add(s)
            // 다음 위치로 이동
            s += if (s + m < n) m - badChar[text[s + m].code] else 1
        } else {
            // Bad Character Rule 적용
            s += maxOf(1, j - badChar[text[s + j].code])
        }
    }
    return result
}

fun main() {
    val text = "ABAAABCDABCABCABC"
    val pattern = "ABC"
    println("텍스트: ${'$'}text")
    println("패턴: ${'$'}pattern")
    val positions = boyerMoore(text, pattern)
    println("패턴 발견 위치: ${'$'}{positions.joinToString()}")
    println("발견 개수: ${'$'}{positions.size}")
}
                """.trimIndent(),
                explanation = "Bad Character Rule을 사용한 Boyer-Moore 알고리즘",
                expectedOutput = """
텍스트: ABAAABCDABCABCABC
패턴: ABC
패턴 발견 위치: 4, 8, 11, 14
발견 개수: 4
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("kmp", "rabin-karp"),
        difficulty = Difficulty.HIGH,
        frequency = 4
    )

    private fun createZAlgorithm() = Algorithm(
        id = "z-algorithm",
        name = "Z Algorithm",
        koreanName = "Z 알고리즘",
        category = AlgorithmCategory.STRING,
        purpose = "Z 배열을 사용한 선형 시간 문자열 매칭",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n)",
            worst = "O(n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("Z 배열 생성", "접두사 매칭 활용"),
        advantages = listOf("선형 시간 보장", "구현이 비교적 간단"),
        disadvantages = listOf("추가 메모리 필요"),
        useCases = listOf("문자열 매칭", "가장 긴 반복 부분문자열", "문자열 압축"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun buildZArray(s: String): IntArray {
    val n = s.length
    val z = IntArray(n)
    var l = 0
    var r = 0

    for (i in 1 until n) {
        if (i < r) {
            z[i] = minOf(r - i, z[i - l])
        }
        while (i + z[i] < n && s[z[i]] == s[i + z[i]]) {
            z[i]++
        }
        if (i + z[i] > r) {
            l = i
            r = i + z[i]
        }
    }
    return z
}

fun zSearch(text: String, pattern: String): List<Int> {
    val concat = pattern + "${'$'}" + text
    val z = buildZArray(concat)
    val result = mutableListOf<Int>()

    for (i in pattern.length + 1 until concat.length) {
        if (z[i] == pattern.length) {
            result.add(i - pattern.length - 1)
        }
    }
    return result
}

fun main() {
    val text = "AABXAABXCAABXAABXAY"
    val pattern = "AABX"
    println("텍스트: ${'$'}text")
    println("패턴: ${'$'}pattern")

    val concat = pattern + "${'$'}" + text
    val zArray = buildZArray(concat)
    println("Z 배열: ${'$'}{zArray.toList()}")

    val positions = zSearch(text, pattern)
    println("패턴 발견 위치: ${'$'}{positions.joinToString()}")
}
                """.trimIndent(),
                explanation = "Z 배열을 활용한 문자열 패턴 매칭",
                expectedOutput = """
텍스트: AABXAABXCAABXAABXAY
패턴: AABX
Z 배열: [0, 1, 0, 0, 0, 4, 1, 0, 4, 0, 0, 0, 0, 4, 1, 0, 4, 1, 0, 0, 0, 1, 0, 0]
패턴 발견 위치: 0, 4, 9, 13
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("kmp", "suffix-array"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createSuffixArray() = Algorithm(
        id = "suffix-array",
        name = "Suffix Array",
        koreanName = "접미사 배열",
        category = AlgorithmCategory.STRING,
        purpose = "문자열의 모든 접미사를 정렬하여 효율적인 검색 지원",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("모든 접미사 인덱스 정렬", "이진 검색 가능"),
        advantages = listOf("공간 효율적 (Suffix Tree 대비)", "다양한 문자열 문제 해결"),
        disadvantages = listOf("구축 시간 필요", "구현 복잡도"),
        useCases = listOf("문자열 검색", "가장 긴 반복 부분문자열", "데이터 압축"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun buildSuffixArray(s: String): IntArray {
    val n = s.length
    // (접미사 인덱스, 접미사 문자열) 쌍으로 정렬
    return (0 until n)
        .sortedWith(compareBy { s.substring(it) })
        .toIntArray()
}

fun searchWithSuffixArray(text: String, pattern: String, sa: IntArray): List<Int> {
    val result = mutableListOf<Int>()
    val n = text.length
    val m = pattern.length

    // 이진 검색으로 패턴 찾기
    var lo = 0
    var hi = n - 1

    while (lo <= hi) {
        val mid = (lo + hi) / 2
        val suffix = text.substring(sa[mid])
        val cmp = suffix.take(m).compareTo(pattern)

        when {
            cmp < 0 -> lo = mid + 1
            cmp > 0 -> hi = mid - 1
            else -> {
                // 패턴 발견, 연속된 매칭 찾기
                var left = mid
                while (left >= 0 && text.substring(sa[left]).startsWith(pattern)) {
                    result.add(sa[left])
                    left--
                }
                var right = mid + 1
                while (right < n && text.substring(sa[right]).startsWith(pattern)) {
                    result.add(sa[right])
                    right++
                }
                break
            }
        }
    }
    return result.sorted()
}

fun main() {
    val text = "banana"
    val sa = buildSuffixArray(text)

    println("텍스트: ${'$'}text")
    println("접미사 배열: ${'$'}{sa.toList()}")
    println()
    println("정렬된 접미사:")
    sa.forEachIndexed { idx, i ->
        println("  ${'$'}idx: [${'$'}i] ${'$'}{text.substring(i)}")
    }

    val pattern = "ana"
    val positions = searchWithSuffixArray(text, pattern, sa)
    println("\n패턴 '${'$'}pattern' 발견 위치: ${'$'}positions")
}
                """.trimIndent(),
                explanation = "접미사 배열 구축 및 이진 검색을 통한 패턴 매칭",
                expectedOutput = """
텍스트: banana
접미사 배열: [5, 3, 1, 0, 4, 2]

정렬된 접미사:
  0: [5] a
  1: [3] ana
  2: [1] anana
  3: [0] banana
  4: [4] na
  5: [2] nana

패턴 'ana' 발견 위치: [1, 3]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("z-algorithm", "kmp", "suffix-tree"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )

    private fun createAhoCorasick() = Algorithm(
        id = "aho-corasick",
        name = "Aho-Corasick Algorithm",
        koreanName = "아호-코라식 알고리즘",
        category = AlgorithmCategory.STRING,
        purpose = "여러 패턴을 동시에 효율적으로 검색",
        timeComplexity = TimeComplexity(
            best = "O(n+m+z)",
            average = "O(n+m+z)",
            worst = "O(n+m+z)"
        ),
        spaceComplexity = "O(m * ALPHABET)",
        characteristics = listOf("Trie + Failure Function", "다중 패턴 동시 검색"),
        advantages = listOf("다중 패턴 검색에 최적", "선형 시간 보장"),
        disadvantages = listOf("메모리 사용량 많음", "구현 복잡"),
        useCases = listOf("검열 필터", "바이러스 스캔", "키워드 필터링", "네트워크 침입 탐지"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class AhoCorasick {
    private val root = Node()

    class Node {
        val children = mutableMapOf<Char, Node>()
        var fail: Node? = null
        val output = mutableListOf<String>()
    }

    fun addPattern(pattern: String) {
        var node = root
        for (ch in pattern) {
            node = node.children.getOrPut(ch) { Node() }
        }
        node.output.add(pattern)
    }

    fun build() {
        val queue = ArrayDeque<Node>()

        // 루트의 자식들의 실패 링크는 루트
        for (child in root.children.values) {
            child.fail = root
            queue.add(child)
        }

        // BFS로 실패 링크 구축
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()

            for ((ch, child) in curr.children) {
                queue.add(child)

                var fail = curr.fail
                while (fail != null && ch !in fail.children) {
                    fail = fail.fail
                }
                child.fail = fail?.children?.get(ch) ?: root
                child.output.addAll(child.fail!!.output)
            }
        }
    }

    fun search(text: String): List<Pair<Int, String>> {
        val result = mutableListOf<Pair<Int, String>>()
        var node = root

        for ((i, ch) in text.withIndex()) {
            while (node != root && ch !in node.children) {
                node = node.fail!!
            }
            node = node.children[ch] ?: root

            for (pattern in node.output) {
                result.add(i - pattern.length + 1 to pattern)
            }
        }
        return result
    }
}

fun main() {
    val ac = AhoCorasick()
    val patterns = listOf("he", "she", "his", "hers")
    patterns.forEach { ac.addPattern(it) }
    ac.build()

    val text = "ahishers"
    println("텍스트: ${'$'}text")
    println("패턴: ${'$'}patterns")
    println()

    val matches = ac.search(text)
    println("발견된 패턴:")
    matches.forEach { (pos, pattern) ->
        println("  위치 ${'$'}pos: '${'$'}pattern'")
    }
}
                """.trimIndent(),
                explanation = "Trie와 실패 함수를 결합한 다중 패턴 매칭 알고리즘",
                expectedOutput = """
텍스트: ahishers
패턴: [he, she, his, hers]

발견된 패턴:
  위치 1: 'his'
  위치 3: 'she'
  위치 4: 'he'
  위치 4: 'hers'
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("trie", "kmp", "rabin-karp"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )
}
