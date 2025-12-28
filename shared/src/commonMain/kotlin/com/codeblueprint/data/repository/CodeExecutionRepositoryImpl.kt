package com.codeblueprint.data.repository

import com.codeblueprint.data.interpreter.JavaScriptInterpolator
import com.codeblueprint.data.interpreter.KotlinInterpolator
import com.codeblueprint.data.interpreter.PythonInterpolator
import com.codeblueprint.data.interpreter.SwiftInterpolator
import com.codeblueprint.data.interpreter.VariableExtractor
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

    private val kotlinInterpolator = KotlinInterpolator()
    private val pythonInterpolator = PythonInterpolator()
    private val javaScriptInterpolator = JavaScriptInterpolator()
    private val swiftInterpolator = SwiftInterpolator()

    /**
     * 코드 실행
     *
     * expectedOutput이 제공되면 해당 값을 반환,
     * 없으면 시뮬레이션된 결과 반환
     */
    override suspend fun executeCode(
        code: String,
        language: ProgrammingLanguage,
        expectedOutput: String?
    ): CodeExecutionResult {
        // 실행 시뮬레이션을 위한 딜레이
        delay(500)

        return try {
            // expectedOutput이 있으면 해당 값 사용, 없으면 시뮬레이션
            val output = if (!expectedOutput.isNullOrBlank()) {
                expectedOutput
            } else {
                simulateExecution(code, language)
            }

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
     * 문자열 템플릿 ($var, ${expr}) 지원
     * 함수 파라미터 바인딩 지원
     */
    private fun simulateKotlinExecution(code: String, output: StringBuilder) {
        // 1. 전역 변수 추출
        val variables = VariableExtractor.extractKotlinVariables(code).toMutableMap()

        // 2. 함수 파라미터 및 호출 인자 추출
        val functionParams = VariableExtractor.extractKotlinFunctionParams(code)
        val functionCalls = VariableExtractor.extractKotlinFunctionCalls(code)

        // 3. 호출된 함수의 파라미터에 인자 값 바인딩
        functionCalls.forEach { (funcName, args) ->
            val params = functionParams[funcName] ?: return@forEach
            params.forEachIndexed { index, paramName ->
                if (index < args.size) {
                    variables[paramName] = args[index]
                }
            }
        }

        // println("...") 문 추출 및 문자열 보간 처리
        val printlnStringPattern = """println\s*\(\s*"([^"]*)"\s*\)""".toRegex()
        printlnStringPattern.findAll(code).forEach { match ->
            val template = match.groupValues[1]
            val interpolated = kotlinInterpolator.interpolate(template, variables)
            output.appendLine(interpolated)
        }

        // println(변수) 문 처리
        val printlnVarPattern = """println\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()
        printlnVarPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            // 이미 문자열 println으로 처리된 것은 제외
            val fullMatch = match.value
            if (!code.contains("""println\s*\(\s*"[^"]*$varName[^"]*"\s*\)""".toRegex())) {
                val value = variables[varName] ?: varName
                output.appendLine(value)
            }
        }

        // Singleton 패턴 감지 (object 키워드 사용 시)
        if (code.contains("object") && code.contains("getInstance")) {
            output.appendLine("Singleton instance created")
        }

        // === 연산자 결과 시뮬레이션 (Singleton 비교)
        if (code.contains("===") && (code.contains("Singleton") || code.contains("object"))) {
            // 이미 출력에 true가 없는 경우만 추가
            if (!output.contains("true")) {
                output.appendLine("true")
            }
        }

        // main 함수 존재 시 (출력이 없는 경우만)
        if (code.contains("fun main") && output.isEmpty()) {
            output.appendLine("Program executed successfully")
        }
    }

    /**
     * Java 코드 실행 시뮬레이션
     * 문자열 연결 (+) 처리 지원
     */
    private fun simulateJavaExecution(code: String, output: StringBuilder) {
        // 변수 추출
        val variables = VariableExtractor.extractJavaVariables(code)

        // System.out.println("...") 단순 문자열
        val printlnStringPattern = """System\.out\.println\s*\(\s*"([^"]*)"\s*\)""".toRegex()
        printlnStringPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // System.out.println("..." + var + "...") 문자열 연결
        val printlnConcatPattern = """System\.out\.println\s*\(\s*(.+?)\s*\)\s*;""".toRegex()
        printlnConcatPattern.findAll(code).forEach { match ->
            val content = match.groupValues[1].trim()
            // 이미 처리된 단순 문자열은 건너뛰기
            if (content.startsWith("\"") && content.endsWith("\"") && !content.contains("+")) {
                return@forEach
            }
            // + 연산자가 있는 경우 문자열 연결 처리
            if (content.contains("+")) {
                val result = evaluateJavaStringConcat(content, variables)
                if (result != null && !output.contains(result)) {
                    output.appendLine(result)
                }
            }
        }

        // main 메서드 존재 시 (출력이 없는 경우만)
        if (code.contains("public static void main") && output.isEmpty()) {
            output.appendLine("Program executed successfully")
        }
    }

    /**
     * Java 문자열 연결 평가
     */
    private fun evaluateJavaStringConcat(content: String, variables: Map<String, String>): String? {
        val parts = mutableListOf<String>()
        var current = StringBuilder()
        var inString = false

        for (c in content) {
            when {
                !inString && c == '"' -> {
                    inString = true
                }
                inString && c == '"' -> {
                    inString = false
                    parts.add(current.toString())
                    current = StringBuilder()
                }
                inString -> {
                    current.append(c)
                }
                !inString && c == '+' -> {
                    // + 연산자, 현재 토큰 처리
                    if (current.isNotBlank()) {
                        val token = current.toString().trim()
                        if (token.isNotEmpty() && !token.startsWith("\"")) {
                            // 변수 또는 표현식
                            val value = variables[token]
                            if (value != null) {
                                parts.add(value)
                            } else if (token.contains("==")) {
                                // 비교 연산
                                parts.add(evaluateBooleanExpr(token, variables))
                            }
                        }
                        current = StringBuilder()
                    }
                }
                !inString && !c.isWhitespace() -> {
                    current.append(c)
                }
            }
        }

        // 마지막 토큰 처리
        if (current.isNotBlank()) {
            val token = current.toString().trim()
            val value = variables[token]
            if (value != null) {
                parts.add(value)
            } else if (token.contains("==")) {
                parts.add(evaluateBooleanExpr(token, variables))
            }
        }

        return if (parts.isNotEmpty()) parts.joinToString("") else null
    }

    /**
     * boolean 표현식 평가 (a == b)
     */
    private fun evaluateBooleanExpr(expr: String, variables: Map<String, String>): String {
        val pattern = """(\w+)\s*==\s*(\w+)""".toRegex()
        val match = pattern.find(expr) ?: return expr

        val left = variables[match.groupValues[1]] ?: match.groupValues[1]
        val right = variables[match.groupValues[2]] ?: match.groupValues[2]

        return (left == right).toString()
    }

    /**
     * Python 코드 실행 시뮬레이션
     * f-string ({var}) 지원
     */
    private fun simulatePythonExecution(code: String, output: StringBuilder) {
        // 변수 추출
        val variables = VariableExtractor.extractPythonVariables(code)

        // print(f"...") f-string 처리
        val fstringPattern = """print\s*\(\s*f["'](.+?)["']\s*\)""".toRegex()
        fstringPattern.findAll(code).forEach { match ->
            val template = match.groupValues[1]
            val interpolated = pythonInterpolator.interpolate(template, variables)
            output.appendLine(interpolated)
        }

        // print("...") 일반 문자열
        val printStringPattern = """print\s*\(\s*["']([^"'{}]*)["']\s*\)""".toRegex()
        printStringPattern.findAll(code).forEach { match ->
            val content = match.groupValues[1]
            // f-string으로 이미 처리된 것은 건너뛰기
            if (!code.contains("f\"$content\"") && !code.contains("f'$content'")) {
                output.appendLine(content)
            }
        }

        // print(변수) 처리
        val printVarPattern = """print\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()
        printVarPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            val value = variables[varName] ?: varName
            output.appendLine(value)
        }

        // 클래스 정의 감지
        if (code.contains("class") && code.contains("def __init__")) {
            if (!output.contains("Class instance created")) {
                output.appendLine("Class instance created")
            }
        }
    }

    /**
     * JavaScript 코드 실행 시뮬레이션
     * 템플릿 리터럴 (`${var}`) 지원
     */
    private fun simulateJavaScriptExecution(code: String, output: StringBuilder) {
        // 변수 추출
        val variables = VariableExtractor.extractJavaScriptVariables(code)

        // console.log(`...`) 템플릿 리터럴 처리
        val templateLiteralPattern = """console\.log\s*\(\s*`([^`]*)`\s*\)""".toRegex()
        templateLiteralPattern.findAll(code).forEach { match ->
            val template = match.groupValues[1]
            val interpolated = javaScriptInterpolator.interpolate(template, variables)
            output.appendLine(interpolated)
        }

        // console.log("...") 또는 console.log('...') 일반 문자열
        val consoleLogStringPattern = """console\.log\s*\(\s*["']([^"']*)["']\s*\)""".toRegex()
        consoleLogStringPattern.findAll(code).forEach { match ->
            output.appendLine(match.groupValues[1])
        }

        // console.log(변수) 처리
        val consoleLogVarPattern = """console\.log\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()
        consoleLogVarPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            val value = variables[varName] ?: varName
            output.appendLine(value)
        }

        // function 정의 감지 (출력이 없는 경우만)
        if ((code.contains("function") || code.contains("const") || code.contains("let")) && output.isEmpty()) {
            output.appendLine("Script executed successfully")
        }
    }

    /**
     * Swift 코드 실행 시뮬레이션
     * 문자열 보간 (\(var)) 지원
     */
    private fun simulateSwiftExecution(code: String, output: StringBuilder) {
        // 변수 추출
        val variables = VariableExtractor.extractSwiftVariables(code)

        // print("...") 처리 (문자열 보간 포함)
        val printPattern = """print\s*\(\s*"([^"]*)"\s*\)""".toRegex()
        printPattern.findAll(code).forEach { match ->
            val template = match.groupValues[1]
            val interpolated = swiftInterpolator.interpolate(template, variables)
            output.appendLine(interpolated)
        }

        // print(변수) 처리 - 문자열 print가 아닌 경우만
        val printVarPattern = """print\s*\(\s*([a-zA-Z_][a-zA-Z0-9_]*)\s*\)""".toRegex()
        printVarPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            val value = variables[varName] ?: varName
            output.appendLine(value)
        }

        // class/struct 정의 감지 (출력이 없는 경우만)
        if ((code.contains("class") || code.contains("struct")) && output.isEmpty()) {
            output.appendLine("Swift code executed successfully")
        }
    }
}
