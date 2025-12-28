# 문자열 알고리즘 (String Algorithms)

문자열 검색, 매칭, 처리를 위한 알고리즘입니다.

---

## 1. KMP (Knuth-Morris-Pratt)

**목적**: 패턴 문자열을 텍스트에서 효율적으로 검색

**시간 복잡도**: O(n + m)

**공간 복잡도**: O(m)

**특징**:
- 실패 함수(failure function) 사용
- 중복 비교 없음

**장점**:
- 선형 시간 보장
- 전처리로 효율적 검색

**단점**:
- 실패 함수 이해 어려움

**활용 예시**:
- 텍스트 편집기 검색
- DNA 서열 매칭
- 문서 검색

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// 실패 함수 (부분 일치 테이블) 생성
fun computeFailure(pattern: String): IntArray {
    val m = pattern.length
    val failure = IntArray(m)
    var j = 0

    for (i in 1 until m) {
        while (j > 0 && pattern[i] != pattern[j]) {
            j = failure[j - 1]
        }
        if (pattern[i] == pattern[j]) {
            j++
        }
        failure[i] = j
    }

    return failure
}

// KMP 검색 - 모든 매칭 위치 반환
fun kmpSearch(text: String, pattern: String): List<Int> {
    val n = text.length
    val m = pattern.length
    if (m == 0) return emptyList()

    val failure = computeFailure(pattern)
    val matches = mutableListOf<Int>()
    var j = 0

    for (i in 0 until n) {
        while (j > 0 && text[i] != pattern[j]) {
            j = failure[j - 1]
        }
        if (text[i] == pattern[j]) {
            j++
        }
        if (j == m) {
            matches.add(i - m + 1)
            j = failure[j - 1]
        }
    }

    return matches
}

// 첫 번째 매칭만 찾기
fun kmpSearchFirst(text: String, pattern: String): Int {
    val n = text.length
    val m = pattern.length
    if (m == 0) return 0

    val failure = computeFailure(pattern)
    var j = 0

    for (i in 0 until n) {
        while (j > 0 && text[i] != pattern[j]) {
            j = failure[j - 1]
        }
        if (text[i] == pattern[j]) {
            j++
        }
        if (j == m) {
            return i - m + 1
        }
    }

    return -1
}
```

**관련 알고리즘**: Rabin-Karp, Boyer-Moore

---

## 2. Rabin-Karp

**목적**: 해시를 이용한 문자열 패턴 매칭

**시간 복잡도**: O(n + m) 평균, O(nm) 최악

**공간 복잡도**: O(1)

**특징**:
- 롤링 해시 사용
- 여러 패턴 동시 검색에 유리

**장점**:
- 여러 패턴 검색에 효율적
- 구현이 간단

**단점**:
- 해시 충돌 시 느림

**활용 예시**:
- 표절 검사
- 다중 패턴 매칭
- 문서 유사도

**난이도**: 중간 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
fun rabinKarp(text: String, pattern: String): List<Int> {
    val n = text.length
    val m = pattern.length
    if (m > n) return emptyList()

    val base = 256L
    val mod = 1_000_000_007L
    val matches = mutableListOf<Int>()

    // base^(m-1) 계산
    var h = 1L
    repeat(m - 1) { h = (h * base) % mod }

    // 패턴과 첫 번째 윈도우 해시 계산
    var patternHash = 0L
    var windowHash = 0L

    for (i in 0 until m) {
        patternHash = (patternHash * base + pattern[i].code) % mod
        windowHash = (windowHash * base + text[i].code) % mod
    }

    for (i in 0..n - m) {
        if (patternHash == windowHash) {
            // 해시 일치 시 실제 문자열 비교
            if (text.substring(i, i + m) == pattern) {
                matches.add(i)
            }
        }

        // 다음 윈도우 해시 계산 (롤링 해시)
        if (i < n - m) {
            windowHash = ((windowHash - text[i].code * h) * base + text[i + m].code) % mod
            if (windowHash < 0) windowHash += mod
        }
    }

    return matches
}

// 다중 패턴 검색
fun rabinKarpMultiple(text: String, patterns: List<String>): Map<String, List<Int>> {
    val result = mutableMapOf<String, MutableList<Int>>()
    val patternsByLength = patterns.groupBy { it.length }

    val base = 256L
    val mod = 1_000_000_007L

    for ((m, patternGroup) in patternsByLength) {
        if (m > text.length) continue

        // 패턴 해시 계산
        val patternHashes = mutableMapOf<Long, MutableList<String>>()
        for (pattern in patternGroup) {
            var hash = 0L
            for (c in pattern) {
                hash = (hash * base + c.code) % mod
            }
            patternHashes.getOrPut(hash) { mutableListOf() }.add(pattern)
        }

        // h = base^(m-1)
        var h = 1L
        repeat(m - 1) { h = (h * base) % mod }

        // 초기 윈도우 해시
        var windowHash = 0L
        for (i in 0 until m) {
            windowHash = (windowHash * base + text[i].code) % mod
        }

        for (i in 0..text.length - m) {
            patternHashes[windowHash]?.forEach { pattern ->
                if (text.substring(i, i + m) == pattern) {
                    result.getOrPut(pattern) { mutableListOf() }.add(i)
                }
            }

            if (i < text.length - m) {
                windowHash = ((windowHash - text[i].code * h) * base + text[i + m].code) % mod
                if (windowHash < 0) windowHash += mod
            }
        }
    }

    return result
}
```

**관련 알고리즘**: KMP, Boyer-Moore

---

## 3. Boyer-Moore

**목적**: 뒤에서부터 비교하는 효율적인 문자열 검색

**시간 복잡도**: O(n/m) 최선, O(nm) 최악

**공간 복잡도**: O(k) - k: 알파벳 크기

**특징**:
- Bad Character Rule
- Good Suffix Rule
- 실제로 가장 빠름

**장점**:
- 긴 패턴에서 매우 빠름
- 문자 건너뛰기 효과적

**단점**:
- 구현이 복잡

**활용 예시**:
- 텍스트 편집기
- grep 유틸리티

**난이도**: 높음 | **사용 빈도**: ★★★★☆

**Kotlin 코드**:
```kotlin
// Bad Character Rule만 사용한 간단 버전
fun boyerMooreSimple(text: String, pattern: String): List<Int> {
    val n = text.length
    val m = pattern.length
    if (m > n) return emptyList()

    // Bad Character Table
    val badChar = IntArray(256) { -1 }
    for (i in 0 until m) {
        badChar[pattern[i].code] = i
    }

    val matches = mutableListOf<Int>()
    var s = 0 // 시프트

    while (s <= n - m) {
        var j = m - 1

        // 뒤에서부터 비교
        while (j >= 0 && pattern[j] == text[s + j]) {
            j--
        }

        if (j < 0) {
            matches.add(s)
            s += if (s + m < n) m - badChar[text[s + m].code] else 1
        } else {
            s += maxOf(1, j - badChar[text[s + j].code])
        }
    }

    return matches
}

// Good Suffix Rule 포함 완전 버전
fun boyerMooreFull(text: String, pattern: String): List<Int> {
    val n = text.length
    val m = pattern.length
    if (m > n) return emptyList()

    // Bad Character Table
    val badChar = IntArray(256) { -1 }
    for (i in 0 until m) {
        badChar[pattern[i].code] = i
    }

    // Good Suffix Table
    val goodSuffix = IntArray(m + 1)
    val borderPos = IntArray(m + 1)

    preprocessStrongSuffix(pattern, goodSuffix, borderPos)
    preprocessCase2(pattern, goodSuffix, borderPos)

    val matches = mutableListOf<Int>()
    var s = 0

    while (s <= n - m) {
        var j = m - 1

        while (j >= 0 && pattern[j] == text[s + j]) {
            j--
        }

        if (j < 0) {
            matches.add(s)
            s += goodSuffix[0]
        } else {
            s += maxOf(goodSuffix[j + 1], j - badChar[text[s + j].code])
        }
    }

    return matches
}

private fun preprocessStrongSuffix(pattern: String, shift: IntArray, borderPos: IntArray) {
    val m = pattern.length
    var i = m
    var j = m + 1
    borderPos[i] = j

    while (i > 0) {
        while (j <= m && pattern[i - 1] != pattern[j - 1]) {
            if (shift[j] == 0) {
                shift[j] = j - i
            }
            j = borderPos[j]
        }
        i--
        j--
        borderPos[i] = j
    }
}

private fun preprocessCase2(pattern: String, shift: IntArray, borderPos: IntArray) {
    val m = pattern.length
    var j = borderPos[0]

    for (i in 0..m) {
        if (shift[i] == 0) {
            shift[i] = j
        }
        if (i == j) {
            j = borderPos[j]
        }
    }
}
```

**관련 알고리즘**: KMP, Rabin-Karp

---

## 4. Z Algorithm (Z 알고리즘)

**목적**: 각 위치에서 접두사와 일치하는 최대 길이 계산

**시간 복잡도**: O(n)

**공간 복잡도**: O(n)

**특징**:
- Z 배열 생성
- 패턴 매칭에 활용

**장점**:
- 선형 시간 복잡도
- 이해하기 쉬움

**단점**:
- 추가 공간 필요

**활용 예시**:
- 문자열 매칭
- 가장 긴 반복 부분문자열

**난이도**: 중간 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
// Z 배열 계산
fun zArray(s: String): IntArray {
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

// Z 알고리즘으로 패턴 매칭
fun zSearch(text: String, pattern: String): List<Int> {
    val concat = "$pattern$$text"
    val z = zArray(concat)
    val m = pattern.length
    val matches = mutableListOf<Int>()

    for (i in m + 1 until concat.length) {
        if (z[i] == m) {
            matches.add(i - m - 1)
        }
    }

    return matches
}

// 가장 긴 반복 부분문자열
fun longestRepeatedSubstring(s: String): String {
    var longest = ""

    for (i in 1 until s.length) {
        val z = zArray(s.substring(i))

        for (j in z.indices) {
            if (z[j] > longest.length) {
                longest = s.substring(i, i + z[j])
            }
        }
    }

    return longest
}
```

**관련 알고리즘**: KMP, Suffix Array

---

## 5. Suffix Array (접미사 배열)

**목적**: 모든 접미사를 사전순으로 정렬한 배열

**시간 복잡도**: O(n log n) 또는 O(n)

**공간 복잡도**: O(n)

**특징**:
- 문자열의 모든 접미사 인덱스
- LCP 배열과 함께 사용

**장점**:
- 다양한 문자열 문제 해결
- Suffix Tree보다 공간 효율적

**단점**:
- 구현이 복잡

**활용 예시**:
- 문자열 검색
- 가장 긴 반복 부분문자열
- 문자열 비교

**난이도**: 높음 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
// O(n log² n) 구현
fun suffixArray(s: String): IntArray {
    val n = s.length
    var suffix = s.indices.toList()
    var rank = IntArray(n) { s[it].code }
    var tmp = IntArray(n)
    var k = 1

    while (k < n) {
        val finalRank = rank
        val finalK = k

        suffix = suffix.sortedWith { a, b ->
            if (finalRank[a] != finalRank[b]) {
                finalRank[a] - finalRank[b]
            } else {
                val ra = if (a + finalK < n) finalRank[a + finalK] else -1
                val rb = if (b + finalK < n) finalRank[b + finalK] else -1
                ra - rb
            }
        }

        tmp[suffix[0]] = 0
        for (i in 1 until n) {
            val prev = suffix[i - 1]
            val curr = suffix[i]

            tmp[curr] = tmp[prev]

            val prevRank2 = if (prev + k < n) rank[prev + k] else -1
            val currRank2 = if (curr + k < n) rank[curr + k] else -1

            if (rank[prev] != rank[curr] || prevRank2 != currRank2) {
                tmp[curr]++
            }
        }

        rank = tmp.copyOf()
        k *= 2
    }

    return suffix.toIntArray()
}

// LCP 배열 (Kasai 알고리즘)
fun lcpArray(s: String, sa: IntArray): IntArray {
    val n = s.length
    val rank = IntArray(n)
    val lcp = IntArray(n)

    for (i in 0 until n) {
        rank[sa[i]] = i
    }

    var k = 0
    for (i in 0 until n) {
        if (rank[i] == 0) {
            k = 0
            continue
        }

        val j = sa[rank[i] - 1]
        while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
            k++
        }

        lcp[rank[i]] = k
        if (k > 0) k--
    }

    return lcp
}

// 접미사 배열로 패턴 검색
fun searchWithSuffixArray(text: String, pattern: String, sa: IntArray): Int {
    var left = 0
    var right = text.length - 1

    while (left <= right) {
        val mid = (left + right) / 2
        val suffix = text.substring(sa[mid])

        when {
            suffix.startsWith(pattern) -> return sa[mid]
            suffix < pattern -> left = mid + 1
            else -> right = mid - 1
        }
    }

    return -1
}
```

**관련 알고리즘**: Suffix Tree, LCP Array

---

## 6. Trie (트라이)

**목적**: 문자열 집합의 효율적인 저장과 검색

**시간 복잡도**: O(m) - m: 문자열 길이

**공간 복잡도**: O(ALPHABET * n * m)

**특징**:
- 접두사 트리
- 공통 접두사 공유

**장점**:
- 빠른 검색 및 접두사 매칭
- 자동 완성에 적합

**단점**:
- 메모리 사용량 많음

**활용 예시**:
- 자동 완성
- 사전 구현
- IP 라우팅

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
class TrieNode {
    val children = mutableMapOf<Char, TrieNode>()
    var isEndOfWord = false
    var count = 0 // 해당 노드를 지나는 단어 수
}

class Trie {
    private val root = TrieNode()

    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.getOrPut(char) { TrieNode() }
            node.count++
        }
        node.isEndOfWord = true
    }

    fun search(word: String): Boolean {
        var node = root
        for (char in word) {
            node = node.children[char] ?: return false
        }
        return node.isEndOfWord
    }

    fun startsWith(prefix: String): Boolean {
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return false
        }
        return true
    }

    fun countWithPrefix(prefix: String): Int {
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return 0
        }
        return node.count
    }

    // 자동 완성
    fun autoComplete(prefix: String, limit: Int = 10): List<String> {
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return emptyList()
        }

        val results = mutableListOf<String>()
        val sb = StringBuilder(prefix)

        fun dfs(current: TrieNode) {
            if (results.size >= limit) return

            if (current.isEndOfWord) {
                results.add(sb.toString())
            }

            for ((char, child) in current.children) {
                sb.append(char)
                dfs(child)
                sb.deleteCharAt(sb.length - 1)
            }
        }

        dfs(node)
        return results
    }

    // 삭제
    fun delete(word: String): Boolean {
        fun deleteRec(node: TrieNode, index: Int): Boolean {
            if (index == word.length) {
                if (!node.isEndOfWord) return false
                node.isEndOfWord = false
                node.count--
                return node.children.isEmpty()
            }

            val char = word[index]
            val child = node.children[char] ?: return false

            val shouldDelete = deleteRec(child, index + 1)
            if (shouldDelete) {
                node.children.remove(char)
                node.count--
                return node.children.isEmpty() && !node.isEndOfWord
            }

            node.count--
            return false
        }

        return deleteRec(root, 0) || search(word).also { if (!it) return false }
    }
}
```

**관련 알고리즘**: Suffix Tree, Aho-Corasick

---

## 7. Aho-Corasick

**목적**: 여러 패턴을 동시에 검색

**시간 복잡도**: O(n + m + z) - z: 매칭 수

**공간 복잡도**: O(m * ALPHABET)

**특징**:
- Trie + Failure Function
- 유한 상태 기계

**장점**:
- 다중 패턴 매칭에 최적
- 선형 시간 보장

**단점**:
- 구현이 복잡
- 메모리 사용량

**활용 예시**:
- 검열 필터
- 바이러스 스캔
- 네트워크 침입 탐지

**난이도**: 높음 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
import java.util.LinkedList
import java.util.Queue

class AhoCorasick {
    class Node {
        val children = mutableMapOf<Char, Node>()
        var failure: Node? = null
        val output = mutableListOf<String>()
    }

    private val root = Node()

    fun addPattern(pattern: String) {
        var node = root
        for (char in pattern) {
            node = node.children.getOrPut(char) { Node() }
        }
        node.output.add(pattern)
    }

    fun build() {
        val queue: Queue<Node> = LinkedList()

        // 깊이 1 노드들의 failure는 root
        for (child in root.children.values) {
            child.failure = root
            queue.add(child)
        }

        // BFS로 failure 링크 구성
        while (queue.isNotEmpty()) {
            val current = queue.poll()

            for ((char, child) in current.children) {
                queue.add(child)

                var failure = current.failure
                while (failure != null && char !in failure.children) {
                    failure = failure.failure
                }

                child.failure = failure?.children?.get(char) ?: root
                child.output.addAll(child.failure!!.output)
            }
        }
    }

    fun search(text: String): List<Pair<Int, String>> {
        val results = mutableListOf<Pair<Int, String>>()
        var node = root

        for (i in text.indices) {
            val char = text[i]

            while (node != root && char !in node.children) {
                node = node.failure!!
            }

            node = node.children[char] ?: root

            for (pattern in node.output) {
                results.add((i - pattern.length + 1) to pattern)
            }
        }

        return results
    }
}

// 사용 예시
fun ahoCorasickExample() {
    val ac = AhoCorasick()
    ac.addPattern("he")
    ac.addPattern("she")
    ac.addPattern("his")
    ac.addPattern("hers")
    ac.build()

    val text = "ushers"
    val matches = ac.search(text)
    // [(1, "she"), (2, "he"), (2, "hers")]
}
```

**관련 알고리즘**: KMP, Trie

