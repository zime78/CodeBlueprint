# 디자인 패턴 레퍼런스

## 개요

소프트웨어 디자인 패턴 모음입니다.

## GoF 디자인 패턴 (23개)

Gang of Four(GoF)가 정의한 23개 디자인 패턴입니다.

| 카테고리 | 패턴 수 | 파일 |
|---------|--------|------|
| [생성 패턴](creational.md) | 5개 | Singleton, Factory Method, Abstract Factory, Builder, Prototype |
| [구조 패턴](structural.md) | 7개 | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy |
| [행위 패턴](behavioral.md) | 11개 | Chain of Responsibility, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor, Interpreter |

---

## 패턴 선택 가이드

### 상황별 추천 패턴

| 상황 | 추천 패턴 | 카테고리 |
|------|----------|---------|
| 객체를 하나만 생성해야 할 때 | Singleton | 생성 |
| 객체 생성을 캡슐화하고 싶을 때 | Factory Method, Abstract Factory | 생성 |
| 복잡한 객체를 단계별로 생성할 때 | Builder | 생성 |
| 기존 객체를 복제하고 싶을 때 | Prototype | 생성 |
| 호환되지 않는 인터페이스를 연결할 때 | Adapter | 구조 |
| 추상화와 구현을 분리할 때 | Bridge | 구조 |
| 트리 구조를 표현할 때 | Composite | 구조 |
| 객체에 동적으로 기능을 추가할 때 | Decorator | 구조 |
| 복잡한 서브시스템을 단순화할 때 | Facade | 구조 |
| 대량의 객체를 효율적으로 관리할 때 | Flyweight | 구조 |
| 객체 접근을 제어할 때 | Proxy | 구조 |
| 요청을 체인으로 처리할 때 | Chain of Responsibility | 행위 |
| 요청을 객체로 캡슐화할 때 | Command | 행위 |
| 컬렉션을 순회할 때 | Iterator | 행위 |
| 객체 간 통신을 중재할 때 | Mediator | 행위 |
| 객체 상태를 저장/복원할 때 | Memento | 행위 |
| 상태 변화를 통지할 때 | Observer | 행위 |
| 상태에 따라 행동을 변경할 때 | State | 행위 |
| 알고리즘을 교환 가능하게 할 때 | Strategy | 행위 |
| 알고리즘 골격을 정의할 때 | Template Method | 행위 |
| 객체 구조에서 연산을 분리할 때 | Visitor | 행위 |
| 간단한 언어를 해석할 때 | Interpreter | 행위 |

---

## 추가 예정

- 알고리즘 패턴
- 아키텍처 패턴 (MVC, MVP, MVVM, Clean Architecture)
- 동시성 패턴
