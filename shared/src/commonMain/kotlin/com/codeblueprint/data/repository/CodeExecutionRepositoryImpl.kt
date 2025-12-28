package com.codeblueprint.data.repository

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.repository.CodeExecutionRepository
import kotlinx.coroutines.delay

/**
 * 코드 실행 Repository Mock 구현
 *
 * TODO: 실제 API 연동 시 JDoodle API 또는 Piston API 사용
 * - JDoodle API: https://www.jdoodle.com/compiler-api
 * - Piston API: https://github.com/engineer-man/piston
 */
class CodeExecutionRepositoryImpl : CodeExecutionRepository {

    /**
     * 코드 실행 (Mock 구현)
     * 실제 실행 대신 시뮬레이션된 결과 반환
     */
    override suspend fun executeCode(code: String, language: ProgrammingLanguage): CodeExecutionResult {
        // 실행 시뮬레이션을 위한 딜레이
        delay(500)

        return try {
            val output = simulateExecution(code, language)
            CodeExecutionResult(
                success = true,
                output = output,
                executionTimeMs = (100..500).random().toLong()
            )
        } catch (e: Exception) {
            CodeExecutionResult(
                success = false,
                output = "",
                errorMessage = e.message ?: "실행 중 오류가 발생했습니다."
            )
        }
    }

    override fun getSupportedLanguages(): List<ProgrammingLanguage> {
        return ProgrammingLanguage.entries.toList()
    }

    /**
     * 코드 실행 시뮬레이션
     * 간단한 패턴 매칭으로 출력 결과 생성
     */
    private fun simulateExecution(code: String, language: ProgrammingLanguage): String {
        val outputBuilder = StringBuilder()

        when (language) {
            ProgrammingLanguage.KOTLIN -> simulateKotlinExecution(code, outputBuilder)
            ProgrammingLanguage.JAVA -> simulateJavaExecution(code, outputBuilder)
            ProgrammingLanguage.PYTHON -> simulatePythonExecution(code, outputBuilder)
            ProgrammingLanguage.JAVASCRIPT -> simulateJavaScriptExecution(code, outputBuilder)
            ProgrammingLanguage.SWIFT -> simulateSwiftExecution(code, outputBuilder)
        }

        return if (outputBuilder.isEmpty()) {
            "// 실행 완료 (출력 없음)"
        } else {
            outputBuilder.toString().trimEnd()
        }
    }

    /**
     * Kotlin 코드 실행 시뮬레이션
     */
    private fun simulateKotlinExecution(code: String, output: StringBuilder) {
        // println 문 추출
        val printlnPattern = """println\s*\(\s*"([^"]*)"\s*\)""".toRegex()
        val printlnVarPattern = """println\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()
        val printlnExprPattern = """println\s*\(\s*(.+?)\s*\)""".toRegex()

        printlnPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // Singleton 패턴 감지
        if (code.contains("object") && code.contains("getInstance")) {
            output.appendLine("Singleton instance created")
        }

        // main 함수 존재 시
        if (code.contains("fun main")) {
            if (output.isEmpty()) {
                output.appendLine("Program executed successfully")
            }
        }

        // === 연산자 결과 시뮬레이션
        if (code.contains("===") && code.contains("Singleton")) {
            output.appendLine("true")
        }
    }

    /**
     * Java 코드 실행 시뮬레이션
     */
    private fun simulateJavaExecution(code: String, output: StringBuilder) {
        val printlnPattern = """System\.out\.println\s*\(\s*"([^"]*)"\s*\)""".toRegex()

        printlnPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        if (code.contains("public static void main")) {
            if (output.isEmpty()) {
                output.appendLine("Program executed successfully")
            }
        }
    }

    /**
     * Python 코드 실행 시뮬레이션
     */
    private fun simulatePythonExecution(code: String, output: StringBuilder) {
        val printPattern = """print\s*\(\s*["']([^"']*)["']\s*\)""".toRegex()
        val printVarPattern = """print\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()

        printPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // 클래스 정의 감지
        if (code.contains("class") && code.contains("def __init__")) {
            output.appendLine("Class instance created")
        }
    }

    /**
     * JavaScript 코드 실행 시뮬레이션
     */
    private fun simulateJavaScriptExecution(code: String, output: StringBuilder) {
        val consoleLogPattern = """console\.log\s*\(\s*["']([^"']*)["']\s*\)""".toRegex()

        consoleLogPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // function 정의 감지
        if (code.contains("function") || code.contains("const") || code.contains("let")) {
            if (output.isEmpty()) {
                output.appendLine("Script executed successfully")
            }
        }
    }

    /**
     * Swift 코드 실행 시뮬레이션
     */
    private fun simulateSwiftExecution(code: String, output: StringBuilder) {
        val printPattern = """print\s*\(\s*"([^"]*)"\s*\)""".toRegex()

        printPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // class 정의 감지
        if (code.contains("class") || code.contains("struct")) {
            if (output.isEmpty()) {
                output.appendLine("Swift code executed successfully")
            }
        }
    }
}
