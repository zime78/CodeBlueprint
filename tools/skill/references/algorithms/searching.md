# 탐색 알고리즘 (Searching Algorithms)

데이터 집합에서 특정 값을 찾는 알고리즘입니다.

---

## 1. Linear Search (선형 탐색)

**목적**: 처음부터 끝까지 순차적으로 검색

**시간 복잡도**: O(n)

**공간 복잡도**: O(1)

**특징**:
- 가장 단순한 탐색
- 정렬 불필요
- 모든 자료구조에 적용 가능

**장점**:
- 구현이 매우 간단
- 전처리 불필요

**단점**:
- 대규모 데이터에서 비효율적

**활용 예시**:
- 소규모 데이터
- 정렬되지 않은 데이터

**난이도**: 낮음 | **사용 빈도**: ★★★☆☆

**Kotlin 코드**:
```kotlin
fun linearSearch(arr: IntArray, target: Int): Int {
    for (i in arr.indices) {
        if (arr[i] == target) return i
    }
    return -1
}
```

**관련 알고리즘**: Binary Search

---

## 2. Binary Search (이진 탐색)

**목적**: 정렬된 배열에서 중간값 비교로 탐색 범위 절반씩 축소

**시간 복잡도**: O(log n)

**공간 복잡도**: O(1) 반복, O(log n) 재귀

**특징**:
- 분할 정복
- 정렬된 배열 필수
- 매우 효율적

**장점**:
- O(log n) 시간 복잡도
- 대규모 데이터에 효율적

**단점**:
- 정렬된 배열 필요
- 연결 리스트에서 비효율적

**활용 예시**:
- 정렬된 배열 검색
- Lower/Upper Bound 찾기
- 이분 탐색 문제

**난이도**: 낮음 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
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
    return -1
}

// Lower Bound: target 이상인 첫 번째 위치
fun lowerBound(arr: IntArray, target: Int): Int {
    var left = 0
    var right = arr.size

    while (left < right) {
        val mid = left + (right - left) / 2
        if (arr[mid] < target) left = mid + 1
        else right = mid
    }
    return left
}
```

**관련 알고리즘**: Ternary Search, Exponential Search

---

## 3. Jump Search (점프 탐색)

**목적**: 블록 단위로 점프하며 탐색 후 선형 탐색

**시간 복잡도**: O(sqrt(n))

**공간 복잡도**: O(1)

**특징**:
- Linear와 Binary의 중간
- 블록 크기는 sqrt(n)이 최적

**장점**:
- Binary Search보다 후진 이동 적음
- 구현이 비교적 간단

**단점**:
- Binary Search보다 느림

**활용 예시**:
- 후진 이동이 비싼 시스템

**난이도**: 낮음 | **사용 빈도**: ★☆☆☆☆

**Kotlin 코드**:
```kotlin
import kotlin.math.sqrt
import kotlin.math.min

fun jumpSearch(arr: IntArray, target: Int): Int {
    val n = arr.size
    val step = sqrt(n.toDouble()).toInt()
    var prev = 0

    while (arr[min(step, n) - 1] < target) {
        prev = step
        if (prev >= n) return -1
    }

    while (arr[prev] < target) {
        prev++
        if (prev == min(step, n)) return -1
    }

    return if (arr[prev] == target) prev else -1
}
```

**관련 알고리즘**: Binary Search, Linear Search

---

## 4. Interpolation Search (보간 탐색)

**목적**: 값의 위치를 추정하여 탐색

**시간 복잡도**: O(log log n) 평균, O(n) 최악

**공간 복잡도**: O(1)

**특징**:
- 균등 분포 데이터에 최적
- Binary Search의 개선

**장점**:
- 균등 분포 시 매우 빠름

**단점**:
- 불균등 분포 시 O(n)
- 오버플로우 주의 필요

**활용 예시**:
- 균등하게 분포된 정렬 데이터
- 사전 검색

**난이도**: 중간 | **사용 빈도**: ★☆☆☆☆

**Kotlin 코드**:
```kotlin
fun interpolationSearch(arr: IntArray, target: Int): Int {
    var low = 0
    var high = arr.size - 1

    while (low <= high && target >= arr[low] && target <= arr[high]) {
        if (low == high) {
            return if (arr[low] == target) low else -1
        }

        val pos = low + ((target - arr[low]).toLong() * (high - low) /
                        (arr[high] - arr[low])).toInt()

        when {
            arr[pos] == target -> return pos
            arr[pos] < target -> low = pos + 1
            else -> high = pos - 1
        }
    }
    return -1
}
```

**관련 알고리즘**: Binary Search

---

## 5. Exponential Search (지수 탐색)

**목적**: 지수적으로 범위를 확장 후 Binary Search

**시간 복잡도**: O(log n)

**공간 복잡도**: O(1)

**특징**:
- 무한 배열에 적합
- 타겟이 앞쪽에 있으면 빠름

**장점**:
- 무한/대규모 배열에 효과적
- 타겟이 앞쪽일 때 빠름

**단점**:
- Binary Search보다 약간 느림

**활용 예시**:
- 무한 배열
- 크기를 모르는 정렬 배열

**난이도**: 중간 | **사용 빈도**: ★★☆☆☆

**Kotlin 코드**:
```kotlin
fun exponentialSearch(arr: IntArray, target: Int): Int {
    if (arr.isEmpty()) return -1
    if (arr[0] == target) return 0

    var i = 1
    while (i < arr.size && arr[i] <= target) {
        i *= 2
    }

    return binarySearchRange(arr, target, i / 2, minOf(i, arr.size - 1))
}

fun binarySearchRange(arr: IntArray, target: Int, left: Int, right: Int): Int {
    var l = left
    var r = right
    while (l <= r) {
        val mid = l + (r - l) / 2
        when {
            arr[mid] == target -> return mid
            arr[mid] < target -> l = mid + 1
            else -> r = mid - 1
        }
    }
    return -1
}
```

**관련 알고리즘**: Binary Search

---

## 6. Fibonacci Search (피보나치 탐색)

**목적**: 피보나치 수열을 이용한 분할 탐색

**시간 복잡도**: O(log n)

**공간 복잡도**: O(1)

**특징**:
- Binary Search의 변형
- 곱셈/나눗셈 대신 덧셈/뺄셈 사용

**장점**:
- 곱셈/나눗셈 없이 구현 가능
- CPU 캐시에 더 친화적

**단점**:
- 구현이 복잡
- 실제로 Binary Search와 큰 차이 없음

**활용 예시**:
- 하드웨어 최적화
- 곱셈이 비싼 시스템

**난이도**: 중간 | **사용 빈도**: ★☆☆☆☆

**Kotlin 코드**:
```kotlin
fun fibonacciSearch(arr: IntArray, target: Int): Int {
    val n = arr.size
    var fibM2 = 0  // (m-2)'th Fibonacci
    var fibM1 = 1  // (m-1)'th Fibonacci
    var fibM = fibM2 + fibM1  // m'th Fibonacci

    while (fibM < n) {
        fibM2 = fibM1
        fibM1 = fibM
        fibM = fibM2 + fibM1
    }

    var offset = -1

    while (fibM > 1) {
        val i = minOf(offset + fibM2, n - 1)

        when {
            arr[i] < target -> {
                fibM = fibM1
                fibM1 = fibM2
                fibM2 = fibM - fibM1
                offset = i
            }
            arr[i] > target -> {
                fibM = fibM2
                fibM1 -= fibM2
                fibM2 = fibM - fibM1
            }
            else -> return i
        }
    }

    if (fibM1 == 1 && offset + 1 < n && arr[offset + 1] == target) {
        return offset + 1
    }

    return -1
}
```

**관련 알고리즘**: Binary Search

---

## 7. Ternary Search (삼진 탐색)

**목적**: 배열을 3등분하여 탐색

**시간 복잡도**: O(log3 n) = O(log n)

**공간 복잡도**: O(1)

**특징**:
- Binary Search의 변형
- Unimodal 함수의 최대/최소값 찾기에 유용

**장점**:
- 최대/최소값 찾기에 유용
- Unimodal 함수에 적합

**단점**:
- 일반 검색은 Binary Search가 더 효율적

**활용 예시**:
- Unimodal 함수의 극값 찾기
- 최적화 문제

**난이도**: 중간 | **사용 빈도**: ★★☆☆☆

**Kotlin 코드**:
```kotlin
// Unimodal 함수의 최대값 위치 찾기
fun ternarySearchMax(f: (Double) -> Double, left: Double, right: Double, eps: Double = 1e-9): Double {
    var l = left
    var r = right

    while (r - l > eps) {
        val m1 = l + (r - l) / 3
        val m2 = r - (r - l) / 3

        if (f(m1) < f(m2)) l = m1
        else r = m2
    }

    return (l + r) / 2
}

// 배열에서 값 검색
fun ternarySearch(arr: IntArray, target: Int): Int {
    var left = 0
    var right = arr.size - 1

    while (left <= right) {
        val mid1 = left + (right - left) / 3
        val mid2 = right - (right - left) / 3

        when {
            arr[mid1] == target -> return mid1
            arr[mid2] == target -> return mid2
            target < arr[mid1] -> right = mid1 - 1
            target > arr[mid2] -> left = mid2 + 1
            else -> {
                left = mid1 + 1
                right = mid2 - 1
            }
        }
    }
    return -1
}
```

**관련 알고리즘**: Binary Search

---

## 8. Hash Table Search (해시 테이블 탐색)

**목적**: 해시 함수로 직접 위치 계산

**시간 복잡도**: O(1) 평균, O(n) 최악

**공간 복잡도**: O(n)

**특징**:
- 키-값 매핑
- 충돌 처리 필요
- 상수 시간 접근

**장점**:
- 평균 O(1) 검색
- 삽입/삭제도 O(1)

**단점**:
- 추가 메모리 필요
- 해시 충돌 처리 필요
- 정렬 순서 유지 안됨

**활용 예시**:
- 데이터베이스 인덱싱
- 캐싱
- 중복 검사

**난이도**: 중간 | **사용 빈도**: ★★★★★

**Kotlin 코드**:
```kotlin
// Kotlin의 HashMap 사용
fun hashTableExample() {
    val map = hashMapOf<String, Int>()

    // 삽입
    map["apple"] = 1
    map["banana"] = 2

    // 검색 - O(1)
    val value = map["apple"]  // 1

    // 존재 여부 확인
    val exists = map.containsKey("apple")  // true

    // 삭제
    map.remove("apple")
}

// 간단한 해시 테이블 구현
class SimpleHashTable<K, V>(private val capacity: Int = 16) {
    private val buckets = Array<MutableList<Pair<K, V>>>(capacity) { mutableListOf() }

    private fun hash(key: K): Int = (key.hashCode() and 0x7fffffff) % capacity

    fun put(key: K, value: V) {
        val bucket = buckets[hash(key)]
        val index = bucket.indexOfFirst { it.first == key }
        if (index >= 0) bucket[index] = key to value
        else bucket.add(key to value)
    }

    fun get(key: K): V? {
        return buckets[hash(key)].find { it.first == key }?.second
    }

    fun remove(key: K): Boolean {
        return buckets[hash(key)].removeIf { it.first == key }
    }
}
```

**관련 알고리즘**: BST Search
