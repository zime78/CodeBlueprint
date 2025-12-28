package com.codeblueprint.data.local.algorithm

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity

/**
 * 정렬 알고리즘 데이터 (13개)
 *
 * Bubble, Selection, Insertion, Quick, Merge, Heap,
 * Counting, Radix, Bucket, Shell, Tim, Intro, Tree Sort
 */
internal object SortingAlgorithms {

    fun getAll(): List<Algorithm> = listOf(
        createBubbleSort(),
        createSelectionSort(),
        createInsertionSort(),
        createQuickSort(),
        createMergeSort(),
        createHeapSort(),
        createCountingSort(),
        createRadixSort(),
        createBucketSort(),
        createShellSort(),
        createTimSort(),
        createIntroSort(),
        createTreeSort()
    )

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

    private fun createSelectionSort() = Algorithm(
        id = "selection-sort",
        name = "Selection Sort",
        koreanName = "선택 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "최소값을 찾아 맨 앞과 교환",
        timeComplexity = TimeComplexity(
            best = "O(n²)",
            average = "O(n²)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("제자리 정렬", "불안정 정렬", "비교 횟수 고정"),
        advantages = listOf("구현이 간단", "메모리 사용 최소", "교환 횟수 최소"),
        disadvantages = listOf("항상 O(n²)", "불안정 정렬"),
        useCases = listOf("교환 비용이 높은 경우", "소규모 데이터", "교육용"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun selectionSort(arr: IntArray) {
    val n = arr.size
    for (i in 0 until n - 1) {
        var minIdx = i
        for (j in i + 1 until n) {
            if (arr[j] < arr[minIdx]) {
                minIdx = j
            }
        }
        // swap
        val temp = arr[i]
        arr[i] = arr[minIdx]
        arr[minIdx] = temp
    }
}

fun main() {
    val arr = intArrayOf(64, 25, 12, 22, 11)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    selectionSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "최소값을 선택하여 맨 앞과 교환하는 선택 정렬",
                expectedOutput = """
정렬 전: [64, 25, 12, 22, 11]
정렬 후: [11, 12, 22, 25, 64]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("bubble-sort", "heap-sort"),
        difficulty = Difficulty.LOW,
        frequency = 1
    )

    private fun createInsertionSort() = Algorithm(
        id = "insertion-sort",
        name = "Insertion Sort",
        koreanName = "삽입 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "정렬된 부분에 새 요소를 올바른 위치에 삽입",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n²)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("제자리 정렬", "안정 정렬", "온라인 알고리즘"),
        advantages = listOf("구현이 간단", "거의 정렬된 경우 O(n)", "안정 정렬"),
        disadvantages = listOf("대규모 데이터에서 비효율적"),
        useCases = listOf("거의 정렬된 데이터", "소규모 데이터", "Tim Sort의 부분 알고리즘"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun insertionSort(arr: IntArray) {
    for (i in 1 until arr.size) {
        val key = arr[i]
        var j = i - 1
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j]
            j--
        }
        arr[j + 1] = key
    }
}

fun main() {
    val arr = intArrayOf(12, 11, 13, 5, 6)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    insertionSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "정렬된 부분에 삽입하는 삽입 정렬",
                expectedOutput = """
정렬 전: [12, 11, 13, 5, 6]
정렬 후: [5, 6, 11, 12, 13]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("shell-sort", "tim-sort"),
        difficulty = Difficulty.LOW,
        frequency = 3
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

    private fun createCountingSort() = Algorithm(
        id = "counting-sort",
        name = "Counting Sort",
        koreanName = "계수 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "각 요소의 개수를 세어 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n+k)",
            average = "O(n+k)",
            worst = "O(n+k)"
        ),
        spaceComplexity = "O(k)",
        characteristics = listOf("비교 기반이 아닌 정렬", "안정 정렬", "정수 또는 제한된 범위"),
        advantages = listOf("범위가 작으면 O(n)", "안정 정렬"),
        disadvantages = listOf("범위가 크면 메모리 낭비", "정수만 가능"),
        useCases = listOf("정수 범위가 작은 경우", "Radix Sort의 부분 알고리즘", "나이/점수 정렬"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun countingSort(arr: IntArray): IntArray {
    if (arr.isEmpty()) return arr

    val max = arr.maxOrNull()!!
    val min = arr.minOrNull()!!
    val range = max - min + 1

    val count = IntArray(range)
    val output = IntArray(arr.size)

    // Count occurrences
    for (num in arr) count[num - min]++

    // Cumulative count
    for (i in 1 until range) count[i] += count[i - 1]

    // Build output array
    for (i in arr.size - 1 downTo 0) {
        output[count[arr[i] - min] - 1] = arr[i]
        count[arr[i] - min]--
    }

    return output
}

fun main() {
    val arr = intArrayOf(4, 2, 2, 8, 3, 3, 1)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    val sorted = countingSort(arr)
    println("정렬 후: ${'$'}{sorted.contentToString()}")
}
                """.trimIndent(),
                explanation = "요소 개수를 세어 정렬하는 계수 정렬",
                expectedOutput = """
정렬 전: [4, 2, 2, 8, 3, 3, 1]
정렬 후: [1, 2, 2, 3, 3, 4, 8]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("radix-sort", "bucket-sort"),
        difficulty = Difficulty.LOW,
        frequency = 3
    )

    private fun createRadixSort() = Algorithm(
        id = "radix-sort",
        name = "Radix Sort",
        koreanName = "기수 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "자릿수별로 정렬 반복",
        timeComplexity = TimeComplexity(
            best = "O(d(n+k))",
            average = "O(d(n+k))",
            worst = "O(d(n+k))"
        ),
        spaceComplexity = "O(n+k)",
        characteristics = listOf("비교 기반이 아닌 정렬", "안정 정렬", "자릿수(d)만큼 반복"),
        advantages = listOf("자릿수가 적으면 매우 빠름", "안정 정렬"),
        disadvantages = listOf("자릿수가 많으면 비효율적", "추가 메모리 필요"),
        useCases = listOf("고정 길이 정수/문자열", "전화번호/우편번호 정렬"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun radixSort(arr: IntArray) {
    val max = arr.maxOrNull() ?: return
    var exp = 1

    while (max / exp > 0) {
        countingSortByDigit(arr, exp)
        exp *= 10
    }
}

fun countingSortByDigit(arr: IntArray, exp: Int) {
    val n = arr.size
    val output = IntArray(n)
    val count = IntArray(10)

    for (num in arr) count[(num / exp) % 10]++
    for (i in 1 until 10) count[i] += count[i - 1]

    for (i in n - 1 downTo 0) {
        val digit = (arr[i] / exp) % 10
        output[count[digit] - 1] = arr[i]
        count[digit]--
    }

    for (i in 0 until n) arr[i] = output[i]
}

fun main() {
    val arr = intArrayOf(170, 45, 75, 90, 802, 24, 2, 66)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    radixSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "자릿수별로 정렬하는 기수 정렬",
                expectedOutput = """
정렬 전: [170, 45, 75, 90, 802, 24, 2, 66]
정렬 후: [2, 24, 45, 66, 75, 90, 170, 802]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("counting-sort", "bucket-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createBucketSort() = Algorithm(
        id = "bucket-sort",
        name = "Bucket Sort",
        koreanName = "버킷 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "요소를 버킷에 분배 후 각 버킷 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n+k)",
            average = "O(n+k)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(n+k)",
        characteristics = listOf("분산 정렬 알고리즘", "균등 분포에 효과적", "버킷 내부 정렬 필요"),
        advantages = listOf("균등 분포 시 O(n)", "병렬화 가능"),
        disadvantages = listOf("불균등 분포 시 비효율적", "추가 메모리 필요"),
        useCases = listOf("균등 분포된 실수", "외부 정렬"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun bucketSort(arr: FloatArray) {
    val n = arr.size
    if (n <= 0) return

    val buckets = Array(n) { mutableListOf<Float>() }

    // Put elements into buckets
    for (num in arr) {
        val idx = (n * num).toInt().coerceIn(0, n - 1)
        buckets[idx].add(num)
    }

    // Sort individual buckets
    for (bucket in buckets) bucket.sort()

    // Concatenate buckets
    var idx = 0
    for (bucket in buckets) {
        for (num in bucket) {
            arr[idx++] = num
        }
    }
}

fun main() {
    val arr = floatArrayOf(0.42f, 0.32f, 0.33f, 0.52f, 0.37f, 0.47f, 0.51f)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    bucketSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "버킷에 분배 후 정렬하는 버킷 정렬",
                expectedOutput = """
정렬 전: [0.42, 0.32, 0.33, 0.52, 0.37, 0.47, 0.51]
정렬 후: [0.32, 0.33, 0.37, 0.42, 0.47, 0.51, 0.52]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("counting-sort", "radix-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createShellSort() = Algorithm(
        id = "shell-sort",
        name = "Shell Sort",
        koreanName = "셸 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "간격(gap)을 두고 삽입 정렬 반복",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n^1.5)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(1)",
        characteristics = listOf("삽입 정렬의 개선판", "제자리 정렬", "Gap sequence에 따라 성능 변화"),
        advantages = listOf("Insertion Sort보다 빠름", "제자리 정렬", "구현이 비교적 간단"),
        disadvantages = listOf("Gap sequence 선택이 중요", "최적 성능 증명 어려움"),
        useCases = listOf("중간 크기 데이터", "임베디드 시스템"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
fun shellSort(arr: IntArray) {
    var gap = arr.size / 2

    while (gap > 0) {
        for (i in gap until arr.size) {
            val temp = arr[i]
            var j = i
            while (j >= gap && arr[j - gap] > temp) {
                arr[j] = arr[j - gap]
                j -= gap
            }
            arr[j] = temp
        }
        gap /= 2
    }
}

fun main() {
    val arr = intArrayOf(12, 34, 54, 2, 3)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    shellSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "간격을 두고 삽입 정렬을 반복하는 셸 정렬",
                expectedOutput = """
정렬 전: [12, 34, 54, 2, 3]
정렬 후: [2, 3, 12, 34, 54]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("insertion-sort"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    private fun createTimSort() = Algorithm(
        id = "tim-sort",
        name = "Tim Sort",
        koreanName = "팀 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "Merge Sort + Insertion Sort 하이브리드",
        timeComplexity = TimeComplexity(
            best = "O(n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("하이브리드 정렬", "안정 정렬", "실제 데이터에 최적화"),
        advantages = listOf("실제 데이터 패턴에 효율적", "안정 정렬", "최악에도 O(n log n)"),
        disadvantages = listOf("구현 복잡", "추가 메모리 필요"),
        useCases = listOf("Python, Java의 기본 정렬", "실제 프로덕션 환경"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// Tim Sort는 복잡한 구현이므로 간략화된 버전 제공
// 실제로는 Python, Java, Kotlin의 기본 정렬에 사용됨
const val MIN_RUN = 32

fun insertionSort(arr: IntArray, left: Int, right: Int) {
    for (i in left + 1..right) {
        val key = arr[i]
        var j = i - 1
        while (j >= left && arr[j] > key) {
            arr[j + 1] = arr[j]
            j--
        }
        arr[j + 1] = key
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

fun timSort(arr: IntArray) {
    val n = arr.size
    // Insertion sort for small runs
    var i = 0
    while (i < n) {
        insertionSort(arr, i, minOf(i + MIN_RUN - 1, n - 1))
        i += MIN_RUN
    }
    // Merge runs
    var size = MIN_RUN
    while (size < n) {
        var left = 0
        while (left < n) {
            val mid = left + size - 1
            val right = minOf(left + 2 * size - 1, n - 1)
            if (mid < right) merge(arr, left, mid, right)
            left += 2 * size
        }
        size *= 2
    }
}

fun main() {
    val arr = intArrayOf(5, 21, 7, 23, 19)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    timSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "Merge Sort와 Insertion Sort를 결합한 Tim Sort",
                expectedOutput = """
정렬 전: [5, 21, 7, 23, 19]
정렬 후: [5, 7, 19, 21, 23]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("merge-sort", "insertion-sort"),
        difficulty = Difficulty.HIGH,
        frequency = 5
    )

    private fun createIntroSort() = Algorithm(
        id = "intro-sort",
        name = "Intro Sort",
        koreanName = "인트로 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "Quick Sort + Heap Sort + Insertion Sort 하이브리드",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n log n)"
        ),
        spaceComplexity = "O(log n)",
        characteristics = listOf("하이브리드 정렬", "Quick Sort의 최악 케이스 방지", "C++ STL sort의 기반"),
        advantages = listOf("최악에도 O(n log n) 보장", "실용적 성능"),
        disadvantages = listOf("불안정 정렬", "구현 복잡"),
        useCases = listOf("C++ STL sort()", "성능 보장이 필요한 경우"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
// Intro Sort 간략화 버전
import kotlin.math.log2

fun introSort(arr: IntArray) {
    val maxDepth = (2 * log2(arr.size.toDouble())).toInt()
    introSortHelper(arr, 0, arr.size - 1, maxDepth)
}

fun introSortHelper(arr: IntArray, low: Int, high: Int, depthLimit: Int) {
    if (high - low < 16) {
        // Small array: use insertion sort
        for (i in low + 1..high) {
            val key = arr[i]
            var j = i - 1
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j]
                j--
            }
            arr[j + 1] = key
        }
        return
    }

    if (depthLimit == 0) {
        // Depth limit reached: use heap sort
        heapSort(arr, low, high)
        return
    }

    // Normal case: use quick sort
    val pivot = partition(arr, low, high)
    introSortHelper(arr, low, pivot - 1, depthLimit - 1)
    introSortHelper(arr, pivot + 1, high, depthLimit - 1)
}

fun main() {
    val arr = intArrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    introSort(arr)
    println("정렬 후: ${'$'}{arr.contentToString()}")
}
                """.trimIndent(),
                explanation = "Quick Sort, Heap Sort, Insertion Sort를 결합한 Intro Sort",
                expectedOutput = """
정렬 전: [3, 1, 4, 1, 5, 9, 2, 6, 5]
정렬 후: [1, 1, 2, 3, 4, 5, 5, 6, 9]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("quick-sort", "heap-sort", "insertion-sort"),
        difficulty = Difficulty.HIGH,
        frequency = 4
    )

    private fun createTreeSort() = Algorithm(
        id = "tree-sort",
        name = "Tree Sort",
        koreanName = "트리 정렬",
        category = AlgorithmCategory.SORTING,
        purpose = "BST에 삽입 후 중위 순회로 정렬",
        timeComplexity = TimeComplexity(
            best = "O(n log n)",
            average = "O(n log n)",
            worst = "O(n²)"
        ),
        spaceComplexity = "O(n)",
        characteristics = listOf("BST 기반 정렬", "균형 트리 사용 시 O(n log n) 보장", "온라인 정렬 가능"),
        advantages = listOf("동적 데이터에 유용", "중복 처리 용이"),
        disadvantages = listOf("추가 메모리 필요", "불균형 시 O(n²)"),
        useCases = listOf("동적 데이터 정렬", "BST 학습용"),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class TreeNode(var value: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

class BST {
    var root: TreeNode? = null

    fun insert(value: Int) {
        root = insertRec(root, value)
    }

    private fun insertRec(node: TreeNode?, value: Int): TreeNode {
        if (node == null) return TreeNode(value)
        if (value < node.value) {
            node.left = insertRec(node.left, value)
        } else {
            node.right = insertRec(node.right, value)
        }
        return node
    }

    fun inorderTraversal(): List<Int> {
        val result = mutableListOf<Int>()
        inorderRec(root, result)
        return result
    }

    private fun inorderRec(node: TreeNode?, result: MutableList<Int>) {
        if (node == null) return
        inorderRec(node.left, result)
        result.add(node.value)
        inorderRec(node.right, result)
    }
}

fun treeSort(arr: IntArray): List<Int> {
    val bst = BST()
    for (num in arr) bst.insert(num)
    return bst.inorderTraversal()
}

fun main() {
    val arr = intArrayOf(5, 4, 7, 2, 11)
    println("정렬 전: ${'$'}{arr.contentToString()}")
    val sorted = treeSort(arr)
    println("정렬 후: ${'$'}sorted")
}
                """.trimIndent(),
                explanation = "BST에 삽입 후 중위 순회로 정렬하는 트리 정렬",
                expectedOutput = """
정렬 전: [5, 4, 7, 2, 11]
정렬 후: [2, 4, 5, 7, 11]
                """.trimIndent()
            )
        ),
        relatedAlgorithmIds = listOf("heap-sort", "bfs"),
        difficulty = Difficulty.MEDIUM,
        frequency = 1
    )
}
