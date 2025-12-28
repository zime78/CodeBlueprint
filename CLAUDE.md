# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

GoF 23개 디자인 패턴 학습 앱. Kotlin Multiplatform + Compose Multiplatform 기반으로 macOS, Android, iOS 지원.

## 빌드 및 실행 명령어

```bash
# Desktop 앱 실행
./gradlew desktopApp:run

# Android 앱 빌드
./gradlew androidApp:assembleDebug

# 전체 빌드
./gradlew build

# SQLDelight 스키마 생성
./gradlew generateCommonMainCodeBlueprintDatabaseInterface
```

## 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                    Platform Apps                         │
│  desktopApp/  │  androidApp/  │  iosApp/                │
├─────────────────────────────────────────────────────────┤
│              composeApp/ (UI Layer)                      │
│  ui/navigation, ui/pattern, ui/theme, ui/search ...     │
├─────────────────────────────────────────────────────────┤
│              shared/ (Core Logic)                        │
│  ┌─────────────────────────────────────────────────┐    │
│  │ presentation/  - ViewModel, UiState             │    │
│  ├─────────────────────────────────────────────────┤    │
│  │ domain/        - UseCase, Repository interface  │    │
│  │                  model/                         │    │
│  ├─────────────────────────────────────────────────┤    │
│  │ data/          - Repository impl, Mapper        │    │
│  │                  local/ (SQLDelight)            │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 모듈 구조

| 모듈 | 설명 |
|------|------|
| `shared` | 공유 비즈니스 로직 (domain, data, presentation) |
| `composeApp` | 공유 Compose UI (screens, theme, navigation) |
| `desktopApp` | Desktop 진입점 (Main.kt) |
| `androidApp` | Android 진입점 (MainActivity) |

### 핵심 라이브러리

| 용도 | 라이브러리 |
|------|-----------|
| DI | Koin (`di/Modules.kt`에서 모든 의존성 정의) |
| Database | SQLDelight (`CodeBlueprint.sq`) |
| Navigation | Decompose (`RootComponent`) |
| UI | Compose Multiplatform |

### 플랫폼별 코드

- `shared/src/commonMain/` - 공통 코드
- `shared/src/desktopMain/` - Desktop 전용 (DatabaseDriverFactory, KoinInit)
- `shared/src/androidMain/` - Android 전용
- `shared/src/iosMain/` - iOS 전용

## 데이터 흐름

```
UI (Screen) → ViewModel → UseCase → Repository → SQLDelight
                ↓
           UiState (StateFlow)
```

### 패턴 데이터 초기화

`PatternDataInitializer.initializeIfNeeded()`가 앱 시작 시 호출되어 DB에 23개 GoF 패턴 삽입.
- Desktop: `KoinInit.kt`에서 호출
- Android: Application 클래스에서 호출

## 코드 규칙

- 주석/문서: 한글
- 코드 식별자: 영어 (camelCase, PascalCase)
- Clean Architecture 계층 준수
- ViewModel은 Koin으로 주입

## 데이터베이스 스키마 변경 시

1. `shared/src/commonMain/sqldelight/com/codeblueprint/db/CodeBlueprint.sq` 수정
2. `./gradlew generateCommonMainCodeBlueprintDatabaseInterface` 실행
3. 기존 DB 삭제 필요 시: `rm -f ~/.codeblueprint/codeblueprint.db`

## Claude Code 연동 (tools/)

프로젝트에 MCP 서버와 SKILL 레퍼런스가 포함되어 있습니다.

### MCP 서버 (`tools/mcp-server/`)

```bash
# 빌드 및 실행
cd tools/mcp-server
npm install
npm run build
```

### SKILL 레퍼런스 (`tools/skill/`)

```
SKILL.md                          # 메인 스킬 정의
references/
├── patterns/
│   ├── index.md                  # 패턴 개요 및 선택 가이드
│   ├── creational.md             # 생성 패턴 (5개)
│   ├── structural.md             # 구조 패턴 (7개)
│   └── behavioral.md             # 행위 패턴 (11개)
├── algorithms/
│   ├── index.md                  # 알고리즘 개요 및 선택 가이드
│   ├── sorting.md                # 정렬 알고리즘 (13개)
│   ├── searching.md              # 탐색 알고리즘 (8개)
│   ├── graph.md                  # 그래프 알고리즘 (12개)
│   ├── dynamic-programming.md    # 동적 프로그래밍 (8개)
│   ├── divide-conquer.md         # 분할 정복 (5개)
│   ├── greedy.md                 # 탐욕 알고리즘 (5개)
│   ├── backtracking.md           # 백트래킹 (5개)
│   ├── string.md                 # 문자열 알고리즘 (7개)
│   └── math.md                   # 수학 알고리즘 (10개)
└── code_templates.md             # 언어별 코드 템플릿
```

사용법:
- `/pattern <name>` - 패턴 코드 생성
- `/pattern list` - 패턴 목록
- `/pattern search <keyword>` - 패턴 검색
- `/algorithm <name>` - 알고리즘 코드 생성
- `/algorithm list` - 알고리즘 목록
- `/algorithm list <category>` - 카테고리별 알고리즘 목록
- `/algorithm search <keyword>` - 알고리즘 검색

## 개발 지침

### 패턴 데이터 수정 시 동기화 필수

패턴 관련 코드 수정 시 아래 3곳을 **모두 함께 수정**해야 합니다:

| 위치 | 파일 | 설명 |
|------|------|------|
| **앱 (DB)** | `shared/.../PatternDataInitializer.kt` | 앱에서 사용하는 패턴 데이터 |
| **MCP** | `tools/mcp-server/data/patterns.json` | MCP 서버 패턴 데이터 |
| **SKILL** | `tools/skill/references/patterns/*.md` | 스킬 레퍼런스 문서 |

### 문서 업데이트

코드 변경 시 관련 문서도 함께 업데이트:

- 패턴 추가/수정 → SKILL 레퍼런스 (`tools/skill/references/patterns/*.md`) + MCP 데이터 (`tools/mcp-server/data/patterns.json`)
- 코드 템플릿 변경 → `tools/skill/references/code_templates.md`
- 아키텍처 변경 → `CLAUDE.md`

### 체크리스트

```
[ ] PatternDataInitializer.kt 수정
[ ] tools/mcp-server/data/patterns.json 수정
[ ] tools/skill/references/patterns/*.md 수정
[ ] tools/skill/references/code_templates.md 수정 (코드 예시 변경 시)
[ ] MCP 서버 재빌드: cd tools/mcp-server && npm run build
[ ] 앱 DB 초기화: rm -f ~/.codeblueprint/codeblueprint.db
```
