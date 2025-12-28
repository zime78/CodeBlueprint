package com.codeblueprint.data.interpreter

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * KotlinInterpolator 테스트
 * Kotlin 문자열 템플릿 보간 테스트 ($var, ${expr})
 */
class KotlinInterpolatorTest {

    private val interpolator = KotlinInterpolator()

    // ========== 단순 변수 보간 테스트 ($var) ==========

    @Test
    fun `interpolate - 단순 변수 치환`() {
        val template = "Hello \$name"
        val variables = mapOf("name" to "John")
        assertEquals("Hello John", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 여러 변수 치환`() {
        val template = "\$greeting, \$name!"
        val variables = mapOf("greeting" to "Hello", "name" to "John")
        assertEquals("Hello, John!", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 숫자 변수 치환`() {
        val template = "Count: \$count"
        val variables = mapOf("count" to "42")
        assertEquals("Count: 42", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 변수가 없으면 원본 유지`() {
        val template = "Hello \$unknown"
        val variables = emptyMap<String, String>()
        assertEquals("Hello \$unknown", interpolator.interpolate(template, variables))
    }

    // ========== 중괄호 표현식 보간 테스트 (${expr}) ==========

    @Test
    fun `interpolate - 중괄호 변수 치환`() {
        val template = "Hello \${name}"
        val variables = mapOf("name" to "John")
        assertEquals("Hello John", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 중괄호와 단순 변수 혼합`() {
        val template = "\${greeting}, \$name!"
        val variables = mapOf("greeting" to "Hello", "name" to "John")
        assertEquals("Hello, John!", interpolator.interpolate(template, variables))
    }

    // ========== 함수 파라미터 보간 테스트 (핵심 버그 수정 대상) ==========

    @Test
    fun `interpolate - 함수 파라미터 치환`() {
        val template = "Executing query: \$sql"
        val variables = mapOf("sql" to "SELECT * FROM users")
        assertEquals("Executing query: SELECT * FROM users", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 복잡한 문자열 내 파라미터 치환`() {
        val template = "Query '\$sql' executed successfully"
        val variables = mapOf("sql" to "SELECT * FROM users")
        assertEquals("Query 'SELECT * FROM users' executed successfully", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 다중 파라미터 치환`() {
        val template = "Connecting to \$host:\$port"
        val variables = mapOf("host" to "localhost", "port" to "5432")
        assertEquals("Connecting to localhost:5432", interpolator.interpolate(template, variables))
    }

    // ========== 엣지 케이스 ==========

    @Test
    fun `interpolate - 빈 템플릿`() {
        val template = ""
        val variables = mapOf("name" to "John")
        assertEquals("", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 변수 없는 템플릿`() {
        val template = "Hello World"
        val variables = mapOf("name" to "John")
        assertEquals("Hello World", interpolator.interpolate(template, variables))
    }

    @Test
    fun `interpolate - 달러 기호 이스케이프`() {
        // 달러 기호 뒤에 변수가 아닌 문자가 오는 경우
        val template = "Price: \$100"
        val variables = emptyMap<String, String>()
        // $100은 변수가 아니므로 원본 유지
        assertEquals("Price: \$100", interpolator.interpolate(template, variables))
    }
}
