# CodeBlueprint CLI

디자인 패턴 및 알고리즘 참조 CLI 도구.

## 요구사항

- Bash 4.0+
- jq (`brew install jq` 또는 `apt install jq`)

## 설치

### 1. PATH에 추가

```bash
# ~/.bashrc 또는 ~/.zshrc에 추가
export PATH="$PATH:/path/to/CodeBlueprint/tools/mcp-cli"
```

### 2. 자동 완성 설정 (선택)

**Bash:**
```bash
source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.bash
```

**Zsh:**
```bash
source /path/to/CodeBlueprint/tools/mcp-cli/completions/codeblueprint.zsh
```

## 사용법

### 패턴 명령어

```bash
# 전체 패턴 목록
codeblueprint pattern list

# 카테고리별 패턴 목록
codeblueprint pattern list creational
codeblueprint pattern list structural
codeblueprint pattern list behavioral

# 패턴 상세 조회
codeblueprint pattern get singleton
codeblueprint pattern get factory

# 패턴 검색
codeblueprint pattern search 객체
codeblueprint pattern search 생성

# 코드 예시 조회
codeblueprint pattern code singleton kotlin
codeblueprint pattern code factory java
```

### 알고리즘 명령어

```bash
# 전체 알고리즘 목록
codeblueprint algorithm list

# 카테고리별 알고리즘 목록
codeblueprint algorithm list sorting
codeblueprint algorithm list searching
codeblueprint algorithm list graph

# 알고리즘 상세 조회
codeblueprint algorithm get quick-sort
codeblueprint algorithm get binary-search

# 알고리즘 검색
codeblueprint algorithm search 정렬
codeblueprint algorithm search 탐색

# 코드 예시 조회
codeblueprint algorithm code quick-sort kotlin
codeblueprint algorithm code binary-search python
```

### 도움말

```bash
codeblueprint help
codeblueprint --help
```

## 지원 언어

코드 예시 조회 시 다음 언어를 지원합니다:

- kotlin
- java
- python
- swift
- javascript

## 예시 출력

```
$ codeblueprint pattern get singleton

Singleton (싱글톤)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
카테고리: 생성 패턴
난이도: LOW

목적:
  클래스의 인스턴스가 오직 하나만 생성되도록 보장하고, 전역적인 접근점 제공

특징:
  • 단 하나의 인스턴스만 존재
  • 전역 접근점 제공
  • 지연 초기화 가능

장점:
  ✓ 메모리 절약 (인스턴스 재사용)
  ✓ 전역 상태 공유 용이
  ✓ 인스턴스 생성 제어

단점:
  ✗ 전역 상태로 인한 테스트 어려움
  ✗ 멀티스레드 환경에서 동기화 필요
  ✗ 의존성 숨김으로 결합도 증가
```
