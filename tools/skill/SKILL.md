---
name: codeblueprint-patterns
description: GoF 디자인 패턴과 알고리즘을 현재 프로젝트에 적용하는 스킬입니다. 사용자가 '/pattern singleton', '/algorithm quick-sort', '싱글톤 패턴 적용해줘', '퀵 정렬 구현해줘' 등 패턴/알고리즘 관련 요청 시 사용합니다. (user)
---

# CodeBlueprint Patterns & Algorithms - 디자인 패턴 및 알고리즘 적용 스킬

## 개요

이 스킬은 23개 GoF(Gang of Four) 디자인 패턴과 73개 알고리즘을 현재 프로젝트에 적용하는 데 도움을 줍니다.
프로젝트 구조를 분석하여 적합한 언어(Kotlin, Java, Swift, Python)로 코드를 생성합니다.

## 사용법

### 패턴 목록 조회
```
/pattern list
```

### 특정 패턴 적용
```
/pattern <패턴명>
/pattern singleton
/pattern factory-method
/pattern observer
```

### 자연어 요청
```
싱글톤 패턴 적용해줘
옵저버 패턴으로 이벤트 처리 구현해줘
팩토리 메서드 패턴 코드 생성해줘
```

### 패턴 검색
```
/pattern search <키워드>
/pattern search 생성
/pattern search 이벤트
```

---

## 알고리즘 사용법

### 알고리즘 목록 조회
```
/algorithm list
/algorithm list sorting
/algorithm list graph
```

### 특정 알고리즘 적용
```
/algorithm <알고리즘명>
/algorithm quick-sort
/algorithm dijkstra
/algorithm kmp
```

### 알고리즘 검색
```
/algorithm search <키워드>
/algorithm search 정렬
/algorithm search 최단경로
```

### 자연어 요청
```
퀵 정렬 구현해줘
다익스트라 알고리즘 코드 보여줘
BFS로 최단 경로 찾는 코드 생성해줘
```

## 지원 알고리즘 (73개)

### 카테고리별 알고리즘

| 카테고리 | 개수 | 대표 알고리즘 |
|---------|-----|-------------|
| [정렬](references/algorithms/sorting.md) | 13개 | Quick Sort, Merge Sort, Heap Sort |
| [탐색](references/algorithms/searching.md) | 8개 | Binary Search, Hash Table |
| [그래프](references/algorithms/graph.md) | 12개 | Dijkstra, BFS, DFS, A* |
| [동적 프로그래밍](references/algorithms/dynamic-programming.md) | 8개 | LCS, LIS, Knapsack |
| [분할 정복](references/algorithms/divide-conquer.md) | 5개 | Merge Sort, Strassen |
| [탐욕](references/algorithms/greedy.md) | 5개 | Huffman, Activity Selection |
| [백트래킹](references/algorithms/backtracking.md) | 5개 | N-Queens, Sudoku |
| [문자열](references/algorithms/string.md) | 7개 | KMP, Rabin-Karp, Trie |
| [수학](references/algorithms/math.md) | 10개 | GCD, FFT, Miller-Rabin |

---

## 지원 패턴 (23개)

### 생성 패턴 (Creational) - 5개
| ID | 영문명 | 한글명 | 난이도 |
|----|--------|--------|--------|
| singleton | Singleton | 싱글톤 | 낮음 |
| factory-method | Factory Method | 팩토리 메서드 | 중간 |
| abstract-factory | Abstract Factory | 추상 팩토리 | 높음 |
| builder | Builder | 빌더 | 중간 |
| prototype | Prototype | 프로토타입 | 중간 |

### 구조 패턴 (Structural) - 7개
| ID | 영문명 | 한글명 | 난이도 |
|----|--------|--------|--------|
| adapter | Adapter | 어댑터 | 낮음 |
| bridge | Bridge | 브릿지 | 높음 |
| composite | Composite | 컴포지트 | 중간 |
| decorator | Decorator | 데코레이터 | 중간 |
| facade | Facade | 퍼사드 | 낮음 |
| flyweight | Flyweight | 플라이웨이트 | 높음 |
| proxy | Proxy | 프록시 | 중간 |

### 행위 패턴 (Behavioral) - 11개
| ID | 영문명 | 한글명 | 난이도 |
|----|--------|--------|--------|
| chain-of-responsibility | Chain of Responsibility | 책임 연쇄 | 중간 |
| command | Command | 커맨드 | 중간 |
| iterator | Iterator | 반복자 | 낮음 |
| mediator | Mediator | 중재자 | 중간 |
| memento | Memento | 메멘토 | 중간 |
| observer | Observer | 옵저버 | 중간 |
| state | State | 상태 | 중간 |
| strategy | Strategy | 전략 | 낮음 |
| template-method | Template Method | 템플릿 메서드 | 중간 |
| visitor | Visitor | 방문자 | 높음 |
| interpreter | Interpreter | 인터프리터 | 높음 |

## 워크플로우

1. **프로젝트 분석**: 현재 프로젝트의 언어 및 구조 파악
2. **패턴 선택**: 요청된 패턴 정보 로드
3. **코드 생성**: 프로젝트에 맞는 언어로 패턴 코드 생성
4. **적용 가이드**: 생성된 코드를 어디에 배치할지 안내

## 언어 자동 감지

프로젝트 구조를 분석하여 적합한 언어를 자동 선택합니다:

| 파일/디렉토리 | 감지 언어 |
|--------------|----------|
| `*.kt`, `build.gradle.kts` | Kotlin |
| `*.java`, `pom.xml` | Java |
| `*.swift`, `Package.swift` | Swift |
| `*.py`, `requirements.txt` | Python |

## 참조 문서

### 패턴 레퍼런스
- [patterns/index.md](references/patterns/index.md) - 패턴 개요 및 선택 가이드
- [patterns/creational.md](references/patterns/creational.md) - 생성 패턴 (5개)
- [patterns/structural.md](references/patterns/structural.md) - 구조 패턴 (7개)
- [patterns/behavioral.md](references/patterns/behavioral.md) - 행위 패턴 (11개)

### 알고리즘 레퍼런스
- [algorithms/index.md](references/algorithms/index.md) - 알고리즘 개요 및 선택 가이드
- [algorithms/sorting.md](references/algorithms/sorting.md) - 정렬 알고리즘 (13개)
- [algorithms/searching.md](references/algorithms/searching.md) - 탐색 알고리즘 (8개)
- [algorithms/graph.md](references/algorithms/graph.md) - 그래프 알고리즘 (12개)
- [algorithms/dynamic-programming.md](references/algorithms/dynamic-programming.md) - 동적 프로그래밍 (8개)
- [algorithms/divide-conquer.md](references/algorithms/divide-conquer.md) - 분할 정복 (5개)
- [algorithms/greedy.md](references/algorithms/greedy.md) - 탐욕 알고리즘 (5개)
- [algorithms/backtracking.md](references/algorithms/backtracking.md) - 백트래킹 (5개)
- [algorithms/string.md](references/algorithms/string.md) - 문자열 알고리즘 (7개)
- [algorithms/math.md](references/algorithms/math.md) - 수학 알고리즘 (10개)

### 코드 템플릿
- [code_templates.md](references/code_templates.md) - 언어별 코드 템플릿

## 예시

### Singleton 패턴 적용 요청
```
User: /pattern singleton
```

### 응답 예시
```kotlin
// Kotlin Singleton 패턴
object DatabaseConnection {
    private var connection: Connection? = null

    fun getConnection(): Connection {
        if (connection == null) {
            connection = createConnection()
        }
        return connection!!
    }

    private fun createConnection(): Connection {
        // 연결 생성 로직
        return Connection()
    }
}
```

**적용 위치 제안:**
- `src/main/kotlin/com/yourpackage/data/DatabaseConnection.kt`

**관련 패턴:**
- Factory Method: 객체 생성 로직을 분리하고 싶다면
- Prototype: 인스턴스 복제가 필요하다면
