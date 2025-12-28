package com.codeblueprint.data.local.datasource

import com.codeblueprint.data.local.db.PatternDao
import com.codeblueprint.data.local.entity.PatternEntity
import com.google.gson.Gson
import javax.inject.Inject

/**
 * 패턴 초기 데이터 생성기
 *
 * 데이터베이스가 최초 생성될 때 23개의 GoF 디자인 패턴 데이터를 삽입합니다.
 */
class PatternDataInitializer @Inject constructor(
    private val patternDao: PatternDao,
    private val gson: Gson
) {
    /**
     * 초기 패턴 데이터 삽입
     */
    suspend fun initializePatterns() {
        val patterns = createInitialPatterns()
        patternDao.insertPatterns(patterns)
    }

    private fun createInitialPatterns(): List<PatternEntity> {
        return listOf(
            // ==================== 생성 패턴 (5개) ====================
            createSingleton(),
            createFactoryMethod(),
            createAbstractFactory(),
            createBuilder(),
            createPrototype(),

            // ==================== 구조 패턴 (7개) ====================
            createAdapter(),
            createBridge(),
            createComposite(),
            createDecorator(),
            createFacade(),
            createFlyweight(),
            createProxy(),

            // ==================== 행위 패턴 (11개) ====================
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

    private fun createSingleton() = PatternEntity(
        id = "singleton",
        name = "Singleton",
        koreanName = "싱글톤",
        category = "CREATIONAL",
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
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
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
            """.trimIndent() to "Kotlin의 object 키워드를 사용한 싱글톤 구현"
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
        difficulty = "LOW",
        frequency = 5
    )

    private fun createFactoryMethod() = PatternEntity(
        id = "factory_method",
        name = "Factory Method",
        koreanName = "팩토리 메서드",
        category = "CREATIONAL",
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
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
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

class ConcreteCreatorB : Creator() {
    override fun factoryMethod() = ConcreteProductB()
}
            """.trimIndent() to "추상 팩토리 메서드를 통한 객체 생성 위임"
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
    class ConcreteProduct {
        +use()
    }
    Creator <|-- ConcreteCreator
    Product <|.. ConcreteProduct
    Creator --> Product
        """.trimIndent(),
        relatedPatternIds = listOf("abstract_factory", "singleton"),
        difficulty = "MEDIUM",
        frequency = 5
    )

    private fun createAbstractFactory() = PatternEntity(
        id = "abstract_factory",
        name = "Abstract Factory",
        koreanName = "추상 팩토리",
        category = "CREATIONAL",
        purpose = "관련된 객체들의 패밀리를 생성하기 위한 인터페이스를 제공합니다. 구체적인 클래스를 지정하지 않고 관련 객체 집합을 생성합니다.",
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
            "크로스 플랫폼 UI 툴킷 (Windows, Mac, Linux GUI)",
            "데이터베이스 드라이버 전체 컴포넌트",
            "테마 시스템 (다크 모드, 라이트 모드)"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Button { fun render() }
interface Checkbox { fun render() }

class WindowsButton : Button {
    override fun render() = println("Windows 버튼")
}
class WindowsCheckbox : Checkbox {
    override fun render() = println("Windows 체크박스")
}

class MacButton : Button {
    override fun render() = println("Mac 버튼")
}
class MacCheckbox : Checkbox {
    override fun render() = println("Mac 체크박스")
}

interface GUIFactory {
    fun createButton(): Button
    fun createCheckbox(): Checkbox
}

class WindowsFactory : GUIFactory {
    override fun createButton() = WindowsButton()
    override fun createCheckbox() = WindowsCheckbox()
}

class MacFactory : GUIFactory {
    override fun createButton() = MacButton()
    override fun createCheckbox() = MacCheckbox()
}
            """.trimIndent() to "플랫폼별 UI 컴포넌트 생성을 추상화"
        ),
        diagram = """
classDiagram
    class AbstractFactory {
        <<interface>>
        +createProductA(): ProductA
        +createProductB(): ProductB
    }
    class ConcreteFactory1 {
        +createProductA(): ProductA
        +createProductB(): ProductB
    }
    AbstractFactory <|.. ConcreteFactory1
        """.trimIndent(),
        relatedPatternIds = listOf("factory_method", "builder"),
        difficulty = "HIGH",
        frequency = 3
    )

    private fun createBuilder() = PatternEntity(
        id = "builder",
        name = "Builder",
        koreanName = "빌더",
        category = "CREATIONAL",
        purpose = "복잡한 객체의 생성 과정과 표현 방법을 분리하여, 동일한 생성 절차에서 다른 표현 결과를 만들 수 있게 합니다.",
        characteristics = listOf(
            "단계별 객체 생성",
            "동일한 생성 절차로 다른 표현 생성",
            "생성 과정과 표현 분리"
        ),
        advantages = listOf(
            "가독성 높은 객체 생성 코드",
            "불변 객체 생성 용이",
            "생성자 매개변수가 많을 때 효과적",
            "선택적 매개변수 처리 용이"
        ),
        disadvantages = listOf(
            "코드 양 증가",
            "간단한 객체에는 오버엔지니어링"
        ),
        useCases = listOf(
            "StringBuilder, StringBuffer",
            "HTTP 요청 빌더",
            "SQL 쿼리 빌더",
            "복잡한 설정 객체 생성 (알림 메시지)"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
data class Pizza(
    val dough: String,
    val sauce: String,
    val topping: String
) {
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

// 사용
val pizza = Pizza.Builder()
    .dough("씬 도우")
    .sauce("크림")
    .topping("페퍼로니")
    .build()
            """.trimIndent() to "빌더 패턴을 통한 단계적 객체 생성"
        ),
        diagram = """
classDiagram
    class Builder {
        <<interface>>
        +buildPartA()
        +buildPartB()
        +getResult(): Product
    }
    class ConcreteBuilder {
        +buildPartA()
        +buildPartB()
        +getResult(): Product
    }
    class Product {
        +partA
        +partB
    }
    Builder <|.. ConcreteBuilder
    ConcreteBuilder --> Product
        """.trimIndent(),
        relatedPatternIds = listOf("abstract_factory", "prototype"),
        difficulty = "MEDIUM",
        frequency = 5
    )

    private fun createPrototype() = PatternEntity(
        id = "prototype",
        name = "Prototype",
        koreanName = "프로토타입",
        category = "CREATIONAL",
        purpose = "기존 객체를 복제하여 새로운 객체를 생성합니다. 객체 생성 비용이 클 때 유용합니다.",
        characteristics = listOf(
            "객체 생성 비용이 클 때 사용",
            "복제를 통한 새 인스턴스 생성",
            "런타임에 동적으로 객체 추가 가능"
        ),
        advantages = listOf(
            "객체 생성 비용 절감",
            "복잡한 객체 생성 과정 단순화",
            "런타임에 동적으로 객체 추가 가능"
        ),
        disadvantages = listOf(
            "깊은 복사(Deep Copy) 구현이 복잡",
            "순환 참조 문제 발생 가능"
        ),
        useCases = listOf(
            "게임 캐릭터 복제",
            "문서 템플릿 복사",
            "데이터베이스 레코드 복제",
            "GUI 컴포넌트 복제"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
data class Document(
    val title: String,
    val content: String,
    val author: String
) : Cloneable {
    public override fun clone(): Document {
        return copy()
    }
}

// 사용
val original = Document("제목", "내용", "작성자")
val cloned = original.clone()
println(original == cloned)      // true (내용 동일)
println(original === cloned)     // false (다른 객체)
            """.trimIndent() to "Kotlin data class의 copy()를 활용한 프로토타입 패턴"
        ),
        diagram = """
classDiagram
    class Prototype {
        <<interface>>
        +clone(): Prototype
    }
    class ConcretePrototype {
        +clone(): Prototype
    }
    Prototype <|.. ConcretePrototype
        """.trimIndent(),
        relatedPatternIds = listOf("factory_method", "singleton"),
        difficulty = "MEDIUM",
        frequency = 2
    )

    // ==================== 구조 패턴 ====================

    private fun createAdapter() = PatternEntity(
        id = "adapter",
        name = "Adapter",
        koreanName = "어댑터",
        category = "STRUCTURAL",
        purpose = "호환되지 않는 인터페이스를 가진 객체들이 협력할 수 있도록 래퍼 역할을 합니다.",
        characteristics = listOf(
            "래퍼(Wrapper) 역할",
            "기존 클래스 수정 없이 호환성 제공",
            "인터페이스 변환"
        ),
        advantages = listOf(
            "기존 코드 수정 없이 호환성 제공",
            "단일 책임 원칙 준수",
            "개방-폐쇄 원칙 준수"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가",
            "어댑터 클래스 추가로 인한 오버헤드"
        ),
        useCases = listOf(
            "레거시 시스템과 신규 시스템 통합",
            "서드파티 라이브러리 통합",
            "전압 어댑터 (220V → 110V)",
            "XML to JSON 변환기"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
// 기존 인터페이스
interface MediaPlayer {
    fun play(audioType: String, fileName: String)
}

// 호환되지 않는 인터페이스
interface AdvancedMediaPlayer {
    fun playVlc(fileName: String)
    fun playMp4(fileName: String)
}

class VlcPlayer : AdvancedMediaPlayer {
    override fun playVlc(fileName: String) = println("VLC: ${'$'}fileName")
    override fun playMp4(fileName: String) {}
}

// 어댑터
class MediaAdapter(audioType: String) : MediaPlayer {
    private val advancedPlayer: AdvancedMediaPlayer = VlcPlayer()

    override fun play(audioType: String, fileName: String) {
        when (audioType) {
            "vlc" -> advancedPlayer.playVlc(fileName)
            "mp4" -> advancedPlayer.playMp4(fileName)
        }
    }
}
            """.trimIndent() to "미디어 플레이어 인터페이스를 통합하는 어댑터"
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
    class Adaptee {
        +specificRequest()
    }
    Target <|.. Adapter
    Adapter --> Adaptee
        """.trimIndent(),
        relatedPatternIds = listOf("bridge", "decorator", "proxy"),
        difficulty = "LOW",
        frequency = 5
    )

    private fun createBridge() = PatternEntity(
        id = "bridge",
        name = "Bridge",
        koreanName = "브릿지",
        category = "STRUCTURAL",
        purpose = "추상화와 구현을 분리하여 독립적으로 변경할 수 있게 합니다.",
        characteristics = listOf(
            "구현부와 추상부를 별도 클래스 계층으로 분리",
            "복합(Composition) 활용",
            "런타임에 구현 변경 가능"
        ),
        advantages = listOf(
            "추상화와 구현의 독립적 확장",
            "구현 세부사항을 클라이언트로부터 숨김",
            "개방-폐쇄 원칙 준수"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가",
            "고도로 결합된 클래스에는 부적합"
        ),
        useCases = listOf(
            "플랫폼 독립적 애플리케이션 (Windows, Linux, Mac)",
            "그래픽 렌더링 시스템 (DirectX, OpenGL)",
            "데이터베이스 드라이버",
            "원격 제어 시스템"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
// 구현부 인터페이스
interface DrawingAPI {
    fun drawCircle(x: Int, y: Int, radius: Int)
}

class DrawingAPI1 : DrawingAPI {
    override fun drawCircle(x: Int, y: Int, radius: Int) {
        println("API1: (${'$'}x, ${'$'}y) 반지름 ${'$'}radius")
    }
}

class DrawingAPI2 : DrawingAPI {
    override fun drawCircle(x: Int, y: Int, radius: Int) {
        println("API2: (${'$'}x, ${'$'}y) 반지름 ${'$'}radius")
    }
}

// 추상부
abstract class Shape(protected val drawingAPI: DrawingAPI) {
    abstract fun draw()
}

class Circle(
    private val x: Int,
    private val y: Int,
    private val radius: Int,
    drawingAPI: DrawingAPI
) : Shape(drawingAPI) {
    override fun draw() = drawingAPI.drawCircle(x, y, radius)
}
            """.trimIndent() to "도형과 렌더링 API를 브릿지 패턴으로 분리"
        ),
        diagram = """
classDiagram
    class Abstraction {
        #implementor: Implementor
        +operation()
    }
    class Implementor {
        <<interface>>
        +operationImpl()
    }
    Abstraction --> Implementor
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "strategy"),
        difficulty = "HIGH",
        frequency = 2
    )

    private fun createComposite() = PatternEntity(
        id = "composite",
        name = "Composite",
        koreanName = "컴포지트",
        category = "STRUCTURAL",
        purpose = "객체들을 트리 구조로 구성하여 부분-전체 계층을 표현합니다. 개별 객체와 복합 객체를 동일하게 다룹니다.",
        characteristics = listOf(
            "개별 객체와 복합 객체를 동일하게 취급",
            "재귀적 구조",
            "트리 계층 표현"
        ),
        advantages = listOf(
            "복잡한 트리 구조 간편 관리",
            "개방-폐쇄 원칙 준수",
            "다형성과 재귀 활용"
        ),
        disadvantages = listOf(
            "설계가 지나치게 일반화될 수 있음",
            "특정 컴포넌트 제한이 어려움"
        ),
        useCases = listOf(
            "파일 시스템 (폴더-파일 구조)",
            "UI 컴포넌트 계층 (Container-Component)",
            "조직도 (부서-직원)",
            "그래픽 편집기 (그룹-도형)"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Component {
    fun operation(): String
}

class Leaf(private val name: String) : Component {
    override fun operation() = name
}

class Composite(private val name: String) : Component {
    private val children = mutableListOf<Component>()

    fun add(component: Component) { children.add(component) }
    fun remove(component: Component) { children.remove(component) }

    override fun operation(): String {
        val results = children.map { it.operation() }
        return "${'$'}name[${'$'}{results.joinToString(", ")}]"
    }
}

// 사용
val tree = Composite("root")
tree.add(Leaf("Leaf A"))
tree.add(Composite("Branch").apply {
    add(Leaf("Leaf B"))
    add(Leaf("Leaf C"))
})
println(tree.operation()) // root[Leaf A, Branch[Leaf B, Leaf C]]
            """.trimIndent() to "트리 구조를 표현하는 컴포지트 패턴"
        ),
        diagram = """
classDiagram
    class Component {
        <<interface>>
        +operation()
    }
    class Leaf {
        +operation()
    }
    class Composite {
        -children: List~Component~
        +add(Component)
        +remove(Component)
        +operation()
    }
    Component <|.. Leaf
    Component <|.. Composite
    Composite o-- Component
        """.trimIndent(),
        relatedPatternIds = listOf("decorator", "iterator"),
        difficulty = "MEDIUM",
        frequency = 3
    )

    private fun createDecorator() = PatternEntity(
        id = "decorator",
        name = "Decorator",
        koreanName = "데코레이터",
        category = "STRUCTURAL",
        purpose = "객체에 동적으로 새로운 기능을 추가합니다. 상속 없이 객체를 확장합니다.",
        characteristics = listOf(
            "상속 없이 객체 확장",
            "래퍼를 통한 기능 추가",
            "런타임에 동적 기능 추가/제거"
        ),
        advantages = listOf(
            "런타임에 동적으로 기능 추가/제거",
            "단일 책임 원칙 준수",
            "조합을 통한 유연한 확장",
            "기존 코드 수정 없음"
        ),
        disadvantages = listOf(
            "작은 객체들이 많이 생성됨",
            "래퍼 스택이 복잡해질 수 있음"
        ),
        useCases = listOf(
            "Java I/O Stream (BufferedReader, FileReader)",
            "GUI 컴포넌트 스크롤바, 테두리 추가",
            "커피 주문 시스템 (우유, 시럽 추가)",
            "HTTP 요청/응답 래퍼"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Coffee {
    fun cost(): Double
    fun description(): String
}

class SimpleCoffee : Coffee {
    override fun cost() = 1.0
    override fun description() = "커피"
}

abstract class CoffeeDecorator(
    protected val coffee: Coffee
) : Coffee

class Milk(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun cost() = coffee.cost() + 0.5
    override fun description() = coffee.description() + " + 우유"
}

class Sugar(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun cost() = coffee.cost() + 0.2
    override fun description() = coffee.description() + " + 설탕"
}

// 사용
val coffee = Sugar(Milk(SimpleCoffee()))
println("${'$'}{coffee.description()}: ${'$'}{coffee.cost()}")
// 커피 + 우유 + 설탕: 1.7
            """.trimIndent() to "커피에 옵션을 추가하는 데코레이터 패턴"
        ),
        diagram = """
classDiagram
    class Component {
        <<interface>>
        +operation()
    }
    class ConcreteComponent {
        +operation()
    }
    class Decorator {
        -component: Component
        +operation()
    }
    class ConcreteDecorator {
        +operation()
    }
    Component <|.. ConcreteComponent
    Component <|.. Decorator
    Decorator <|-- ConcreteDecorator
    Decorator o-- Component
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "composite", "strategy"),
        difficulty = "MEDIUM",
        frequency = 4
    )

    private fun createFacade() = PatternEntity(
        id = "facade",
        name = "Facade",
        koreanName = "퍼사드",
        category = "STRUCTURAL",
        purpose = "복잡한 서브시스템에 대한 단순화된 인터페이스를 제공합니다.",
        characteristics = listOf(
            "서브시스템의 복잡성을 감춤",
            "고수준 인터페이스 제공",
            "클라이언트와 서브시스템 간 결합도 감소"
        ),
        advantages = listOf(
            "시스템 사용 간소화",
            "서브시스템과 클라이언트 간 결합도 감소",
            "레이어드 아키텍처 구현 용이"
        ),
        disadvantages = listOf(
            "퍼사드가 모든 클래스와 결합된 God Object가 될 수 있음"
        ),
        useCases = listOf(
            "컴파일러 인터페이스",
            "비디오 변환 라이브러리",
            "데이터베이스 접근 레이어",
            "라이브러리/프레임워크 래퍼"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
// 복잡한 서브시스템들
class VideoFile(val name: String)
class CodecFactory { fun extract(file: VideoFile) = "codec" }
class BitrateReader { fun read(file: VideoFile, codec: String) = ByteArray(0) }
class AudioMixer { fun fix(buffer: ByteArray) = buffer }

// 퍼사드
class VideoConverter {
    fun convert(fileName: String, format: String): ByteArray {
        val file = VideoFile(fileName)
        val codec = CodecFactory().extract(file)
        val buffer = BitrateReader().read(file, codec)
        return AudioMixer().fix(buffer)
    }
}

// 사용 - 복잡한 서브시스템을 간단하게 사용
val converter = VideoConverter()
val mp4 = converter.convert("video.ogg", "mp4")
            """.trimIndent() to "비디오 변환의 복잡성을 숨기는 퍼사드"
        ),
        diagram = """
classDiagram
    class Facade {
        +operation()
    }
    class SubsystemA {
        +operationA()
    }
    class SubsystemB {
        +operationB()
    }
    Facade --> SubsystemA
    Facade --> SubsystemB
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "mediator"),
        difficulty = "LOW",
        frequency = 5
    )

    private fun createFlyweight() = PatternEntity(
        id = "flyweight",
        name = "Flyweight",
        koreanName = "플라이웨이트",
        category = "STRUCTURAL",
        purpose = "공유를 통해 대량의 객체를 효율적으로 지원합니다. 메모리 사용을 최소화합니다.",
        characteristics = listOf(
            "메모리 사용 최적화",
            "내재 상태(Intrinsic)와 외재 상태(Extrinsic) 분리",
            "객체 공유"
        ),
        advantages = listOf(
            "메모리 사용량 대폭 감소",
            "성능 향상 (객체 생성 비용 절감)"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가",
            "CPU 사이클 증가 가능 (컨텍스트 데이터 찾기)"
        ),
        useCases = listOf(
            "텍스트 에디터의 문자 렌더링",
            "게임의 파티클 시스템",
            "웹 브라우저의 폰트 렌더링",
            "문자열 풀 (String Pool)"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
// 공유되는 내재 상태
class TreeType(
    val name: String,
    val color: String,
    val texture: String
) {
    fun draw(x: Int, y: Int) {
        println("Drawing ${'$'}name at (${'$'}x, ${'$'}y)")
    }
}

// 플라이웨이트 팩토리
object TreeFactory {
    private val treeTypes = mutableMapOf<String, TreeType>()

    fun getTreeType(name: String, color: String, texture: String): TreeType {
        val key = "${'$'}name-${'$'}color-${'$'}texture"
        return treeTypes.getOrPut(key) {
            TreeType(name, color, texture)
        }
    }
}

// 외재 상태를 포함하는 컨텍스트
class Tree(
    val x: Int,
    val y: Int,
    val type: TreeType  // 공유됨
) {
    fun draw() = type.draw(x, y)
}
            """.trimIndent() to "나무 객체의 공통 속성을 공유하는 플라이웨이트"
        ),
        diagram = """
classDiagram
    class FlyweightFactory {
        -flyweights: Map
        +getFlyweight(key): Flyweight
    }
    class Flyweight {
        -intrinsicState
        +operation(extrinsicState)
    }
    FlyweightFactory --> Flyweight
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "singleton"),
        difficulty = "HIGH",
        frequency = 2
    )

    private fun createProxy() = PatternEntity(
        id = "proxy",
        name = "Proxy",
        koreanName = "프록시",
        category = "STRUCTURAL",
        purpose = "다른 객체에 대한 접근을 제어하는 대리자를 제공합니다.",
        characteristics = listOf(
            "실제 객체에 대한 간접 참조",
            "추가 기능 제공 (로깅, 캐싱, 접근 제어)",
            "지연 초기화 가능"
        ),
        advantages = listOf(
            "실제 객체 접근 제어",
            "지연 초기화 (Lazy Initialization)",
            "로깅, 캐싱, 접근 제어 추가 가능",
            "개방-폐쇄 원칙 준수"
        ),
        disadvantages = listOf(
            "응답 시간 증가 가능",
            "코드 복잡도 증가"
        ),
        useCases = listOf(
            "가상 프록시 (이미지 지연 로딩)",
            "보호 프록시 (접근 권한 제어)",
            "원격 프록시 (RMI, Web Service)",
            "스마트 참조 (참조 카운팅)"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Image {
    fun display()
}

class RealImage(private val fileName: String) : Image {
    init {
        loadFromDisk()
    }

    private fun loadFromDisk() {
        println("Loading ${'$'}fileName from disk...")
    }

    override fun display() {
        println("Displaying ${'$'}fileName")
    }
}

// 가상 프록시 - 지연 로딩
class ProxyImage(private val fileName: String) : Image {
    private var realImage: RealImage? = null

    override fun display() {
        if (realImage == null) {
            realImage = RealImage(fileName)  // 실제 필요할 때 로딩
        }
        realImage?.display()
    }
}

// 사용
val image = ProxyImage("photo.jpg")
// 이 시점에는 로딩되지 않음
image.display()  // 이때 로딩됨
            """.trimIndent() to "이미지 지연 로딩을 위한 가상 프록시"
        ),
        diagram = """
classDiagram
    class Subject {
        <<interface>>
        +request()
    }
    class RealSubject {
        +request()
    }
    class Proxy {
        -realSubject: RealSubject
        +request()
    }
    Subject <|.. RealSubject
    Subject <|.. Proxy
    Proxy --> RealSubject
        """.trimIndent(),
        relatedPatternIds = listOf("adapter", "decorator"),
        difficulty = "MEDIUM",
        frequency = 4
    )

    // ==================== 행위 패턴 ====================

    private fun createChainOfResponsibility() = PatternEntity(
        id = "chain_of_responsibility",
        name = "Chain of Responsibility",
        koreanName = "책임 연쇄",
        category = "BEHAVIORAL",
        purpose = "요청을 처리할 객체를 체인 형태로 연결하여, 요청이 처리될 때까지 체인을 따라 전달합니다.",
        characteristics = listOf(
            "요청이 처리될 때까지 체인을 따라 전달",
            "송신자와 수신자 분리",
            "동적 처리자 추가/제거 가능"
        ),
        advantages = listOf(
            "요청 송신자와 수신자 분리",
            "처리자 동적 추가/제거 가능",
            "단일 책임 원칙 준수"
        ),
        disadvantages = listOf(
            "요청이 처리되지 않을 수 있음",
            "디버깅 어려움"
        ),
        useCases = listOf(
            "이벤트 버블링 (DOM)",
            "로깅 시스템 (레벨별 처리)",
            "승인 프로세스",
            "예외 처리 체인"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
abstract class Handler {
    private var next: Handler? = null

    fun setNext(handler: Handler): Handler {
        next = handler
        return handler
    }

    open fun handle(request: String): String? {
        return next?.handle(request)
    }
}

class LowLevelSupport : Handler() {
    override fun handle(request: String): String? {
        return if (request == "simple") "LowLevel 처리"
        else super.handle(request)
    }
}

class MidLevelSupport : Handler() {
    override fun handle(request: String): String? {
        return if (request == "medium") "MidLevel 처리"
        else super.handle(request)
    }
}

class HighLevelSupport : Handler() {
    override fun handle(request: String): String? {
        return "HighLevel 처리 (최종)"
    }
}

// 사용
val chain = LowLevelSupport()
chain.setNext(MidLevelSupport()).setNext(HighLevelSupport())
println(chain.handle("complex"))  // HighLevel 처리 (최종)
            """.trimIndent() to "고객 지원 시스템의 책임 연쇄 패턴"
        ),
        diagram = """
classDiagram
    class Handler {
        <<abstract>>
        -next: Handler
        +setNext(Handler): Handler
        +handle(request): String
    }
    class ConcreteHandler {
        +handle(request): String
    }
    Handler <|-- ConcreteHandler
    Handler --> Handler : next
        """.trimIndent(),
        relatedPatternIds = listOf("command", "mediator"),
        difficulty = "MEDIUM",
        frequency = 3
    )

    private fun createCommand() = PatternEntity(
        id = "command",
        name = "Command",
        koreanName = "커맨드",
        category = "BEHAVIORAL",
        purpose = "요청을 객체로 캡슐화하여, 요청의 실행, 취소, 재실행을 가능하게 합니다.",
        characteristics = listOf(
            "요청을 실행, 취소, 재실행 가능",
            "요청을 큐에 저장",
            "요청 송신자와 수신자 분리"
        ),
        advantages = listOf(
            "요청 송신자와 수신자 분리",
            "Undo/Redo 구현 용이",
            "명령 큐 및 로깅 가능",
            "개방-폐쇄 원칙 준수"
        ),
        disadvantages = listOf(
            "코드 복잡도 증가",
            "커맨드 클래스 수 증가"
        ),
        useCases = listOf(
            "GUI 버튼 동작",
            "작업 취소/재실행 (Ctrl+Z, Ctrl+Y)",
            "트랜잭션 시스템",
            "매크로 기록"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Command {
    fun execute()
    fun undo()
}

class Light {
    fun on() = println("Light ON")
    fun off() = println("Light OFF")
}

class LightOnCommand(private val light: Light) : Command {
    override fun execute() = light.on()
    override fun undo() = light.off()
}

class LightOffCommand(private val light: Light) : Command {
    override fun execute() = light.off()
    override fun undo() = light.on()
}

class RemoteControl {
    private val history = mutableListOf<Command>()

    fun executeCommand(command: Command) {
        command.execute()
        history.add(command)
    }

    fun undoLast() {
        history.removeLastOrNull()?.undo()
    }
}
            """.trimIndent() to "리모컨의 명령을 객체화하는 커맨드 패턴"
        ),
        diagram = """
classDiagram
    class Command {
        <<interface>>
        +execute()
        +undo()
    }
    class ConcreteCommand {
        -receiver: Receiver
        +execute()
        +undo()
    }
    class Receiver {
        +action()
    }
    class Invoker {
        -command: Command
        +setCommand(Command)
        +executeCommand()
    }
    Command <|.. ConcreteCommand
    ConcreteCommand --> Receiver
    Invoker --> Command
        """.trimIndent(),
        relatedPatternIds = listOf("memento", "chain_of_responsibility"),
        difficulty = "MEDIUM",
        frequency = 4
    )

    private fun createIterator() = PatternEntity(
        id = "iterator",
        name = "Iterator",
        koreanName = "반복자",
        category = "BEHAVIORAL",
        purpose = "내부 구조를 노출하지 않고 컬렉션 요소에 순차적으로 접근하는 방법을 제공합니다.",
        characteristics = listOf(
            "통일된 인터페이스로 다양한 컬렉션 순회",
            "컬렉션과 순회 알고리즘 분리",
            "여러 순회 동시 실행 가능"
        ),
        advantages = listOf(
            "컬렉션 구현과 순회 알고리즘 분리",
            "단일 책임 원칙 준수",
            "여러 순회 알고리즘 동시 실행 가능"
        ),
        disadvantages = listOf(
            "간단한 컬렉션에는 오버킬",
            "효율성이 직접 접근보다 낮을 수 있음"
        ),
        useCases = listOf(
            "Java Collection Framework (Iterator)",
            "데이터베이스 결과셋 순회",
            "파일 시스템 탐색",
            "트리/그래프 순회"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Iterator<T> {
    fun hasNext(): Boolean
    fun next(): T
}

interface IterableCollection<T> {
    fun createIterator(): Iterator<T>
}

class WordsCollection(private val words: List<String>) : IterableCollection<String> {

    override fun createIterator() = WordsIterator(words)

    class WordsIterator(private val words: List<String>) : Iterator<String> {
        private var index = 0

        override fun hasNext() = index < words.size
        override fun next() = words[index++]
    }
}

// 사용 (Kotlin의 for-in은 내부적으로 Iterator 사용)
val collection = WordsCollection(listOf("First", "Second", "Third"))
val iterator = collection.createIterator()
while (iterator.hasNext()) {
    println(iterator.next())
}
            """.trimIndent() to "컬렉션의 요소를 순회하는 반복자 패턴"
        ),
        diagram = """
classDiagram
    class Iterator {
        <<interface>>
        +hasNext(): Boolean
        +next(): T
    }
    class ConcreteIterator {
        -collection: Collection
        -index: Int
        +hasNext(): Boolean
        +next(): T
    }
    class Aggregate {
        <<interface>>
        +createIterator(): Iterator
    }
    Iterator <|.. ConcreteIterator
    Aggregate --> Iterator
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "visitor"),
        difficulty = "LOW",
        frequency = 5
    )

    private fun createMediator() = PatternEntity(
        id = "mediator",
        name = "Mediator",
        koreanName = "중재자",
        category = "BEHAVIORAL",
        purpose = "객체 간 직접 통신을 제한하고, 중재자를 통해서만 협력하도록 합니다.",
        characteristics = listOf(
            "다대다 관계를 일대다 관계로 단순화",
            "중앙집중식 제어",
            "객체 간 결합도 감소"
        ),
        advantages = listOf(
            "컴포넌트 간 결합도 감소",
            "컴포넌트 재사용성 증가",
            "통신 로직 중앙화"
        ),
        disadvantages = listOf(
            "중재자가 복잡해질 수 있음 (God Object)",
            "중재자 자체가 단일 실패 지점"
        ),
        useCases = listOf(
            "채팅방 시스템",
            "공항 관제탑",
            "UI 다이얼로그 폼 검증",
            "MVC 패턴의 Controller"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface ChatMediator {
    fun sendMessage(message: String, user: User)
    fun addUser(user: User)
}

class ChatRoom : ChatMediator {
    private val users = mutableListOf<User>()

    override fun addUser(user: User) {
        users.add(user)
    }

    override fun sendMessage(message: String, sender: User) {
        users.filter { it != sender }
            .forEach { it.receive(message) }
    }
}

abstract class User(
    protected val mediator: ChatMediator,
    val name: String
) {
    abstract fun send(message: String)
    abstract fun receive(message: String)
}

class ChatUser(mediator: ChatMediator, name: String) : User(mediator, name) {
    override fun send(message: String) {
        println("${'$'}name 전송: ${'$'}message")
        mediator.sendMessage(message, this)
    }

    override fun receive(message: String) {
        println("${'$'}name 수신: ${'$'}message")
    }
}
            """.trimIndent() to "채팅방의 메시지 전달을 중재하는 패턴"
        ),
        diagram = """
classDiagram
    class Mediator {
        <<interface>>
        +notify(sender, event)
    }
    class ConcreteMediator {
        -componentA
        -componentB
        +notify(sender, event)
    }
    class Component {
        -mediator: Mediator
    }
    Mediator <|.. ConcreteMediator
    Component --> Mediator
    ConcreteMediator --> Component
        """.trimIndent(),
        relatedPatternIds = listOf("facade", "observer"),
        difficulty = "MEDIUM",
        frequency = 3
    )

    private fun createMemento() = PatternEntity(
        id = "memento",
        name = "Memento",
        koreanName = "메멘토",
        category = "BEHAVIORAL",
        purpose = "캡슐화를 위배하지 않으면서 객체의 내부 상태를 저장하고 복원합니다.",
        characteristics = listOf(
            "캡슐화를 위배하지 않음",
            "상태 이력 관리",
            "Undo/Redo 구현 용이"
        ),
        advantages = listOf(
            "캡슐화 유지",
            "Undo/Redo 구현 용이",
            "객체 상태 이력 관리"
        ),
        disadvantages = listOf(
            "메모리 사용량 증가",
            "직렬화 비용"
        ),
        useCases = listOf(
            "텍스트 에디터 Undo",
            "게임 세이브/로드",
            "트랜잭션 롤백",
            "브라우저 뒤로가기"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
// 메멘토 - 상태 저장
data class EditorMemento(val content: String)

// 오리지네이터 - 상태를 가진 객체
class Editor {
    var content: String = ""

    fun save(): EditorMemento = EditorMemento(content)

    fun restore(memento: EditorMemento) {
        content = memento.content
    }
}

// 케어테이커 - 메멘토 관리
class History {
    private val mementos = mutableListOf<EditorMemento>()

    fun push(memento: EditorMemento) {
        mementos.add(memento)
    }

    fun pop(): EditorMemento? {
        return mementos.removeLastOrNull()
    }
}

// 사용
val editor = Editor()
val history = History()

editor.content = "Hello"
history.push(editor.save())

editor.content = "Hello World"
history.push(editor.save())

editor.content = "Hello World!"

history.pop()?.let { editor.restore(it) }  // "Hello World"로 복원
            """.trimIndent() to "텍스트 에디터의 Undo 기능 구현"
        ),
        diagram = """
classDiagram
    class Originator {
        -state
        +save(): Memento
        +restore(Memento)
    }
    class Memento {
        -state
        +getState()
    }
    class Caretaker {
        -mementos: List~Memento~
        +push(Memento)
        +pop(): Memento
    }
    Originator --> Memento
    Caretaker --> Memento
        """.trimIndent(),
        relatedPatternIds = listOf("command", "iterator"),
        difficulty = "MEDIUM",
        frequency = 3
    )

    private fun createObserver() = PatternEntity(
        id = "observer",
        name = "Observer",
        koreanName = "옵저버",
        category = "BEHAVIORAL",
        purpose = "객체 상태 변화를 관찰자들에게 자동으로 통지합니다. 일대다 의존성을 정의합니다.",
        characteristics = listOf(
            "일대다 의존성 정의",
            "발행-구독 모델",
            "느슨한 결합"
        ),
        advantages = listOf(
            "느슨한 결합",
            "동적으로 구독자 추가/제거",
            "개방-폐쇄 원칙 준수"
        ),
        disadvantages = listOf(
            "구독자에게 알림 순서 보장 안 됨",
            "메모리 누수 위험 (구독 해제 미수행)"
        ),
        useCases = listOf(
            "이벤트 리스너 (GUI)",
            "MVC 패턴 (Model-View)",
            "발행-구독 시스템",
            "데이터 바인딩"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Observer {
    fun update(message: String)
}

class Subject {
    private val observers = mutableListOf<Observer>()

    fun attach(observer: Observer) {
        observers.add(observer)
    }

    fun detach(observer: Observer) {
        observers.remove(observer)
    }

    fun notify(message: String) {
        observers.forEach { it.update(message) }
    }
}

class ConcreteObserver(private val name: String) : Observer {
    override fun update(message: String) {
        println("${'$'}name received: ${'$'}message")
    }
}

// 사용
val subject = Subject()
val observer1 = ConcreteObserver("Observer1")
val observer2 = ConcreteObserver("Observer2")

subject.attach(observer1)
subject.attach(observer2)
subject.notify("Hello!")  // 모든 옵저버에게 알림
            """.trimIndent() to "상태 변화를 구독자들에게 알리는 옵저버 패턴"
        ),
        diagram = """
classDiagram
    class Subject {
        -observers: List~Observer~
        +attach(Observer)
        +detach(Observer)
        +notify()
    }
    class Observer {
        <<interface>>
        +update()
    }
    class ConcreteObserver {
        +update()
    }
    Subject --> Observer
    Observer <|.. ConcreteObserver
        """.trimIndent(),
        relatedPatternIds = listOf("mediator", "strategy"),
        difficulty = "MEDIUM",
        frequency = 5
    )

    private fun createState() = PatternEntity(
        id = "state",
        name = "State",
        koreanName = "상태",
        category = "BEHAVIORAL",
        purpose = "객체의 내부 상태에 따라 행동을 변경합니다. 객체가 클래스를 바꾼 것처럼 보입니다.",
        characteristics = listOf(
            "상태를 별도 클래스로 캡슐화",
            "상태 전이 관리",
            "조건문 대체"
        ),
        advantages = listOf(
            "단일 책임 원칙 준수",
            "개방-폐쇄 원칙 준수",
            "조건문 제거로 코드 간소화"
        ),
        disadvantages = listOf(
            "상태가 적으면 과도한 설계",
            "클래스 수 증가"
        ),
        useCases = listOf(
            "TCP 연결 상태 (LISTEN, ESTABLISHED, CLOSED)",
            "주문 상태 (접수, 배송, 완료)",
            "자판기 상태",
            "게임 캐릭터 상태"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface State {
    fun handle(context: Context)
}

class Context {
    var state: State = ConcreteStateA()

    fun request() {
        state.handle(this)
    }
}

class ConcreteStateA : State {
    override fun handle(context: Context) {
        println("State A 처리 → State B로 전이")
        context.state = ConcreteStateB()
    }
}

class ConcreteStateB : State {
    override fun handle(context: Context) {
        println("State B 처리 → State A로 전이")
        context.state = ConcreteStateA()
    }
}

// 사용
val context = Context()
context.request()  // State A 처리 → State B로 전이
context.request()  // State B 처리 → State A로 전이
            """.trimIndent() to "상태에 따라 행동이 변하는 상태 패턴"
        ),
        diagram = """
classDiagram
    class Context {
        -state: State
        +request()
    }
    class State {
        <<interface>>
        +handle(Context)
    }
    class ConcreteStateA {
        +handle(Context)
    }
    class ConcreteStateB {
        +handle(Context)
    }
    Context --> State
    State <|.. ConcreteStateA
    State <|.. ConcreteStateB
        """.trimIndent(),
        relatedPatternIds = listOf("strategy", "singleton"),
        difficulty = "MEDIUM",
        frequency = 4
    )

    private fun createStrategy() = PatternEntity(
        id = "strategy",
        name = "Strategy",
        koreanName = "전략",
        category = "BEHAVIORAL",
        purpose = "알고리즘 군을 정의하고 캡슐화하여 교환 가능하게 만듭니다. 런타임에 알고리즘을 변경할 수 있습니다.",
        characteristics = listOf(
            "런타임에 알고리즘 변경 가능",
            "알고리즘을 사용하는 클라이언트와 독립적",
            "조건문 대체"
        ),
        advantages = listOf(
            "개방-폐쇄 원칙 준수",
            "런타임에 알고리즘 선택",
            "조건문 대체",
            "테스트 용이"
        ),
        disadvantages = listOf(
            "클라이언트가 전략을 알아야 함",
            "전략 객체 수 증가"
        ),
        useCases = listOf(
            "정렬 알고리즘 (QuickSort, MergeSort)",
            "결제 방식 (카드, 현금, 포인트)",
            "압축 알고리즘",
            "라우팅 알고리즘"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface PaymentStrategy {
    fun pay(amount: Int)
}

class CreditCardStrategy(
    private val cardNumber: String
) : PaymentStrategy {
    override fun pay(amount: Int) {
        println("${'$'}amount원을 카드(${'$'}cardNumber)로 결제")
    }
}

class CashStrategy : PaymentStrategy {
    override fun pay(amount: Int) {
        println("${'$'}amount원을 현금으로 결제")
    }
}

class ShoppingCart {
    private var paymentStrategy: PaymentStrategy? = null

    fun setPaymentStrategy(strategy: PaymentStrategy) {
        this.paymentStrategy = strategy
    }

    fun checkout(amount: Int) {
        paymentStrategy?.pay(amount)
            ?: println("결제 방식을 선택해주세요")
    }
}

// 사용
val cart = ShoppingCart()
cart.setPaymentStrategy(CreditCardStrategy("1234-5678"))
cart.checkout(10000)  // 카드 결제

cart.setPaymentStrategy(CashStrategy())
cart.checkout(5000)   // 현금 결제
            """.trimIndent() to "결제 방식을 교체 가능하게 하는 전략 패턴"
        ),
        diagram = """
classDiagram
    class Context {
        -strategy: Strategy
        +setStrategy(Strategy)
        +executeStrategy()
    }
    class Strategy {
        <<interface>>
        +execute()
    }
    class ConcreteStrategyA {
        +execute()
    }
    class ConcreteStrategyB {
        +execute()
    }
    Context --> Strategy
    Strategy <|.. ConcreteStrategyA
    Strategy <|.. ConcreteStrategyB
        """.trimIndent(),
        relatedPatternIds = listOf("state", "template_method"),
        difficulty = "LOW",
        frequency = 5
    )

    private fun createTemplateMethod() = PatternEntity(
        id = "template_method",
        name = "Template Method",
        koreanName = "템플릿 메서드",
        category = "BEHAVIORAL",
        purpose = "알고리즘의 골격을 정의하고, 세부 단계는 서브클래스에서 구현합니다.",
        characteristics = listOf(
            "코드 재사용 극대화",
            "불변 부분과 가변 부분 분리",
            "상속 기반"
        ),
        advantages = listOf(
            "코드 중복 제거",
            "프레임워크 구축에 유용",
            "공통 부분 통제 가능"
        ),
        disadvantages = listOf(
            "리스코프 치환 원칙 위반 가능",
            "템플릿 메서드가 많아지면 유지보수 어려움"
        ),
        useCases = listOf(
            "프레임워크 라이프사이클 메서드",
            "데이터 마이닝 알고리즘",
            "테스트 프레임워크 (setUp, tearDown)",
            "HTTP 요청 처리"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
abstract class DataMiner {
    // 템플릿 메서드 - 알고리즘의 골격 정의
    fun mine(path: String) {
        val file = openFile(path)
        val data = extractData(file)
        val analysis = analyzeData(data)
        sendReport(analysis)
        closeFile(file)
    }

    abstract fun openFile(path: String): String
    abstract fun extractData(file: String): String
    abstract fun analyzeData(data: String): String

    // 공통 구현
    open fun sendReport(analysis: String) {
        println("Report: ${'$'}analysis")
    }

    open fun closeFile(file: String) {
        println("Closing file")
    }
}

class PDFDataMiner : DataMiner() {
    override fun openFile(path: String) = "PDF opened: ${'$'}path"
    override fun extractData(file: String) = "PDF data"
    override fun analyzeData(data: String) = "PDF analysis"
}

class CSVDataMiner : DataMiner() {
    override fun openFile(path: String) = "CSV opened: ${'$'}path"
    override fun extractData(file: String) = "CSV data"
    override fun analyzeData(data: String) = "CSV analysis"
}
            """.trimIndent() to "데이터 마이닝 알고리즘의 골격을 정의하는 템플릿 메서드"
        ),
        diagram = """
classDiagram
    class AbstractClass {
        +templateMethod()
        #primitiveOperation1()
        #primitiveOperation2()
    }
    class ConcreteClass {
        #primitiveOperation1()
        #primitiveOperation2()
    }
    AbstractClass <|-- ConcreteClass
        """.trimIndent(),
        relatedPatternIds = listOf("strategy", "factory_method"),
        difficulty = "LOW",
        frequency = 4
    )

    private fun createVisitor() = PatternEntity(
        id = "visitor",
        name = "Visitor",
        koreanName = "방문자",
        category = "BEHAVIORAL",
        purpose = "객체 구조에서 수행할 연산을 분리합니다. 클래스 변경 없이 새로운 연산을 추가할 수 있습니다.",
        characteristics = listOf(
            "새로운 연산 추가가 용이",
            "Double Dispatch 활용",
            "연산 관련 코드 한 곳에 모음"
        ),
        advantages = listOf(
            "개방-폐쇄 원칙 준수",
            "단일 책임 원칙 준수",
            "연산 관련 코드 한 곳에 모음"
        ),
        disadvantages = listOf(
            "새로운 클래스 추가가 어려움",
            "캡슐화 위반 가능"
        ),
        useCases = listOf(
            "컴파일러의 AST 순회",
            "XML/JSON 파싱",
            "보고서 생성",
            "파일 시스템 작업"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Shape {
    fun accept(visitor: ShapeVisitor)
}

class Circle(val radius: Double) : Shape {
    override fun accept(visitor: ShapeVisitor) {
        visitor.visitCircle(this)
    }
}

class Rectangle(val width: Double, val height: Double) : Shape {
    override fun accept(visitor: ShapeVisitor) {
        visitor.visitRectangle(this)
    }
}

interface ShapeVisitor {
    fun visitCircle(circle: Circle)
    fun visitRectangle(rectangle: Rectangle)
}

class AreaCalculator : ShapeVisitor {
    var totalArea = 0.0

    override fun visitCircle(circle: Circle) {
        totalArea += Math.PI * circle.radius * circle.radius
    }

    override fun visitRectangle(rectangle: Rectangle) {
        totalArea += rectangle.width * rectangle.height
    }
}

// 사용
val shapes = listOf(Circle(5.0), Rectangle(4.0, 6.0))
val calculator = AreaCalculator()
shapes.forEach { it.accept(calculator) }
println("총 면적: ${'$'}{calculator.totalArea}")
            """.trimIndent() to "도형의 면적 계산을 분리하는 방문자 패턴"
        ),
        diagram = """
classDiagram
    class Visitor {
        <<interface>>
        +visitElementA(ElementA)
        +visitElementB(ElementB)
    }
    class Element {
        <<interface>>
        +accept(Visitor)
    }
    class ElementA {
        +accept(Visitor)
    }
    Element <|.. ElementA
    Visitor --> Element
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "iterator"),
        difficulty = "HIGH",
        frequency = 2
    )

    private fun createInterpreter() = PatternEntity(
        id = "interpreter",
        name = "Interpreter",
        koreanName = "인터프리터",
        category = "BEHAVIORAL",
        purpose = "언어의 문법 표현 및 해석기를 정의합니다. 간단한 언어를 구현할 때 사용합니다.",
        characteristics = listOf(
            "간단한 언어 구현",
            "문법 규칙을 클래스로 표현",
            "AST(Abstract Syntax Tree) 기반"
        ),
        advantages = listOf(
            "문법 변경 및 확장 용이",
            "문법 구현이 간단"
        ),
        disadvantages = listOf(
            "복잡한 문법은 유지보수 어려움",
            "성능이 떨어질 수 있음"
        ),
        useCases = listOf(
            "SQL 파서",
            "정규 표현식",
            "수식 계산기",
            "설정 파일 파서"
        ),
        codeExamples = createCodeExamplesJson(
            "KOTLIN" to """
interface Expression {
    fun interpret(): Int
}

class NumberExpression(private val number: Int) : Expression {
    override fun interpret() = number
}

class AddExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret() = left.interpret() + right.interpret()
}

class SubtractExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret() = left.interpret() - right.interpret()
}

// 사용: (5 + 3) - 2 = 6
val expression = SubtractExpression(
    AddExpression(
        NumberExpression(5),
        NumberExpression(3)
    ),
    NumberExpression(2)
)
println(expression.interpret())  // 6
            """.trimIndent() to "산술 표현식을 해석하는 인터프리터 패턴"
        ),
        diagram = """
classDiagram
    class AbstractExpression {
        <<interface>>
        +interpret(Context)
    }
    class TerminalExpression {
        +interpret(Context)
    }
    class NonterminalExpression {
        +interpret(Context)
    }
    AbstractExpression <|.. TerminalExpression
    AbstractExpression <|.. NonterminalExpression
    NonterminalExpression o-- AbstractExpression
        """.trimIndent(),
        relatedPatternIds = listOf("composite", "visitor"),
        difficulty = "HIGH",
        frequency = 1
    )

    // ==================== 헬퍼 함수 ====================

    private fun createCodeExamplesJson(vararg examples: Pair<String, Pair<String, String>>): String {
        val list = examples.map { (lang, codeAndExplanation) ->
            mapOf(
                "language" to lang,
                "code" to codeAndExplanation.first,
                "explanation" to codeAndExplanation.second
            )
        }
        return gson.toJson(list)
    }

    private infix fun String.to(codeAndExplanation: Pair<String, String>) =
        this to codeAndExplanation
}
