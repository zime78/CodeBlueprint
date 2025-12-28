package com.codeblueprint.data.repository

import com.codeblueprint.domain.model.ProgrammingLanguage
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * CodeExecutionRepositoryImpl 통합 테스트
 * 코드 실행 시뮬레이션 결과 검증
 */
class CodeExecutionRepositoryImplTest {

    private val repository = CodeExecutionRepositoryImpl()

    // ========== Kotlin 함수 파라미터 보간 테스트 (핵심 버그 수정 대상) ==========

    @Test
    fun `executeCode - Kotlin 단순 함수 파라미터 보간`() = runTest {
        val code = """
            fun query(sql: String) {
                println("Executing query: ${'$'}sql")
            }
            query("SELECT * FROM users")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Executing query: SELECT * FROM users"),
            "파라미터가 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    @Test
    fun `executeCode - Kotlin object 메서드 파라미터 보간`() = runTest {
        val code = """
            object DatabaseConnection {
                fun query(sql: String) {
                    println("Executing query: ${'$'}sql")
                }
            }
            DatabaseConnection.query("SELECT * FROM users")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Executing query: SELECT * FROM users"),
            "object 메서드 파라미터가 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    @Test
    fun `executeCode - Kotlin 다중 파라미터 보간`() = runTest {
        val code = """
            fun greet(name: String, greeting: String) {
                println("${'$'}greeting, ${'$'}name!")
            }
            greet("John", "Hello")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Hello, John!"),
            "다중 파라미터가 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    // ========== 기존 val/var 변수 보간 테스트 (회귀 테스트) ==========

    @Test
    fun `executeCode - Kotlin val 변수 보간`() = runTest {
        val code = """
            val name = "World"
            println("Hello, ${'$'}name!")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Hello, World!"),
            "val 변수가 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    @Test
    fun `executeCode - Kotlin 중괄호 표현식 보간`() = runTest {
        val code = """
            val name = "World"
            println("Hello, ${'$'}{name}!")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Hello, World!"),
            "중괄호 표현식이 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    // ========== 복합 시나리오 테스트 ==========

    @Test
    fun `executeCode - Kotlin 변수와 파라미터 혼합`() = runTest {
        val code = """
            val prefix = "Result"
            fun execute(query: String) {
                println("${'$'}prefix: ${'$'}query")
            }
            execute("SELECT * FROM users")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Result: SELECT * FROM users"),
            "변수와 파라미터 모두 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    @Test
    fun `executeCode - Kotlin Singleton 패턴 전체 시나리오`() = runTest {
        val code = """
            object DatabaseConnection {
                init {
                    println("Database connection initialized")
                }

                fun query(sql: String) {
                    println("Executing query: ${'$'}sql")
                }
            }

            fun main() {
                DatabaseConnection.query("SELECT * FROM users")
                println("Singleton instance check: ${'$'}{DatabaseConnection === DatabaseConnection}")
            }
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.KOTLIN)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Database connection initialized"),
            "init 블록 출력이 있어야 함. 실제 출력: ${result.output}"
        )
        assertTrue(
            result.output.contains("Executing query: SELECT * FROM users"),
            "쿼리 파라미터가 치환되어야 함. 실제 출력: ${result.output}"
        )
    }

    // ========== 다른 언어 영향 없음 확인 (회귀 테스트) ==========

    @Test
    fun `executeCode - Python f-string 정상 동작`() = runTest {
        val code = """
            name = "World"
            print(f"Hello, {name}!")
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.PYTHON)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Hello, World!"),
            "Python f-string이 정상 동작해야 함. 실제 출력: ${result.output}"
        )
    }

    @Test
    fun `executeCode - JavaScript 템플릿 리터럴 정상 동작`() = runTest {
        val code = """
            const name = "World";
            console.log(`Hello, ${'$'}{name}!`);
        """.trimIndent()

        val result = repository.executeCode(code, ProgrammingLanguage.JAVASCRIPT)
        assertTrue(result.success, "실행 성공해야 함")
        assertTrue(
            result.output.contains("Hello, World!"),
            "JavaScript 템플릿 리터럴이 정상 동작해야 함. 실제 출력: ${result.output}"
        )
    }
}
