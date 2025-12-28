package com.codeblueprint.data.interpreter

/**
 * 코드에서 변수 선언을 추출하는 유틸리티
 * 각 언어별 변수 선언 패턴을 파싱하여 변수명과 값을 추출
 */
object VariableExtractor {

    /**
     * Kotlin 코드에서 변수 선언 추출
     * 지원 패턴:
     * - val name = "value"
     * - var name = "value"
     * - val name = 123
     */
    fun extractKotlinVariables(code: String): Map<String, String> {
        val variables = mutableMapOf<String, String>()

        // 문자열 변수: val/var name = "value"
        val stringPattern = """(?:val|var)\s+(\w+)\s*=\s*"([^"]*)"""".toRegex()
        stringPattern.findAll(code).forEach { match ->
            variables[match.groupValues[1]] = match.groupValues[2]
        }

        // 숫자 변수: val/var name = 123 또는 123.45
        val numericPattern = """(?:val|var)\s+(\w+)\s*=\s*(-?\d+(?:\.\d+)?)(?!\s*[+\-*/])""".toRegex()
        numericPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // boolean 변수: val/var name = true/false
        val boolPattern = """(?:val|var)\s+(\w+)\s*=\s*(true|false)""".toRegex()
        boolPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        return variables
    }

    /**
     * Java 코드에서 변수 선언 추출
     * 지원 패턴:
     * - String name = "value";
     * - int name = 123;
     */
    fun extractJavaVariables(code: String): Map<String, String> {
        val variables = mutableMapOf<String, String>()

        // 문자열 변수
        val stringPattern = """(?:String|var)\s+(\w+)\s*=\s*"([^"]*)"""".toRegex()
        stringPattern.findAll(code).forEach { match ->
            variables[match.groupValues[1]] = match.groupValues[2]
        }

        // 숫자 변수
        val numericPattern = """(?:int|long|double|float|var)\s+(\w+)\s*=\s*(-?\d+(?:\.\d+)?)""".toRegex()
        numericPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // boolean 변수
        val boolPattern = """(?:boolean|var)\s+(\w+)\s*=\s*(true|false)""".toRegex()
        boolPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        return variables
    }

    /**
     * Python 코드에서 변수 선언 추출
     * 지원 패턴:
     * - name = "value"
     * - name = 123
     */
    fun extractPythonVariables(code: String): Map<String, String> {
        val variables = mutableMapOf<String, String>()

        // 문자열 변수 (큰따옴표)
        val stringDoublePattern = """(\w+)\s*=\s*"([^"]*)"""".toRegex()
        stringDoublePattern.findAll(code).forEach { match ->
            variables[match.groupValues[1]] = match.groupValues[2]
        }

        // 문자열 변수 (작은따옴표)
        val stringSinglePattern = """(\w+)\s*=\s*'([^']*)'""".toRegex()
        stringSinglePattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // 숫자 변수
        val numericPattern = """(\w+)\s*=\s*(-?\d+(?:\.\d+)?)(?!\s*[+\-*/])""".toRegex()
        numericPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // boolean 변수 (Python: True/False)
        val boolPattern = """(\w+)\s*=\s*(True|False)""".toRegex()
        boolPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2].lowercase()
            }
        }

        return variables
    }

    /**
     * JavaScript 코드에서 변수 선언 추출
     * 지원 패턴:
     * - const name = "value"
     * - let name = "value"
     * - var name = "value"
     */
    fun extractJavaScriptVariables(code: String): Map<String, String> {
        val variables = mutableMapOf<String, String>()

        // 문자열 변수 (큰따옴표)
        val stringDoublePattern = """(?:const|let|var)\s+(\w+)\s*=\s*"([^"]*)"""".toRegex()
        stringDoublePattern.findAll(code).forEach { match ->
            variables[match.groupValues[1]] = match.groupValues[2]
        }

        // 문자열 변수 (작은따옴표)
        val stringSinglePattern = """(?:const|let|var)\s+(\w+)\s*=\s*'([^']*)'""".toRegex()
        stringSinglePattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // 숫자 변수
        val numericPattern = """(?:const|let|var)\s+(\w+)\s*=\s*(-?\d+(?:\.\d+)?)""".toRegex()
        numericPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // boolean 변수
        val boolPattern = """(?:const|let|var)\s+(\w+)\s*=\s*(true|false)""".toRegex()
        boolPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        return variables
    }

    /**
     * Swift 코드에서 변수 선언 추출
     * 지원 패턴:
     * - let name = "value"
     * - var name = "value"
     */
    fun extractSwiftVariables(code: String): Map<String, String> {
        val variables = mutableMapOf<String, String>()

        // 문자열 변수
        val stringPattern = """(?:let|var)\s+(\w+)\s*=\s*"([^"]*)"""".toRegex()
        stringPattern.findAll(code).forEach { match ->
            variables[match.groupValues[1]] = match.groupValues[2]
        }

        // 숫자 변수
        val numericPattern = """(?:let|var)\s+(\w+)\s*=\s*(-?\d+(?:\.\d+)?)""".toRegex()
        numericPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        // boolean 변수
        val boolPattern = """(?:let|var)\s+(\w+)\s*=\s*(true|false)""".toRegex()
        boolPattern.findAll(code).forEach { match ->
            val varName = match.groupValues[1]
            if (!variables.containsKey(varName)) {
                variables[varName] = match.groupValues[2]
            }
        }

        return variables
    }
}
