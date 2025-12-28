# CodeBlueprint Tools - MCP 서버, CLI & SKILL 사용 가이드

Claude Code와 연동하여 디자인 패턴 및 알고리즘 정보를 활용할 수 있는 도구 모음입니다.

---

## 목차

1. [소개](#소개)
2. [사전 요구사항](#사전-요구사항)
3. [MCP CLI 설정](#mcp-cli-설정)
4. [MCP 서버 설정](#mcp-서버-설정)
5. [SKILL 설정](#skill-설정)
6. [사용 예시](#사용-예시)
7. [다른 프로젝트에서 사용하기](#다른-프로젝트에서-사용하기)
8. [데이터 구조](#데이터-구조)
9. [문제 해결](#문제-해결)

---

## 소개

### MCP 서버란?

**MCP (Model Context Protocol)** 서버는 Claude Code가 외부 데이터와 도구에 접근할 수 있게 해주는 확장 기능입니다. 이 MCP 서버는 23개 GoF 디자인 패턴과 73개 알고리즘 정보를 제공합니다.

**제공 기능:**
- 패턴/알고리즘 목록 조회
- 상세 정보 조회
- 코드 예시 제공 (Kotlin, Java, Swift, Python)
- 키워드 검색
- 상황 기반 추천

### SKILL이란?

**SKILL**은 Claude Code에서 특정 작업을 자동화하는 커스텀 명령어입니다. `/pattern`, `/algorithm` 명령으로 빠르게 패턴/알고리즘 코드를 생성할 수 있습니다.

**제공 기능:**
- `/pattern singleton` - Singleton 패턴 코드 생성
- `/algorithm quick-sort` - Quick Sort 알고리즘 코드 생성
- 프로젝트 언어 자동 감지

---

## 사전 요구사항

### 1. Node.js 설치 (v18 이상)

```bash
# macOS (Homebrew)
brew install node

# 버전 확인
node --version  # v18.0.0 이상 필요
npm --version
```

### 2. Claude Code 설치

Claude Code CLI가 설치되어 있어야 합니다.

```bash
# 설치 확인
claude --version
```

### 3. 디렉토리 구조 확인

```
tools/
├── README.md                 # 이 문서
├── mcp-cli/                  # MCP CLI (Bash 스크립트)
│   ├── codeblueprint         # 메인 CLI 스크립트
│   ├── README.md             # CLI 사용법 문서
│   └── completions/          # 셸 자동완성
│       ├── codeblueprint.bash
│       └── codeblueprint.zsh
├── mcp-server/               # MCP 서버
│   ├── package.json          # Node.js 프로젝트 설정
│   ├── tsconfig.json         # TypeScript 설정
│   ├── src/
│   │   └── index.ts          # MCP 서버 메인 코드
│   ├── data/
│   │   ├── patterns.json     # 23개 패턴 데이터
│   │   └── algorithms.json   # 73개 알고리즘 데이터
│   └── dist/                 # 빌드 출력 (npm run build)
└── skill/                    # SKILL 파일
    ├── SKILL.md              # 스킬 정의
    └── references/           # 레퍼런스 문서
        ├── patterns/         # 패턴 문서 (4개)
        │   ├── index.md
        │   ├── creational.md
        │   ├── structural.md
        │   └── behavioral.md
        ├── algorithms/       # 알고리즘 문서 (10개)
        │   ├── index.md
        │   ├── sorting.md
        │   ├── searching.md
        │   ├── graph.md
        │   ├── dynamic-programming.md
        │   ├── divide-conquer.md
        │   ├── greedy.md
        │   ├── backtracking.md
        │   ├── string.md
        │   └── math.md
        └── code_templates.md # 언어별 코드 템플릿
```

---

## MCP CLI 설정

MCP CLI는 터미널에서 패턴/알고리즘 정보를 빠르게 조회할 수 있는 Bash 스크립트입니다.

### Step 1: PATH에 추가

```bash
# .zshrc 또는 .bashrc에 추가
export PATH="$PATH:/path/to/CodeBlueprint/tools/mcp-cli"

# 적용
source ~/.zshrc  # 또는 source ~/.bashrc
```

### Step 2: 실행 권한 부여

```bash
chmod +x /path/to/CodeBlueprint/tools/mcp-cli/codeblueprint
```

### Step 3: 사용

```bash
# 패턴 목록
codeblueprint pattern list

# 패턴 상세
codeblueprint pattern get singleton

# 패턴 코드 예시
codeblueprint pattern code factory-method kotlin

# 알고리즘 목록 (카테고리별)
codeblueprint algorithm list sorting

# 알고리즘 검색
codeblueprint algorithm search 정렬

# 도움말
codeblueprint help
```

### Step 4: 자동완성 설정 (선택)

```bash
# Zsh 사용자
source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.zsh

# 또는 .zshrc에 추가
echo 'source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.zsh' >> ~/.zshrc

# Bash 사용자
source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.bash

# 또는 .bashrc에 추가
echo 'source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.bash' >> ~/.bashrc
```

---

## MCP 서버 설정

### Step 1: MCP 서버 빌드

```bash
# 프로젝트 루트에서 실행
cd tools/mcp-server

# 의존성 설치
npm install

# TypeScript 빌드
npm run build
```

빌드가 완료되면 `dist/` 폴더에 JavaScript 파일이 생성됩니다.

### Step 2: Claude Code에 MCP 서버 등록

Claude Code 설정 파일에 MCP 서버를 등록합니다.

**설정 파일 위치:**
- macOS/Linux: `~/.claude.json`
- Windows: `%USERPROFILE%\.claude.json`

**설정 파일 편집:**

```bash
# 설정 파일 열기 (없으면 생성됨)
nano ~/.claude.json
```

**설정 내용 추가:**

```json
{
  "mcpServers": {
    "codeblueprint": {
      "command": "node",
      "args": ["/절대경로/tools/mcp-server/dist/index.js"],
      "env": {}
    }
  }
}
```

> **중요:** `/절대경로/` 부분을 실제 프로젝트 경로로 변경하세요.

**예시 (macOS):**
```json
{
  "mcpServers": {
    "codeblueprint": {
      "command": "node",
      "args": ["/Users/사용자명/Git/CodeBlueprint/tools/mcp-server/dist/index.js"],
      "env": {}
    }
  }
}
```

### Step 3: 연결 테스트

```bash
# Claude Code 재시작 후 테스트
claude

# MCP 도구 목록 확인
/mcp
```

MCP 서버가 정상 연결되면 `codeblueprint` 서버와 도구 목록이 표시됩니다.

### MCP 서버 제공 도구 (10개)

| 도구명 | 설명 | 예시 |
|--------|------|------|
| `list_patterns` | 패턴 목록 조회 | 전체 또는 카테고리별 |
| `get_pattern` | 패턴 상세 정보 | `singleton` |
| `get_code` | 언어별 코드 예시 | `singleton` + `kotlin` |
| `search_patterns` | 패턴 검색 | `생성`, `이벤트` |
| `recommend_pattern` | 패턴 추천 | 상황 설명 입력 |
| `list_algorithms` | 알고리즘 목록 조회 | 전체 또는 카테고리별 |
| `get_algorithm` | 알고리즘 상세 정보 | `quick-sort` |
| `search_algorithms` | 알고리즘 검색 | `정렬`, `그래프` |
| `recommend_algorithm` | 알고리즘 추천 | 문제 설명 입력 |

---

## SKILL 설정

### Step 1: SKILL 디렉토리 복사

SKILL을 사용하려면 `tools/skill/` 폴더를 Claude Code 스킬 디렉토리에 복사합니다.

```bash
# 스킬 디렉토리 생성 (없는 경우)
mkdir -p ~/.claude/skills

# 스킬 복사
cp -r tools/skill ~/.claude/skills/codeblueprint-patterns
```

### Step 2: 복사 확인

```bash
# 스킬 디렉토리 확인
ls ~/.claude/skills/codeblueprint-patterns/

# 출력 예시:
# SKILL.md
# references/
```

### Step 3: Claude Code에서 사용

Claude Code를 실행하면 자동으로 SKILL이 로드됩니다.

```bash
# Claude Code 실행
claude

# 스킬 사용
/pattern list
/algorithm list
```

---

## 사용 예시

### MCP 도구 사용 예시

Claude Code 대화에서 다음과 같이 요청하면 MCP 도구가 자동으로 호출됩니다:

#### 1. 패턴 목록 조회
```
사용 가능한 디자인 패턴 목록을 보여줘
```

#### 2. 특정 패턴 상세 정보
```
Singleton 패턴에 대해 자세히 알려줘
```

#### 3. 패턴 코드 예시
```
Observer 패턴을 Kotlin으로 구현해줘
```

#### 4. 패턴 검색
```
이벤트 처리에 적합한 패턴을 찾아줘
```

#### 5. 상황 기반 패턴 추천
```
객체를 하나만 생성해야 하는 상황인데 어떤 패턴을 써야 할까?
```

#### 6. 알고리즘 목록 조회
```
정렬 알고리즘 목록을 보여줘
```

#### 7. 알고리즘 상세 정보
```
Quick Sort 알고리즘의 시간 복잡도와 특징을 알려줘
```

#### 8. 알고리즘 검색
```
최단 경로를 찾는 알고리즘을 검색해줘
```

#### 9. 문제 기반 알고리즘 추천
```
정렬된 배열에서 특정 값을 빠르게 찾아야 해. 어떤 알고리즘을 써야 할까?
```

#### 10. 복합 요청
```
Factory Method 패턴과 Abstract Factory 패턴의 차이점을 설명하고 각각 언제 사용하는지 알려줘
```

### SKILL 명령어 사용 예시

```bash
# 패턴 관련 명령어
/pattern list                    # 전체 패턴 목록
/pattern singleton               # Singleton 패턴 코드 생성
/pattern factory-method          # Factory Method 패턴 코드 생성
/pattern search 생성             # "생성" 키워드로 패턴 검색
/pattern search 이벤트           # "이벤트" 키워드로 패턴 검색

# 알고리즘 관련 명령어
/algorithm list                  # 전체 알고리즘 목록
/algorithm list sorting          # 정렬 알고리즘만 조회
/algorithm list graph            # 그래프 알고리즘만 조회
/algorithm quick-sort            # Quick Sort 코드 생성
/algorithm dijkstra              # Dijkstra 알고리즘 코드 생성
/algorithm search 정렬           # "정렬" 키워드로 알고리즘 검색
```

### 실제 프로젝트 적용 시나리오

#### 시나리오 1: 데이터베이스 연결 관리

```
User: 데이터베이스 연결을 전역에서 하나만 사용하고 싶어

Claude: Singleton 패턴이 적합합니다.
        [MCP 도구로 Singleton 패턴 정보 조회]
        [프로젝트 언어에 맞는 코드 생성]
```

#### 시나리오 2: 이벤트 기반 시스템 구현

```
User: 상태 변경 시 여러 컴포넌트에 알림을 보내고 싶어

Claude: Observer 패턴이 적합합니다.
        [MCP 도구로 Observer 패턴 정보 조회]
        [구현 예시 및 적용 가이드 제공]
```

#### 시나리오 3: 최단 경로 탐색

```
User: 지도에서 두 지점 간 최단 경로를 찾아야 해

Claude: Dijkstra 알고리즘을 추천합니다.
        [MCP 도구로 Dijkstra 알고리즘 정보 조회]
        [시간 복잡도, 구현 코드 제공]
```

---

## 다른 프로젝트에서 사용하기

### Step 1: 파일 복사

```bash
# 새 프로젝트에 tools 디렉토리 복사
cp -r /path/to/CodeBlueprint/tools /path/to/새프로젝트/

# 또는 git에서 직접 복제
git clone https://github.com/your-repo/CodeBlueprint.git
cp -r CodeBlueprint/tools ./
```

### Step 2: MCP 서버 빌드

```bash
cd 새프로젝트/tools/mcp-server
npm install
npm run build
```

### Step 3: Claude Code 설정 수정

`~/.claude.json`에서 경로를 새 프로젝트로 변경:

```json
{
  "mcpServers": {
    "codeblueprint": {
      "command": "node",
      "args": ["/path/to/새프로젝트/tools/mcp-server/dist/index.js"],
      "env": {}
    }
  }
}
```

### Step 4: SKILL 복사 (선택)

```bash
# 기존 스킬 삭제 후 새로 복사
rm -rf ~/.claude/skills/codeblueprint-patterns
cp -r tools/skill ~/.claude/skills/codeblueprint-patterns
```

### 데이터 커스터마이징

프로젝트에 맞게 패턴/알고리즘 데이터를 수정할 수 있습니다:

```bash
# 패턴 데이터 수정
nano tools/mcp-server/data/patterns.json

# 알고리즘 데이터 수정
nano tools/mcp-server/data/algorithms.json

# 수정 후 재빌드
cd tools/mcp-server
npm run build
```

---

## 데이터 구조

### patterns.json 구조

```json
{
  "patterns": [
    {
      "id": "singleton",           // 고유 ID
      "name": "Singleton",         // 영문명
      "koreanName": "싱글톤",       // 한글명
      "category": "creational",    // 카테고리 (creational/structural/behavioral)
      "purpose": "클래스의 인스턴스를 오직 하나만 생성...",  // 목적
      "characteristics": [...],    // 특징 배열
      "advantages": [...],         // 장점 배열
      "disadvantages": [...],      // 단점 배열
      "useCases": [...],           // 활용 예시 배열
      "difficulty": "low",         // 난이도 (low/medium/high)
      "frequency": 5,              // 사용 빈도 (1-5)
      "relatedPatterns": [...],    // 관련 패턴 ID 배열
      "codeExamples": {            // 언어별 코드 예시
        "kotlin": "...",
        "java": "...",
        "swift": "...",
        "python": "..."
      },
      "diagram": "..."             // Mermaid 클래스 다이어그램
    }
  ],
  "categories": {
    "creational": { "name": "생성 패턴", "description": "...", "patterns": [...] },
    "structural": { "name": "구조 패턴", "description": "...", "patterns": [...] },
    "behavioral": { "name": "행위 패턴", "description": "...", "patterns": [...] }
  }
}
```

### algorithms.json 구조

```json
{
  "algorithms": [
    {
      "id": "quick-sort",          // 고유 ID
      "name": "Quick Sort",        // 영문명
      "koreanName": "퀵 정렬",      // 한글명
      "category": "sorting",       // 카테고리
      "purpose": "피벗을 기준으로 분할 정복 정렬",  // 목적
      "timeComplexity": {          // 시간 복잡도
        "best": "O(n log n)",
        "average": "O(n log n)",
        "worst": "O(n²)"
      },
      "spaceComplexity": "O(log n)",  // 공간 복잡도
      "characteristics": [...],    // 특징 배열
      "advantages": [...],         // 장점 배열
      "disadvantages": [...],      // 단점 배열
      "useCases": [...],           // 활용 예시 배열
      "difficulty": "MEDIUM",      // 난이도
      "frequency": 5,              // 사용 빈도
      "relatedAlgorithms": [...],  // 관련 알고리즘 ID 배열
      "codeExamples": { ... }      // 언어별 코드 예시 (선택)
    }
  ],
  "categories": {
    "sorting": { "name": "정렬 알고리즘", "description": "...", "algorithms": [...] },
    "searching": { ... },
    "graph": { ... },
    "dynamic-programming": { ... },
    "divide-conquer": { ... },
    "greedy": { ... },
    "backtracking": { ... },
    "string": { ... },
    "math": { ... }
  }
}
```

### 새 패턴/알고리즘 추가 방법

1. `patterns.json` 또는 `algorithms.json`에 새 항목 추가
2. MCP 서버 재빌드: `npm run build`
3. SKILL 레퍼런스 문서 업데이트 (선택)

---

## 문제 해결

### MCP 서버 연결 안됨

**증상:** Claude Code에서 MCP 도구가 표시되지 않음

**해결:**
```bash
# 1. 빌드 확인
cd tools/mcp-server
npm run build
ls dist/  # index.js 파일 존재 확인

# 2. 경로 확인
cat ~/.claude.json  # 경로가 올바른지 확인

# 3. 수동 실행 테스트
node dist/index.js  # 에러 메시지 확인

# 4. Claude Code 재시작
```

### SKILL 명령어 인식 안됨

**증상:** `/pattern`, `/algorithm` 명령이 작동하지 않음

**해결:**
```bash
# 1. 스킬 디렉토리 확인
ls ~/.claude/skills/codeblueprint-patterns/
# SKILL.md 파일이 있어야 함

# 2. SKILL.md 파일 구조 확인
head ~/.claude/skills/codeblueprint-patterns/SKILL.md
# --- 로 시작하는 frontmatter가 있어야 함

# 3. Claude Code 재시작
```

### npm install 실패

**증상:** 의존성 설치 중 에러 발생

**해결:**
```bash
# Node.js 버전 확인
node --version  # v18.0.0 이상 필요

# npm 캐시 정리 후 재시도
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### TypeScript 빌드 에러

**증상:** `npm run build` 실패

**해결:**
```bash
# TypeScript 버전 확인
npx tsc --version

# tsconfig.json 확인
cat tsconfig.json

# 에러 메시지에 따라 수정 후 재빌드
npm run build
```

### 데이터 파일 로드 실패

**증상:** MCP 서버 실행 시 JSON 파싱 에러

**해결:**
```bash
# JSON 유효성 검사
node -e "JSON.parse(require('fs').readFileSync('data/patterns.json'))"
node -e "JSON.parse(require('fs').readFileSync('data/algorithms.json'))"

# 에러 발생 시 JSON 구문 오류 수정
```

### 디버깅 팁

```bash
# MCP 서버 로그 확인
cd tools/mcp-server
node dist/index.js 2>&1 | tee mcp.log

# Claude Code 디버그 모드
claude --debug
```

---

## 지원 내용 요약

### 디자인 패턴 (23개)

| 카테고리 | 개수 | 패턴 |
|---------|-----|------|
| 생성 | 5개 | Singleton, Factory Method, Abstract Factory, Builder, Prototype |
| 구조 | 7개 | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy |
| 행위 | 11개 | Chain of Responsibility, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor, Interpreter |

### 알고리즘 (73개)

| 카테고리 | 개수 |
|---------|-----|
| 정렬 | 13개 |
| 탐색 | 8개 |
| 그래프 | 12개 |
| 동적 프로그래밍 | 8개 |
| 분할 정복 | 5개 |
| 탐욕 | 5개 |
| 백트래킹 | 5개 |
| 문자열 | 7개 |
| 수학 | 10개 |

---

## 관련 문서

- [프로젝트 README](../README.md)
- [개발 가이드 (CLAUDE.md)](../CLAUDE.md)
- [아키텍처 문서](../docs/ARCHITECTURE.md)
