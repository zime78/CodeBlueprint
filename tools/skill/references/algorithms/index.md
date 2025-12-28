# 알고리즘 레퍼런스

## 개요

소프트웨어 개발에서 자주 사용되는 알고리즘 모음입니다.

## 알고리즘 카테고리 (총 73개)

| 카테고리 | 알고리즘 수 | 파일 |
|---------|-----------|------|
| [정렬](sorting.md) | 13개 | Bubble, Selection, Insertion, Merge, Quick, Heap, Counting, Radix, Bucket, Shell, Tim, Intro, Tree |
| [탐색](searching.md) | 8개 | Linear, Binary, Jump, Interpolation, Exponential, Fibonacci, Ternary, Hash |
| [그래프](graph.md) | 12개 | BFS, DFS, Dijkstra, Bellman-Ford, Floyd-Warshall, A*, Prim, Kruskal, Topological, Tarjan, Kosaraju, Johnson |
| [동적 프로그래밍](dynamic-programming.md) | 8개 | Fibonacci, LCS, LIS, Knapsack, Edit Distance, Matrix Chain, Coin Change, Rod Cutting |
| [분할 정복](divide-conquer.md) | 5개 | Binary Search, Merge Sort, Strassen, Closest Pair, Karatsuba |
| [탐욕](greedy.md) | 5개 | Activity Selection, Huffman, Fractional Knapsack, Job Sequencing, Optimal Merge |
| [백트래킹](backtracking.md) | 5개 | N-Queens, Sudoku, Graph Coloring, Hamiltonian, Subset Sum |
| [문자열](string.md) | 7개 | KMP, Rabin-Karp, Boyer-Moore, Z, Suffix Array, Trie, Aho-Corasick |
| [수학](math.md) | 10개 | GCD, Sieve, Fast Exp, Modular, CRT, FFT, Simpson, Newton-Raphson, Factorization, Miller-Rabin |

---

## 알고리즘 선택 가이드

### 상황별 추천 알고리즘

| 상황 | 추천 알고리즘 | 카테고리 |
|------|-------------|---------|
| 배열 정렬 (일반) | Quick Sort, Merge Sort | 정렬 |
| 거의 정렬된 배열 | Insertion Sort, Tim Sort | 정렬 |
| 정수 범위가 작은 정렬 | Counting Sort, Radix Sort | 정렬 |
| 정렬된 배열에서 검색 | Binary Search | 탐색 |
| 해시 테이블 검색 | Hash Search | 탐색 |
| 최단 경로 (양수 가중치) | Dijkstra | 그래프 |
| 최단 경로 (음수 가중치) | Bellman-Ford | 그래프 |
| 모든 쌍 최단 경로 | Floyd-Warshall | 그래프 |
| 경로 탐색 (게임 AI) | A* | 그래프 |
| 최소 신장 트리 | Prim, Kruskal | 그래프 |
| 의존성 해결 | Topological Sort | 그래프 |
| 최적 부분 구조 문제 | 동적 프로그래밍 | DP |
| 문자열 비교 | Edit Distance | DP |
| 문자열 패턴 매칭 | KMP, Boyer-Moore | 문자열 |
| 여러 패턴 동시 검색 | Aho-Corasick | 문자열 |
| 접두사 검색 | Trie | 문자열 |
| 소수 판별 | Miller-Rabin | 수학 |
| 소수 목록 생성 | Sieve of Eratosthenes | 수학 |
| 최대공약수 | Euclidean GCD | 수학 |

---

## 복잡도 비교

### 정렬 알고리즘

| 알고리즘 | 최선 | 평균 | 최악 | 공간 | 안정성 |
|---------|------|------|------|------|--------|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) | O |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) | X |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) | O |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | O |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) | X |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | X |
| Counting Sort | O(n+k) | O(n+k) | O(n+k) | O(k) | O |
| Radix Sort | O(d(n+k)) | O(d(n+k)) | O(d(n+k)) | O(n+k) | O |
| Tim Sort | O(n) | O(n log n) | O(n log n) | O(n) | O |

### 탐색 알고리즘

| 알고리즘 | 시간 복잡도 | 공간 복잡도 | 요구 조건 |
|---------|-----------|-----------|---------|
| Linear Search | O(n) | O(1) | 없음 |
| Binary Search | O(log n) | O(1) | 정렬됨 |
| Jump Search | O(sqrt n) | O(1) | 정렬됨 |
| Interpolation Search | O(log log n) ~ O(n) | O(1) | 균등 분포 |
| Hash Search | O(1) 평균 | O(n) | 해시 테이블 |

### 그래프 알고리즘

| 알고리즘 | 시간 복잡도 | 공간 복잡도 | 용도 |
|---------|-----------|-----------|------|
| BFS | O(V+E) | O(V) | 최단 경로 (무가중치) |
| DFS | O(V+E) | O(V) | 경로 탐색, 사이클 검출 |
| Dijkstra | O(V² or E log V) | O(V) | 최단 경로 (양수 가중치) |
| Bellman-Ford | O(VE) | O(V) | 최단 경로 (음수 가중치) |
| Floyd-Warshall | O(V³) | O(V²) | 모든 쌍 최단 경로 |
| A* | O(E) | O(V) | 휴리스틱 탐색 |
| Prim | O(V² or E log V) | O(V) | MST |
| Kruskal | O(E log E) | O(V) | MST |

---

## 알고리즘 학습 순서 추천

### 초급
1. Linear Search -> Binary Search
2. Bubble Sort -> Selection Sort -> Insertion Sort
3. BFS -> DFS
4. Euclidean GCD

### 중급
1. Merge Sort -> Quick Sort -> Heap Sort
2. Dijkstra -> Bellman-Ford
3. 동적 프로그래밍 기초 (Fibonacci, LCS, LIS)
4. KMP 문자열 매칭

### 고급
1. A* 탐색
2. Floyd-Warshall, Johnson
3. Suffix Array, Aho-Corasick
4. FFT, Miller-Rabin
