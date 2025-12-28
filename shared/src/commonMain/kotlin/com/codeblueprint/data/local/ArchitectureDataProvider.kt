package com.codeblueprint.data.local

import com.codeblueprint.domain.model.ArchitectureComparison
import com.codeblueprint.domain.model.ArchitectureLayer
import com.codeblueprint.domain.model.ArchitecturePattern

/**
 * 아키텍처 패턴 데이터 제공자
 *
 * 정적 데이터로 5가지 아키텍처 패턴 제공:
 * - MVC (Model-View-Controller)
 * - MVP (Model-View-Presenter)
 * - MVVM (Model-View-ViewModel)
 * - Clean Architecture
 * - MVI (Model-View-Intent)
 */
object ArchitectureDataProvider {

    fun getArchitectures(): List<ArchitecturePattern> = listOf(
        createMVC(),
        createMVP(),
        createMVVM(),
        createCleanArchitecture(),
        createMVI()
    )

    private fun createMVC() = ArchitecturePattern(
        id = "mvc",
        name = "MVC",
        koreanName = "모델-뷰-컨트롤러",
        description = "애플리케이션을 Model, View, Controller 세 가지 역할로 분리하는 아키텍처 패턴입니다. 사용자 인터페이스와 비즈니스 로직을 분리하여 유지보수성을 높입니다.",
        layers = listOf(
            ArchitectureLayer(
                name = "Model",
                koreanName = "모델",
                description = "애플리케이션의 데이터와 비즈니스 로직을 담당",
                responsibilities = listOf(
                    "데이터 저장 및 관리",
                    "비즈니스 규칙 정의",
                    "데이터 유효성 검증"
                )
            ),
            ArchitectureLayer(
                name = "View",
                koreanName = "뷰",
                description = "사용자 인터페이스를 담당",
                responsibilities = listOf(
                    "UI 렌더링",
                    "사용자 입력 수신",
                    "Model 데이터 표시"
                )
            ),
            ArchitectureLayer(
                name = "Controller",
                koreanName = "컨트롤러",
                description = "Model과 View 사이의 중재자 역할",
                responsibilities = listOf(
                    "사용자 요청 처리",
                    "Model 업데이트 명령",
                    "적절한 View 선택"
                )
            )
        ),
        diagram = """
            ┌──────────┐      ┌──────────┐
            │   View   │ ←──  │Controller│
            └────┬─────┘      └────┬─────┘
                 │                 │
                 │   ┌──────────┐  │
                 └── │  Model   │ ←┘
                     └──────────┘
        """.trimIndent(),
        pros = listOf(
            "관심사 분리로 코드 구조화",
            "간단하고 이해하기 쉬움",
            "오랜 역사와 풍부한 자료"
        ),
        cons = listOf(
            "Controller가 비대해지기 쉬움 (Massive Controller)",
            "View와 Model 간의 의존성",
            "단위 테스트가 어려움"
        ),
        useCases = listOf(
            "웹 애플리케이션 (Spring MVC, Ruby on Rails)",
            "간단한 데스크톱 애플리케이션",
            "레거시 시스템"
        ),
        comparison = ArchitectureComparison(
            complexity = 2,
            testability = 2,
            scalability = 2,
            maintainability = 2,
            learningCurve = 1
        ),
        androidRecommendation = false
    )

    private fun createMVP() = ArchitecturePattern(
        id = "mvp",
        name = "MVP",
        koreanName = "모델-뷰-프레젠터",
        description = "MVC에서 파생된 패턴으로, Controller 대신 Presenter를 사용하여 View와 Model을 완전히 분리합니다. View와 Model은 Presenter를 통해서만 통신합니다.",
        layers = listOf(
            ArchitectureLayer(
                name = "Model",
                koreanName = "모델",
                description = "데이터와 비즈니스 로직 담당",
                responsibilities = listOf(
                    "데이터 저장 및 관리",
                    "비즈니스 로직 처리",
                    "Repository 패턴으로 데이터 접근"
                )
            ),
            ArchitectureLayer(
                name = "View",
                koreanName = "뷰",
                description = "UI 표시만 담당하는 수동적 컴포넌트",
                responsibilities = listOf(
                    "UI 렌더링",
                    "사용자 입력을 Presenter에 전달",
                    "Presenter의 명령에 따라 UI 업데이트"
                )
            ),
            ArchitectureLayer(
                name = "Presenter",
                koreanName = "프레젠터",
                description = "View와 Model 사이의 중재자",
                responsibilities = listOf(
                    "UI 로직 처리",
                    "View와 Model 간의 데이터 변환",
                    "사용자 액션에 대한 비즈니스 로직 호출"
                )
            )
        ),
        diagram = """
            ┌──────────┐      ┌──────────┐
            │   View   │ ←─→  │Presenter │
            └──────────┘      └────┬─────┘
                                   │
                              ┌────┴─────┐
                              │  Model   │
                              └──────────┘
        """.trimIndent(),
        pros = listOf(
            "View와 Model의 완전한 분리",
            "단위 테스트 용이 (Presenter)",
            "View 재사용성 향상"
        ),
        cons = listOf(
            "보일러플레이트 코드 증가",
            "Presenter가 비대해질 수 있음",
            "View 인터페이스 정의 필요"
        ),
        useCases = listOf(
            "Android 초기 권장 패턴",
            "테스트가 중요한 프로젝트",
            "복잡한 UI 로직이 있는 화면"
        ),
        comparison = ArchitectureComparison(
            complexity = 3,
            testability = 4,
            scalability = 3,
            maintainability = 3,
            learningCurve = 2
        ),
        androidRecommendation = false
    )

    private fun createMVVM() = ArchitecturePattern(
        id = "mvvm",
        name = "MVVM",
        koreanName = "모델-뷰-뷰모델",
        description = "데이터 바인딩을 통해 View와 ViewModel을 연결하는 패턴입니다. ViewModel은 View의 상태를 Observable 형태로 노출하고, View는 이를 관찰하여 자동으로 UI를 업데이트합니다.",
        layers = listOf(
            ArchitectureLayer(
                name = "Model",
                koreanName = "모델",
                description = "데이터와 비즈니스 로직 담당",
                responsibilities = listOf(
                    "데이터 소스 관리 (Local/Remote)",
                    "Repository 패턴 구현",
                    "도메인 모델 정의"
                )
            ),
            ArchitectureLayer(
                name = "View",
                koreanName = "뷰",
                description = "UI 표시 및 사용자 상호작용",
                responsibilities = listOf(
                    "UI 렌더링 (Activity, Fragment, Compose)",
                    "ViewModel 상태 관찰",
                    "사용자 이벤트를 ViewModel에 전달"
                )
            ),
            ArchitectureLayer(
                name = "ViewModel",
                koreanName = "뷰모델",
                description = "View의 상태 관리 및 비즈니스 로직 조정",
                responsibilities = listOf(
                    "UI 상태 관리 (StateFlow, LiveData)",
                    "View와 Model 사이의 데이터 변환",
                    "생명주기 독립적인 데이터 보존"
                )
            )
        ),
        diagram = """
            ┌──────────┐ observes ┌──────────┐
            │   View   │ ───────→ │ViewModel │
            └──────────┘          └────┬─────┘
                                       │
                                  ┌────┴─────┐
                                  │  Model   │
                                  └──────────┘
        """.trimIndent(),
        pros = listOf(
            "데이터 바인딩으로 보일러플레이트 감소",
            "View와 비즈니스 로직의 완전한 분리",
            "생명주기 관리 용이 (Android ViewModel)",
            "단위 테스트 용이"
        ),
        cons = listOf(
            "데이터 바인딩 오버헤드",
            "복잡한 UI 상태 관리 시 어려움",
            "ViewModel이 비대해질 수 있음"
        ),
        useCases = listOf(
            "Android 앱 개발 (권장)",
            "WPF, UWP 애플리케이션",
            "반응형 UI가 필요한 프로젝트"
        ),
        comparison = ArchitectureComparison(
            complexity = 3,
            testability = 4,
            scalability = 4,
            maintainability = 4,
            learningCurve = 3
        ),
        androidRecommendation = true
    )

    private fun createCleanArchitecture() = ArchitecturePattern(
        id = "clean_architecture",
        name = "Clean Architecture",
        koreanName = "클린 아키텍처",
        description = "Uncle Bob(Robert C. Martin)이 제안한 아키텍처로, 의존성 규칙을 통해 계층을 분리합니다. 안쪽 계층은 바깥쪽 계층에 대해 알지 못하며, 이를 통해 비즈니스 로직을 프레임워크와 UI로부터 독립시킵니다.",
        layers = listOf(
            ArchitectureLayer(
                name = "Entities (Domain)",
                koreanName = "엔티티 (도메인)",
                description = "핵심 비즈니스 규칙",
                responsibilities = listOf(
                    "도메인 모델 정의",
                    "비즈니스 규칙 캡슐화",
                    "프레임워크 독립적"
                )
            ),
            ArchitectureLayer(
                name = "Use Cases",
                koreanName = "유스케이스",
                description = "애플리케이션 비즈니스 규칙",
                responsibilities = listOf(
                    "비즈니스 로직 조정",
                    "엔티티 간의 데이터 흐름 조정",
                    "Repository 인터페이스 정의"
                )
            ),
            ArchitectureLayer(
                name = "Interface Adapters",
                koreanName = "인터페이스 어댑터",
                description = "데이터 변환 계층",
                responsibilities = listOf(
                    "ViewModel, Presenter, Controller",
                    "Repository 구현",
                    "데이터 매핑 (DTO ↔ Entity)"
                )
            ),
            ArchitectureLayer(
                name = "Frameworks & Drivers",
                koreanName = "프레임워크 & 드라이버",
                description = "외부 인터페이스",
                responsibilities = listOf(
                    "UI Framework (Compose, XML)",
                    "Database (Room, SQLDelight)",
                    "Network (Retrofit, Ktor)"
                )
            )
        ),
        diagram = """
            ┌─────────────────────────────────────┐
            │        Frameworks & Drivers          │
            │  ┌─────────────────────────────┐    │
            │  │    Interface Adapters       │    │
            │  │  ┌───────────────────────┐  │    │
            │  │  │      Use Cases        │  │    │
            │  │  │  ┌─────────────────┐  │  │    │
            │  │  │  │    Entities     │  │  │    │
            │  │  │  └─────────────────┘  │  │    │
            │  │  └───────────────────────┘  │    │
            │  └─────────────────────────────┘    │
            └─────────────────────────────────────┘
                 Dependency Rule: → inward
        """.trimIndent(),
        pros = listOf(
            "프레임워크 독립성",
            "테스트 용이성 (순수 Kotlin 도메인)",
            "UI 독립성",
            "데이터베이스 독립성",
            "비즈니스 로직 보호"
        ),
        cons = listOf(
            "보일러플레이트 코드 증가",
            "초기 설정 시간 소요",
            "과도한 추상화 위험",
            "학습 곡선이 높음"
        ),
        useCases = listOf(
            "대규모 엔터프라이즈 앱",
            "장기 유지보수 프로젝트",
            "다중 플랫폼 비즈니스 로직 공유",
            "테스트 커버리지가 중요한 프로젝트"
        ),
        comparison = ArchitectureComparison(
            complexity = 5,
            testability = 5,
            scalability = 5,
            maintainability = 5,
            learningCurve = 5
        ),
        androidRecommendation = true
    )

    private fun createMVI() = ArchitecturePattern(
        id = "mvi",
        name = "MVI",
        koreanName = "모델-뷰-인텐트",
        description = "단방향 데이터 흐름을 강조하는 반응형 아키텍처 패턴입니다. 모든 UI 변경은 Intent를 통해 시작되고, Model은 불변 상태로 관리됩니다.",
        layers = listOf(
            ArchitectureLayer(
                name = "Model (State)",
                koreanName = "모델 (상태)",
                description = "불변 UI 상태",
                responsibilities = listOf(
                    "불변 상태 객체 정의",
                    "UI 상태 완전 표현",
                    "상태 기반 UI 렌더링"
                )
            ),
            ArchitectureLayer(
                name = "View",
                koreanName = "뷰",
                description = "상태 기반 UI 렌더링",
                responsibilities = listOf(
                    "Model(State) 관찰 및 렌더링",
                    "사용자 Intent 생성",
                    "단방향 데이터 흐름 유지"
                )
            ),
            ArchitectureLayer(
                name = "Intent",
                koreanName = "인텐트",
                description = "사용자 의도 표현",
                responsibilities = listOf(
                    "사용자 액션 정의",
                    "상태 변경 요청",
                    "Reducer로 상태 변환"
                )
            )
        ),
        diagram = """
            User → Intent → Reducer → State → View → User
                      ↑                           │
                      └───────────────────────────┘
                         Unidirectional Data Flow
        """.trimIndent(),
        pros = listOf(
            "예측 가능한 상태 관리",
            "단방향 데이터 흐름으로 디버깅 용이",
            "상태 기반 UI로 일관성 보장",
            "시간 여행 디버깅 가능"
        ),
        cons = listOf(
            "보일러플레이트 코드 (Intent, State)",
            "간단한 UI에는 과한 구조",
            "상태 객체가 커질 수 있음",
            "학습 곡선"
        ),
        useCases = listOf(
            "복잡한 UI 상태 관리",
            "Compose/Flutter 기반 앱",
            "Redux 스타일 상태 관리 선호 시",
            "상태 기반 테스팅"
        ),
        comparison = ArchitectureComparison(
            complexity = 4,
            testability = 5,
            scalability = 4,
            maintainability = 4,
            learningCurve = 4
        ),
        androidRecommendation = true
    )
}
