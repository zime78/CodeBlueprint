package com.codeblueprint.data.interpreter

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * VariableExtractor 테스트
 * Kotlin 코드에서 변수, 함수 파라미터, 함수 호출 인자 추출 테스트
 */
class VariableExtractorTest {

    // ========== 기존 val/var 변수 추출 테스트 (회귀 테스트) ==========

    @Test
    fun `extractKotlinVariables - val 문자열 변수 추출`() {
        val code = """val name = "John""""
        val result = VariableExtractor.extractKotlinVariables(code)
        assertEquals("John", result["name"])
    }

    @Test
    fun `extractKotlinVariables - var 문자열 변수 추출`() {
        val code = """var message = "Hello World""""
        val result = VariableExtractor.extractKotlinVariables(code)
        assertEquals("Hello World", result["message"])
    }

    @Test
    fun `extractKotlinVariables - 숫자 변수 추출`() {
        val code = """val count = 42"""
        val result = VariableExtractor.extractKotlinVariables(code)
        assertEquals("42", result["count"])
    }

    @Test
    fun `extractKotlinVariables - boolean 변수 추출`() {
        val code = """val isActive = true"""
        val result = VariableExtractor.extractKotlinVariables(code)
        assertEquals("true", result["isActive"])
    }

    @Test
    fun `extractKotlinVariables - 여러 변수 동시 추출`() {
        val code = """
            val name = "John"
            val age = 25
            val isStudent = false
        """.trimIndent()
        val result = VariableExtractor.extractKotlinVariables(code)

        assertEquals("John", result["name"])
        assertEquals("25", result["age"])
        assertEquals("false", result["isStudent"])
    }

    // ========== 함수 파라미터 추출 테스트 (신규) ==========

    @Test
    fun `extractKotlinFunctionParams - 단일 파라미터 추출`() {
        val code = """fun query(sql: String) { }"""
        val result = VariableExtractor.extractKotlinFunctionParams(code)
        assertEquals(listOf("sql"), result["query"])
    }

    @Test
    fun `extractKotlinFunctionParams - 다중 파라미터 추출`() {
        val code = """fun greet(name: String, age: Int) { }"""
        val result = VariableExtractor.extractKotlinFunctionParams(code)
        assertEquals(listOf("name", "age"), result["greet"])
    }

    @Test
    fun `extractKotlinFunctionParams - 파라미터 없는 함수`() {
        val code = """fun main() { }"""
        val result = VariableExtractor.extractKotlinFunctionParams(code)
        assertEquals(emptyList(), result["main"])
    }

    @Test
    fun `extractKotlinFunctionParams - 여러 함수 동시 추출`() {
        val code = """
            fun query(sql: String) { }
            fun connect(host: String, port: Int) { }
            fun disconnect() { }
        """.trimIndent()
        val result = VariableExtractor.extractKotlinFunctionParams(code)

        assertEquals(listOf("sql"), result["query"])
        assertEquals(listOf("host", "port"), result["connect"])
        assertEquals(emptyList(), result["disconnect"])
    }

    @Test
    fun `extractKotlinFunctionParams - object 내 메서드`() {
        val code = """
            object DatabaseConnection {
                fun query(sql: String) {
                    println("Executing: ${'$'}sql")
                }
            }
        """.trimIndent()
        val result = VariableExtractor.extractKotlinFunctionParams(code)
        assertEquals(listOf("sql"), result["query"])
    }

    // ========== 함수 호출 인자 추출 테스트 (신규) ==========

    @Test
    fun `extractKotlinFunctionCalls - 단일 문자열 인자 추출`() {
        val code = """query("SELECT * FROM users")"""
        val result = VariableExtractor.extractKotlinFunctionCalls(code)
        assertEquals(listOf("SELECT * FROM users"), result["query"])
    }

    @Test
    fun `extractKotlinFunctionCalls - object 메서드 호출`() {
        val code = """DatabaseConnection.query("SELECT * FROM users")"""
        val result = VariableExtractor.extractKotlinFunctionCalls(code)
        assertEquals(listOf("SELECT * FROM users"), result["query"])
    }

    @Test
    fun `extractKotlinFunctionCalls - 여러 인자 추출`() {
        val code = """greet("John", "Hello")"""
        val result = VariableExtractor.extractKotlinFunctionCalls(code)
        assertEquals(listOf("John", "Hello"), result["greet"])
    }

    @Test
    fun `extractKotlinFunctionCalls - 여러 함수 호출`() {
        val code = """
            query("SELECT * FROM users")
            connect("localhost", "5432")
        """.trimIndent()
        val result = VariableExtractor.extractKotlinFunctionCalls(code)

        assertEquals(listOf("SELECT * FROM users"), result["query"])
        assertEquals(listOf("localhost", "5432"), result["connect"])
    }

    @Test
    fun `extractKotlinFunctionCalls - 숫자 인자 추출`() {
        val code = """setPort(8080)"""
        val result = VariableExtractor.extractKotlinFunctionCalls(code)
        assertEquals(listOf("8080"), result["setPort"])
    }

    @Test
    fun `extractKotlinFunctionCalls - 혼합 인자 추출`() {
        val code = """configure("localhost", 8080)"""
        val result = VariableExtractor.extractKotlinFunctionCalls(code)
        assertEquals(listOf("localhost", "8080"), result["configure"])
    }
}
