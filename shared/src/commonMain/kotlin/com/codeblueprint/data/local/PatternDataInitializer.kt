package com.codeblueprint.data.local

import com.codeblueprint.data.mapper.CodeExampleMapper
import com.codeblueprint.data.mapper.PatternMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.model.ProgrammingLanguage

/**
 * 패턴 초기 데이터 생성기
 *
 * 데이터베이스가 최초 생성될 때 23개의 GoF 디자인 패턴 데이터를 삽입합니다.
 */
class PatternDataInitializer(
    private val database: CodeBlueprintDatabase,
    private val patternMapper: PatternMapper,
    private val codeExampleMapper: CodeExampleMapper = CodeExampleMapper()
) {
    private val queries = database.codeBlueprintQueries

    /**
     * 초기 패턴 데이터 삽입
     */
    suspend fun initializeIfNeeded() {
        val count = queries.getPatternCount().executeAsOne()
        if (count == 0L) {
            insertInitialPatterns()
        }
    }

    private fun insertInitialPatterns() {
        val patterns = createInitialPatterns()
        patterns.forEach { pattern ->
            // 패턴 삽입 (기존 로직 유지)
            val values = patternMapper.toEntityValues(pattern)
            queries.insertPattern(
                id = values.id,
                name = values.name,
                korean_name = values.koreanName,
                category = values.category,
                purpose = values.purpose,
                characteristics = values.characteristics,
                advantages = values.advantages,
                disadvantages = values.disadvantages,
                use_cases = values.useCases,
                code_examples = values.codeExamples,
                diagram = values.diagram,
                related_pattern_ids = values.relatedPatternIds,
                difficulty = values.difficulty,
                frequency = values.frequency
            )

            // 코드 예제를 새 테이블에 삽입
            pattern.codeExamples.forEachIndexed { index, codeExample ->
                val exampleValues = codeExampleMapper.toEntityValues(
                    domain = codeExample,
                    patternId = pattern.id,
                    algorithmId = null,
                    index = index
                )
                queries.insertCodeExample(
                    id = exampleValues.id,
                    pattern_id = exampleValues.patternId,
                    algorithm_id = exampleValues.algorithmId,
                    language = exampleValues.language,
                    code = exampleValues.code,
                    explanation = exampleValues.explanation,
                    sample_input = exampleValues.sampleInput,
                    expected_output = exampleValues.expectedOutput,
                    display_order = exampleValues.displayOrder,
                    created_at = exampleValues.createdAt,
                    updated_at = exampleValues.updatedAt
                )
            }
        }
    }

    private fun createInitialPatterns(): List<DesignPattern> {
        return listOf(
            // 생성 패턴 (5개)
            createSingleton(),
            createFactoryMethod(),
            createAbstractFactory(),
            createBuilder(),
            createPrototype(),
            // 구조 패턴 (7개)
            createAdapter(),
            createBridge(),
            createComposite(),
            createDecorator(),
            createFacade(),
            createFlyweight(),
            createProxy(),
            // 행위 패턴 (11개)
            createChainOfResponsibility(),
            createCommand(),
            createIterator(),
            createMediator(),
            createMemento(),
            createObserver(),
            createState(),
            createStrategy(),
            createTemplateMethod(),
            createVisitor(),
            createInterpreter()
        )
    }

    // ==================== 생성 패턴 ====================

    private fun createSingleton() = DesignPattern(
        id = "singleton",
        name = "Singleton",
        koreanName = "싱글톤",
        category = PatternCategory.CREATIONAL,
        purpose = "클래스의 인스턴스를 오직 하나만 생성하도록 보장하고, 전역적인 접근점을 제공합니다.",
        characteristics = listOf(
            "전역적으로 접근 가능한 단일 인스턴스",
            "인스턴스 생성 제어",
            "지연 초기화(Lazy Initialization) 가능"
        ),
        advantages = listOf(
            "메모리 낭비 방지",
            "전역 상태 관리 용이",
            "리소스 공유 효율적"
        ),
        disadvantages = listOf(
            "전역 상태로 인한 결합도 증가",
            "멀티스레드 환경에서 동기화 필요",
            "테스트 어려움 (Mock 객체 생성 어려움)",
            "단일 책임 원칙 위반 가능"
        ),
        useCases = listOf(
            "데이터베이스 연결 풀",
            "로거(Logger) 클래스",
            "설정(Configuration) 관리자",
            "캐시 관리 시스템"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
object Singleton {
    fun doSomething() {
        println("Hello from Singleton")
    }
}

// 사용
fun main() {
    Singleton.doSomething()
    println(Singleton === Singleton) // true
}
                """.trimIndent(),
                explanation = "Kotlin의 object 키워드를 사용한 싱글톤 구현",
                expectedOutput = """
Hello from Singleton
true
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Singleton {
        -instance: Singleton
        -Singleton()
        +getInstance(): Singleton
        +doSomething()
    }
        """.trimIndent(),
        relatedPatternIds = listOf("factory_method", "prototype"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createFactoryMethod() = DesignPattern(
        id = "factory_method",
        name = "Factory Method",
        koreanName = "팩토리 메서드",
        category = PatternCategory.CREATIONAL,
        purpose = "객체 생성을 서브클래스에 위임하여, 어떤 클래스의 인스턴스를 생성할지 서브클래스가 결정하도록 합니다.",
        characteristics = listOf(
            "인터페이스를 통해 객체 생성",
            "구체적인 클래스는 서브클래스가 결정",
            "생성 로직의 캡슐화"
        ),
        advantages = listOf(
            "객체 생성 코드와 사용 코드 분리",
            "개방-폐쇄 원칙(OCP) 준수",
            "새로운 타입 추가 시 기존 코드 수정 불필요",
            "단일 책임 원칙 준수"
        ),
        disadvantages = listOf(
            "클래스 개수 증가로 코드 복잡도 증가",
            "간단한 객체 생성에는 과도한 설계"
        ),
        useCases = listOf(
            "UI 컴포넌트 생성 (Button, Dialog)",
            "문서 생성기 (PDF, Word, Excel)",
            "데이터베이스 연결 (MySQL, PostgreSQL)",
            "로깅 프레임워크"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Product {
    fun use()
}

class ConcreteProductA : Product {
    override fun use() = println("Product A 사용")
}

class ConcreteProductB : Product {
    override fun use() = println("Product B 사용")
}

abstract class Creator {
    abstract fun factoryMethod(): Product

    fun operation() {
        val product = factoryMethod()
        product.use()
    }
}

class ConcreteCreatorA : Creator() {
    override fun factoryMethod() = ConcreteProductA()
}
                """.trimIndent(),
                explanation = "추상 팩토리 메서드를 통한 객체 생성 위임",
                expectedOutput = """
Product A 사용
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Creator {
        <<abstract>>
        +factoryMethod(): Product
        +operation()
    }
    class ConcreteCreator {
        +factoryMethod(): Product
    }
    class Product {
        <<interface>>
        +use()
    }
    Creator <|-- ConcreteCreator
    Product <|.. ConcreteProduct
        """.trimIndent(),
        relatedPatternIds = listOf("abstract_factory", "singleton"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createAbstractFactory() = DesignPattern(
        id = "abstract_factory",
        name = "Abstract Factory",
        koreanName = "추상 팩토리",
        category = PatternCategory.CREATIONAL,
        purpose = "관련된 객체들의 패밀리를 생성하기 위한 인터페이스를 제공합니다.",
        characteristics = listOf(
            "관련 객체 집합 생성",
            "구체 클래스 분리",
            "제품군 교체 용이"
        ),
        advantages = listOf(
            "제품군의 일관성 보장",
            "구체 클래스 분리로 클라이언트 코드 독립성 확보",
            "제품군 교체 용이"
        ),
        disadvantages = listOf(
            "새로운 제품 추가 시 모든 팩토리 수정 필요",
            "코드 복잡도 증가"
        ),
        useCases = listOf(
            "크로스 플랫폼 UI 툴킷",
            "데이터베이스 드라이버 전체 컴포넌트",
            "테마 시스템 (다크 모드, 라이트 모드)"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Button { fun render() }
interface Checkbox { fun render() }

interface GUIFactory {
    fun createButton(): Button
    fun createCheckbox(): Checkbox
}

class WindowsFactory : GUIFactory {
    override fun createButton() = WindowsButton()
    override fun createCheckbox() = WindowsCheckbox()
}
                """.trimIndent(),
                explanation = "플랫폼별 UI 컴포넌트 생성을 추상화",
                expectedOutput = """
Windows Button 렌더링
Windows Checkbox 렌더링
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class AbstractFactory {
        <<interface>>
        +createProductA(): ProductA
        +createProductB(): ProductB
    }
        """.trimIndent(),
        relatedPatternIds = listOf("factory_method", "builder"),
        difficulty = Difficulty.HIGH,
        frequency = 3
    )

    private fun createBuilder() = DesignPattern(
        id = "builder",
        name = "Builder",
        koreanName = "빌더",
        category = PatternCategory.CREATIONAL,
        purpose = "복잡한 객체의 생성 과정과 표현 방법을 분리하여, 동일한 생성 절차에서 다른 표현 결과를 만들 수 있게 합니다.",
        characteristics = listOf(
            "단계별 객체 생성",
            "동일한 생성 절차로 다른 표현 생성",
            "생성 과정과 표현 분리"
        ),
        advantages = listOf(
            "가독성 높은 객체 생성 코드",
            "불변 객체 생성 용이",
            "선택적 매개변수 처리 용이"
        ),
        disadvantages = listOf(
            "코드 양 증가",
            "간단한 객체에는 오버엔지니어링"
        ),
        useCases = listOf(
            "StringBuilder, StringBuffer",
            "HTTP 요청 빌더",
            "SQL 쿼리 빌더"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Pizza(val dough: String, val sauce: String, val topping: String) {
    class Builder {
        private var dough: String = "기본 도우"
        private var sauce: String = "토마토"
        private var topping: String = "치즈"

        fun dough(dough: String) = apply { this.dough = dough }
        fun sauce(sauce: String) = apply { this.sauce = sauce }
        fun topping(topping: String) = apply { this.topping = topping }
        fun build() = Pizza(dough, sauce, topping)
    }
}
                """.trimIndent(),
                explanation = "빌더 패턴을 통한 단계적 객체 생성",
                expectedOutput = """
Pizza(dough=씬 도우, sauce=바베큐, topping=페퍼로니)
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Builder {
        <<interface>>
        +buildPartA()
        +buildPartB()
        +getResult(): Product
    }
        """.trimIndent(),
        relatedPatternIds = listOf("abstract_factory", "prototype"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createPrototype() = DesignPattern(
        id = "prototype",
        name = "Prototype",
        koreanName = "프로토타입",
        category = PatternCategory.CREATIONAL,
        purpose = "기존 객체를 복제하여 새로운 객체를 생성합니다.",
        characteristics = listOf(
            "객체 생성 비용이 클 때 사용",
            "복제를 통한 새 인스턴스 생성",
            "런타임에 동적으로 객체 추가 가능"
        ),
        advantages = listOf(
            "객체 생성 비용 절감",
            "복잡한 객체 생성 과정 단순화"
        ),
        disadvantages = listOf(
            "깊은 복사(Deep Copy) 구현이 복잡",
            "순환 참조 문제 발생 가능"
        ),
        useCases = listOf(
            "게임 캐릭터 복제",
            "문서 템플릿 복사"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class Document(val title: String, val content: String) : Cloneable {
    public override fun clone(): Document = copy()
}

val original = Document("제목", "내용")
val cloned = original.clone()
                """.trimIndent(),
                explanation = "Kotlin data class의 copy()를 활용한 프로토타입 패턴",
                expectedOutput = """
Document(title=제목, content=내용)
Document(title=제목, content=내용)
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Prototype {
        <<interface>>
        +clone(): Prototype
    }
        """.trimIndent(),
        relatedPatternIds = listOf("factory_method", "singleton"),
        difficulty = Difficulty.MEDIUM,
        frequency = 2
    )

    // ==================== 구조 패턴 ====================

    private fun createAdapter() = DesignPattern(
        id = "adapter",
        name = "Adapter",
        koreanName = "어댑터",
        category = PatternCategory.STRUCTURAL,
        purpose = "호환되지 않는 인터페이스를 가진 객체들이 협력할 수 있도록 래퍼 역할을 합니다.",
        characteristics = listOf(
            "래퍼(Wrapper) 역할",
            "기존 클래스 수정 없이 호환성 제공",
            "인터페이스 변환"
        ),
        advantages = listOf(
            "기존 코드 수정 없이 호환성 제공",
            "단일 책임 원칙 준수"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가"
        ),
        useCases = listOf(
            "레거시 시스템과 신규 시스템 통합",
            "서드파티 라이브러리 통합"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface MediaPlayer {
    fun play(fileName: String)
}

class MediaAdapter(private val advancedPlayer: AdvancedMediaPlayer) : MediaPlayer {
    override fun play(fileName: String) {
        advancedPlayer.playVlc(fileName)
    }
}
                """.trimIndent(),
                explanation = "미디어 플레이어 인터페이스를 통합하는 어댑터",
                expectedOutput = """
VLC 형식으로 재생: movie.vlc
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Target {
        <<interface>>
        +request()
    }
    class Adapter {
        -adaptee: Adaptee
        +request()
    }
    Target <|.. Adapter
        """.trimIndent(),
        relatedPatternIds = listOf("bridge", "decorator", "proxy"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createBridge() = DesignPattern(
        id = "bridge",
        name = "Bridge",
        koreanName = "브릿지",
        category = PatternCategory.STRUCTURAL,
        purpose = "추상화와 구현을 분리하여 독립적으로 변경할 수 있게 합니다.",
        characteristics = listOf(
            "구현부와 추상부를 별도 클래스 계층으로 분리",
            "복합(Composition) 활용",
            "런타임에 구현 변경 가능"
        ),
        advantages = listOf(
            "추상화와 구현의 독립적 확장",
            "구현 세부사항을 클라이언트로부터 숨김"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가"
        ),
        useCases = listOf(
            "플랫폼 독립적 애플리케이션",
            "그래픽 렌더링 시스템"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface DrawingAPI {
    fun drawCircle(x: Int, y: Int, radius: Int)
}

abstract class Shape(protected val drawingAPI: DrawingAPI) {
    abstract fun draw()
}

class Circle(x: Int, y: Int, radius: Int, api: DrawingAPI) : Shape(api) {
    override fun draw() = drawingAPI.drawCircle(x, y, radius)
}
                """.trimIndent(),
                explanation = "도형과 렌더링 API를 브릿지 패턴으로 분리",
                expectedOutput = """
원 그리기: 중심(100, 100), 반지름(50)
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Abstraction {
        #implementor: Implementor
        +operation()
    }
    Abstraction --> Implementor
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "strategy"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    private fun createComposite() = DesignPattern(
        id = "composite",
        name = "Composite",
        koreanName = "컴포지트",
        category = PatternCategory.STRUCTURAL,
        purpose = "객체들을 트리 구조로 구성하여 부분-전체 계층을 표현합니다.",
        characteristics = listOf(
            "개별 객체와 복합 객체를 동일하게 취급",
            "재귀적 구조",
            "트리 계층 표현"
        ),
        advantages = listOf(
            "복잡한 트리 구조 간편 관리",
            "다형성과 재귀 활용"
        ),
        disadvantages = listOf(
            "설계가 지나치게 일반화될 수 있음"
        ),
        useCases = listOf(
            "파일 시스템 (폴더-파일 구조)",
            "UI 컴포넌트 계층"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Component {
    fun operation(): String
}

class Leaf(private val name: String) : Component {
    override fun operation() = name
}

class Composite(private val name: String) : Component {
    private val children = mutableListOf<Component>()
    fun add(c: Component) { children.add(c) }
    override fun operation() = "${'$'}name[${'$'}{children.map { it.operation() }}]"
}
                """.trimIndent(),
                explanation = "트리 구조를 표현하는 컴포지트 패턴",
                expectedOutput = """
폴더[파일1, 파일2, 하위폴더[파일3]]
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Component {
        <<interface>>
        +operation()
    }
    Component <|.. Leaf
    Component <|.. Composite
        """.trimIndent(),
        relatedPatternIds = listOf("decorator", "iterator"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createDecorator() = DesignPattern(
        id = "decorator",
        name = "Decorator",
        koreanName = "데코레이터",
        category = PatternCategory.STRUCTURAL,
        purpose = "객체에 동적으로 새로운 기능을 추가합니다.",
        characteristics = listOf(
            "상속 없이 객체 확장",
            "래퍼를 통한 기능 추가",
            "런타임에 동적 기능 추가/제거"
        ),
        advantages = listOf(
            "런타임에 동적으로 기능 추가/제거",
            "단일 책임 원칙 준수"
        ),
        disadvantages = listOf(
            "작은 객체들이 많이 생성됨"
        ),
        useCases = listOf(
            "Java I/O Stream",
            "GUI 컴포넌트 스크롤바, 테두리 추가"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Coffee {
    fun cost(): Double
    fun description(): String
}

class SimpleCoffee : Coffee {
    override fun cost() = 1.0
    override fun description() = "커피"
}

class Milk(private val coffee: Coffee) : Coffee {
    override fun cost() = coffee.cost() + 0.5
    override fun description() = coffee.description() + " + 우유"
}
                """.trimIndent(),
                explanation = "커피에 옵션을 추가하는 데코레이터 패턴",
                expectedOutput = """
커피 + 우유
가격: 1.5
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Component {
        <<interface>>
        +operation()
    }
    Decorator o-- Component
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "composite", "strategy"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createFacade() = DesignPattern(
        id = "facade",
        name = "Facade",
        koreanName = "퍼사드",
        category = PatternCategory.STRUCTURAL,
        purpose = "복잡한 서브시스템에 대한 단순화된 인터페이스를 제공합니다.",
        characteristics = listOf(
            "서브시스템의 복잡성을 감춤",
            "고수준 인터페이스 제공"
        ),
        advantages = listOf(
            "시스템 사용 간소화",
            "서브시스템과 클라이언트 간 결합도 감소"
        ),
        disadvantages = listOf(
            "퍼사드가 God Object가 될 수 있음"
        ),
        useCases = listOf(
            "컴파일러 인터페이스",
            "비디오 변환 라이브러리"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class VideoConverter {
    fun convert(fileName: String, format: String): ByteArray {
        val file = VideoFile(fileName)
        val codec = CodecFactory().extract(file)
        val buffer = BitrateReader().read(file, codec)
        return AudioMixer().fix(buffer)
    }
}
                """.trimIndent(),
                explanation = "비디오 변환의 복잡성을 숨기는 퍼사드",
                expectedOutput = """
비디오 변환 완료: movie.mp4 → mp4 형식
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Facade {
        +operation()
    }
    Facade --> SubsystemA
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "mediator"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createFlyweight() = DesignPattern(
        id = "flyweight",
        name = "Flyweight",
        koreanName = "플라이웨이트",
        category = PatternCategory.STRUCTURAL,
        purpose = "공유를 통해 대량의 객체를 효율적으로 지원합니다.",
        characteristics = listOf(
            "메모리 사용 최적화",
            "내재 상태와 외재 상태 분리"
        ),
        advantages = listOf(
            "메모리 사용량 대폭 감소"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가"
        ),
        useCases = listOf(
            "텍스트 에디터의 문자 렌더링",
            "게임의 파티클 시스템"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
object TreeFactory {
    private val treeTypes = mutableMapOf<String, TreeType>()

    fun getTreeType(name: String, color: String): TreeType {
        return treeTypes.getOrPut("${'$'}name-${'$'}color") {
            TreeType(name, color)
        }
    }
}
                """.trimIndent(),
                explanation = "나무 객체의 공통 속성을 공유하는 플라이웨이트",
                expectedOutput = """
TreeType 생성: 소나무-녹색
캐시에서 재사용: 소나무-녹색
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class FlyweightFactory {
        -flyweights: Map
        +getFlyweight(key): Flyweight
    }
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "singleton"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    private fun createProxy() = DesignPattern(
        id = "proxy",
        name = "Proxy",
        koreanName = "프록시",
        category = PatternCategory.STRUCTURAL,
        purpose = "다른 객체에 대한 접근을 제어하는 대리자를 제공합니다.",
        characteristics = listOf(
            "실제 객체에 대한 간접 참조",
            "추가 기능 제공 (로깅, 캐싱, 접근 제어)"
        ),
        advantages = listOf(
            "실제 객체 접근 제어",
            "지연 초기화 (Lazy Initialization)"
        ),
        disadvantages = listOf(
            "응답 시간 증가 가능"
        ),
        useCases = listOf(
            "가상 프록시 (이미지 지연 로딩)",
            "보호 프록시 (접근 권한 제어)"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
class ProxyImage(private val fileName: String) : Image {
    private var realImage: RealImage? = null

    override fun display() {
        if (realImage == null) {
            realImage = RealImage(fileName)
        }
        realImage?.display()
    }
}
                """.trimIndent(),
                explanation = "이미지 지연 로딩을 위한 가상 프록시",
                expectedOutput = """
이미지 로딩: photo.jpg
이미지 표시: photo.jpg
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Subject {
        <<interface>>
        +request()
    }
    Proxy --> RealSubject
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "decorator"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    // ==================== 행위 패턴 ====================

    private fun createChainOfResponsibility() = DesignPattern(
        id = "chain_of_responsibility",
        name = "Chain of Responsibility",
        koreanName = "책임 연쇄",
        category = PatternCategory.BEHAVIORAL,
        purpose = "요청을 처리할 객체를 체인 형태로 연결하여, 요청이 처리될 때까지 체인을 따라 전달합니다.",
        characteristics = listOf(
            "요청이 처리될 때까지 체인을 따라 전달",
            "송신자와 수신자 분리"
        ),
        advantages = listOf(
            "요청 송신자와 수신자 분리",
            "처리자 동적 추가/제거 가능"
        ),
        disadvantages = listOf(
            "요청이 처리되지 않을 수 있음"
        ),
        useCases = listOf(
            "이벤트 버블링",
            "로깅 시스템"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
abstract class Handler {
    private var next: Handler? = null
    fun setNext(handler: Handler): Handler {
        next = handler
        return handler
    }
    open fun handle(request: String): String? = next?.handle(request)
}
                """.trimIndent(),
                explanation = "고객 지원 시스템의 책임 연쇄 패턴",
                expectedOutput = """
FAQ에서 처리됨: 비밀번호 재설정
기술지원팀에서 처리됨: 서버 오류
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Handler {
        -next: Handler
        +handle(request): String
    }
        """.trimIndent(),
        relatedPatternIds = listOf("command", "mediator"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createCommand() = DesignPattern(
        id = "command",
        name = "Command",
        koreanName = "커맨드",
        category = PatternCategory.BEHAVIORAL,
        purpose = "요청을 객체로 캡슐화하여, 요청의 실행, 취소, 재실행을 가능하게 합니다.",
        characteristics = listOf(
            "요청을 실행, 취소, 재실행 가능",
            "요청 송신자와 수신자 분리"
        ),
        advantages = listOf(
            "Undo/Redo 구현 용이",
            "명령 큐 및 로깅 가능"
        ),
        disadvantages = listOf(
            "커맨드 클래스 수 증가"
        ),
        useCases = listOf(
            "GUI 버튼 동작",
            "작업 취소/재실행"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Command {
    fun execute()
    fun undo()
}

class LightOnCommand(private val light: Light) : Command {
    override fun execute() = light.on()
    override fun undo() = light.off()
}
                """.trimIndent(),
                explanation = "리모컨의 명령을 객체화하는 커맨드 패턴",
                expectedOutput = """
조명 켜기
조명 끄기 (Undo)
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Command {
        <<interface>>
        +execute()
        +undo()
    }
        """.trimIndent(),
        relatedPatternIds = listOf("memento", "chain_of_responsibility"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createIterator() = DesignPattern(
        id = "iterator",
        name = "Iterator",
        koreanName = "반복자",
        category = PatternCategory.BEHAVIORAL,
        purpose = "내부 구조를 노출하지 않고 컬렉션 요소에 순차적으로 접근하는 방법을 제공합니다.",
        characteristics = listOf(
            "통일된 인터페이스로 다양한 컬렉션 순회",
            "컬렉션과 순회 알고리즘 분리"
        ),
        advantages = listOf(
            "컬렉션 구현과 순회 알고리즘 분리",
            "단일 책임 원칙 준수"
        ),
        disadvantages = listOf(
            "간단한 컬렉션에는 오버킬"
        ),
        useCases = listOf(
            "Java Collection Framework",
            "데이터베이스 결과셋 순회"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Iterator<T> {
    fun hasNext(): Boolean
    fun next(): T
}

class WordsIterator(private val words: List<String>) : Iterator<String> {
    private var index = 0
    override fun hasNext() = index < words.size
    override fun next() = words[index++]
}
                """.trimIndent(),
                explanation = "컬렉션의 요소를 순회하는 반복자 패턴",
                expectedOutput = """
Hello
World
Iterator
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Iterator {
        <<interface>>
        +hasNext(): Boolean
        +next(): T
    }
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "visitor"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createMediator() = DesignPattern(
        id = "mediator",
        name = "Mediator",
        koreanName = "중재자",
        category = PatternCategory.BEHAVIORAL,
        purpose = "객체 간 직접 통신을 제한하고, 중재자를 통해서만 협력하도록 합니다.",
        characteristics = listOf(
            "다대다 관계를 일대다 관계로 단순화",
            "중앙집중식 제어"
        ),
        advantages = listOf(
            "컴포넌트 간 결합도 감소",
            "통신 로직 중앙화"
        ),
        disadvantages = listOf(
            "중재자가 복잡해질 수 있음"
        ),
        useCases = listOf(
            "채팅방 시스템",
            "공항 관제탑"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface ChatMediator {
    fun sendMessage(message: String, user: User)
}

class ChatRoom : ChatMediator {
    private val users = mutableListOf<User>()
    override fun sendMessage(message: String, sender: User) {
        users.filter { it != sender }.forEach { it.receive(message) }
    }
}
                """.trimIndent(),
                explanation = "채팅방의 메시지 전달을 중재하는 패턴",
                expectedOutput = """
[Alice → 채팅방]: 안녕하세요!
[Bob] 메시지 수신: 안녕하세요!
[Charlie] 메시지 수신: 안녕하세요!
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Mediator {
        <<interface>>
        +notify(sender, event)
    }
        """.trimIndent(),
        relatedPatternIds = listOf("facade", "observer"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createMemento() = DesignPattern(
        id = "memento",
        name = "Memento",
        koreanName = "메멘토",
        category = PatternCategory.BEHAVIORAL,
        purpose = "캡슐화를 위배하지 않으면서 객체의 내부 상태를 저장하고 복원합니다.",
        characteristics = listOf(
            "캡슐화를 위배하지 않음",
            "상태 이력 관리"
        ),
        advantages = listOf(
            "Undo/Redo 구현 용이",
            "객체 상태 이력 관리"
        ),
        disadvantages = listOf(
            "메모리 사용량 증가"
        ),
        useCases = listOf(
            "텍스트 에디터 Undo",
            "게임 세이브/로드"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
data class EditorMemento(val content: String)

class Editor {
    var content: String = ""
    fun save(): EditorMemento = EditorMemento(content)
    fun restore(memento: EditorMemento) { content = memento.content }
}
                """.trimIndent(),
                explanation = "텍스트 에디터의 Undo 기능 구현",
                expectedOutput = """
현재 내용: Hello World
저장됨
현재 내용: Hello World, Modified
복원됨
현재 내용: Hello World
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Originator {
        +save(): Memento
        +restore(Memento)
    }
        """.trimIndent(),
        relatedPatternIds = listOf("command", "iterator"),
        difficulty = Difficulty.MEDIUM,
        frequency = 3
    )

    private fun createObserver() = DesignPattern(
        id = "observer",
        name = "Observer",
        koreanName = "옵저버",
        category = PatternCategory.BEHAVIORAL,
        purpose = "객체 상태 변화를 관찰자들에게 자동으로 통지합니다.",
        characteristics = listOf(
            "일대다 의존성 정의",
            "발행-구독 모델"
        ),
        advantages = listOf(
            "느슨한 결합",
            "동적으로 구독자 추가/제거"
        ),
        disadvantages = listOf(
            "메모리 누수 위험"
        ),
        useCases = listOf(
            "이벤트 리스너",
            "MVC 패턴"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Observer { fun update(message: String) }

class Subject {
    private val observers = mutableListOf<Observer>()
    fun attach(observer: Observer) { observers.add(observer) }
    fun notify(message: String) { observers.forEach { it.update(message) } }
}
                """.trimIndent(),
                explanation = "상태 변화를 구독자들에게 알리는 옵저버 패턴",
                expectedOutput = """
Observer1: 새 알림 수신
Observer2: 새 알림 수신
Observer3: 새 알림 수신
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Subject {
        -observers: List~Observer~
        +attach(Observer)
        +notify()
    }
        """.trimIndent(),
        relatedPatternIds = listOf("mediator", "strategy"),
        difficulty = Difficulty.MEDIUM,
        frequency = 5
    )

    private fun createState() = DesignPattern(
        id = "state",
        name = "State",
        koreanName = "상태",
        category = PatternCategory.BEHAVIORAL,
        purpose = "객체의 내부 상태에 따라 행동을 변경합니다.",
        characteristics = listOf(
            "상태를 별도 클래스로 캡슐화",
            "상태 전이 관리"
        ),
        advantages = listOf(
            "단일 책임 원칙 준수",
            "조건문 제거로 코드 간소화"
        ),
        disadvantages = listOf(
            "상태가 적으면 과도한 설계"
        ),
        useCases = listOf(
            "TCP 연결 상태",
            "주문 상태"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface State { fun handle(context: Context) }

class Context {
    var state: State = ConcreteStateA()
    fun request() { state.handle(this) }
}

class ConcreteStateA : State {
    override fun handle(context: Context) {
        println("State A → State B로 전이")
        context.state = ConcreteStateB()
    }
}
                """.trimIndent(),
                explanation = "상태에 따라 행동이 변하는 상태 패턴",
                expectedOutput = """
State A → State B로 전이
State B → State A로 전이
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Context {
        -state: State
        +request()
    }
        """.trimIndent(),
        relatedPatternIds = listOf("strategy", "singleton"),
        difficulty = Difficulty.MEDIUM,
        frequency = 4
    )

    private fun createStrategy() = DesignPattern(
        id = "strategy",
        name = "Strategy",
        koreanName = "전략",
        category = PatternCategory.BEHAVIORAL,
        purpose = "알고리즘 군을 정의하고 캡슐화하여 교환 가능하게 만듭니다.",
        characteristics = listOf(
            "런타임에 알고리즘 변경 가능",
            "조건문 대체"
        ),
        advantages = listOf(
            "개방-폐쇄 원칙 준수",
            "런타임에 알고리즘 선택"
        ),
        disadvantages = listOf(
            "전략 객체 수 증가"
        ),
        useCases = listOf(
            "정렬 알고리즘",
            "결제 방식"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface PaymentStrategy { fun pay(amount: Int) }

class CreditCardStrategy(val cardNumber: String) : PaymentStrategy {
    override fun pay(amount: Int) = println("${'$'}amount원을 카드로 결제")
}

class ShoppingCart {
    private var paymentStrategy: PaymentStrategy? = null
    fun setPaymentStrategy(strategy: PaymentStrategy) { paymentStrategy = strategy }
    fun checkout(amount: Int) { paymentStrategy?.pay(amount) }
}
                """.trimIndent(),
                explanation = "결제 방식을 교체 가능하게 하는 전략 패턴",
                expectedOutput = """
10000원을 카드로 결제
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Context {
        -strategy: Strategy
        +setStrategy(Strategy)
    }
        """.trimIndent(),
        relatedPatternIds = listOf("state", "template_method"),
        difficulty = Difficulty.LOW,
        frequency = 5
    )

    private fun createTemplateMethod() = DesignPattern(
        id = "template_method",
        name = "Template Method",
        koreanName = "템플릿 메서드",
        category = PatternCategory.BEHAVIORAL,
        purpose = "알고리즘의 골격을 정의하고, 세부 단계는 서브클래스에서 구현합니다.",
        characteristics = listOf(
            "코드 재사용 극대화",
            "불변 부분과 가변 부분 분리"
        ),
        advantages = listOf(
            "코드 중복 제거",
            "프레임워크 구축에 유용"
        ),
        disadvantages = listOf(
            "템플릿 메서드가 많아지면 유지보수 어려움"
        ),
        useCases = listOf(
            "프레임워크 라이프사이클 메서드",
            "테스트 프레임워크"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
abstract class DataMiner {
    fun mine(path: String) {
        val file = openFile(path)
        val data = extractData(file)
        sendReport(analyzeData(data))
    }
    abstract fun openFile(path: String): String
    abstract fun extractData(file: String): String
    abstract fun analyzeData(data: String): String
    open fun sendReport(analysis: String) = println("Report: ${'$'}analysis")
}
                """.trimIndent(),
                explanation = "데이터 마이닝 알고리즘의 골격을 정의하는 템플릿 메서드",
                expectedOutput = """
파일 열기: data.csv
데이터 추출 완료
Report: 분석 결과 - 총 100건의 데이터
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class AbstractClass {
        +templateMethod()
        #primitiveOperation1()
    }
        """.trimIndent(),
        relatedPatternIds = listOf("strategy", "factory_method"),
        difficulty = Difficulty.LOW,
        frequency = 4
    )

    private fun createVisitor() = DesignPattern(
        id = "visitor",
        name = "Visitor",
        koreanName = "방문자",
        category = PatternCategory.BEHAVIORAL,
        purpose = "객체 구조에서 수행할 연산을 분리합니다.",
        characteristics = listOf(
            "새로운 연산 추가가 용이",
            "Double Dispatch 활용"
        ),
        advantages = listOf(
            "개방-폐쇄 원칙 준수",
            "연산 관련 코드 한 곳에 모음"
        ),
        disadvantages = listOf(
            "새로운 클래스 추가가 어려움"
        ),
        useCases = listOf(
            "컴파일러의 AST 순회",
            "보고서 생성"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Shape { fun accept(visitor: ShapeVisitor) }

class Circle(val radius: Double) : Shape {
    override fun accept(visitor: ShapeVisitor) = visitor.visitCircle(this)
}

interface ShapeVisitor {
    fun visitCircle(circle: Circle)
    fun visitRectangle(rectangle: Rectangle)
}
                """.trimIndent(),
                explanation = "도형의 면적 계산을 분리하는 방문자 패턴",
                expectedOutput = """
원 면적 계산: 78.54
사각형 면적 계산: 200.0
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class Visitor {
        <<interface>>
        +visitElementA(ElementA)
    }
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "iterator"),
        difficulty = Difficulty.HIGH,
        frequency = 2
    )

    private fun createInterpreter() = DesignPattern(
        id = "interpreter",
        name = "Interpreter",
        koreanName = "인터프리터",
        category = PatternCategory.BEHAVIORAL,
        purpose = "언어의 문법 표현 및 해석기를 정의합니다.",
        characteristics = listOf(
            "간단한 언어 구현",
            "문법 규칙을 클래스로 표현"
        ),
        advantages = listOf(
            "문법 변경 및 확장 용이"
        ),
        disadvantages = listOf(
            "복잡한 문법은 유지보수 어려움"
        ),
        useCases = listOf(
            "SQL 파서",
            "정규 표현식"
        ),
        codeExamples = listOf(
            CodeExample(
                language = ProgrammingLanguage.KOTLIN,
                code = """
interface Expression { fun interpret(): Int }

class NumberExpression(private val number: Int) : Expression {
    override fun interpret() = number
}

class AddExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret() = left.interpret() + right.interpret()
}
                """.trimIndent(),
                explanation = "산술 표현식을 해석하는 인터프리터 패턴",
                expectedOutput = """
3 + 5 = 8
(2 + 3) + 4 = 9
                """.trimIndent()
            )
        ),
        diagram = """
classDiagram
    class AbstractExpression {
        <<interface>>
        +interpret(Context)
    }
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "visitor"),
        difficulty = Difficulty.HIGH,
        frequency = 1
    )
}
