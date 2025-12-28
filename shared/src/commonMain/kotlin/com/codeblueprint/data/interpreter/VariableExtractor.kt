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

    // ========== Kotlin 함수 파라미터 및 호출 추출 (신규) ==========

    /**
     * Kotlin 함수 정의에서 파라미터 정보 추출
     * 지원 패턴:
     * - fun name(param1: Type1, param2: Type2) { }
     * - fun name() { }
     *
     * @param code 분석할 Kotlin 코드
     * @return Map<함수명, List<파라미터명>>
     */
    fun extractKotlinFunctionParams(code: String): Map<String, List<String>> {
        val functions = mutableMapOf<String, List<String>>()

        // 함수 정의 패턴: fun name(params)
        val functionPattern = """fun\s+(\w+)\s*\(([^)]*)\)""".toRegex()

        functionPattern.findAll(code).forEach { match ->
            val funcName = match.groupValues[1]
            val paramsStr = match.groupValues[2].trim()

            val params = if (paramsStr.isEmpty()) {
                emptyList()
            } else {
                paramsStr.split(",")
                    .map { param ->
                        // "name: Type" 또는 "name: Type = default" 에서 name만 추출
                        param.trim().split(":").first().trim()
                    }
                    .filter { it.isNotEmpty() }
            }
            functions[funcName] = params
        }

        return functions
    }

    /**
     * Kotlin 함수 호출에서 인자 값 추출
     * 지원 패턴:
     * - functionName("arg1", "arg2")
     * - object.methodName("arg1", "arg2")
     * - functionName(123)
     * - functionName("string", 123)
     *
     * @param code 분석할 Kotlin 코드
     * @return Map<함수명, List<인자값>>
     */
    fun extractKotlinFunctionCalls(code: String): Map<String, List<String>> {
        val calls = mutableMapOf<String, List<String>>()

        // 함수 호출 패턴: (object.)?methodName(args)
        // 함수 정의(fun 키워드)는 제외
        val callPattern = """(?:\w+\.)?(\w+)\s*\(([^)]*)\)""".toRegex()

        callPattern.findAll(code).forEach { match ->
            val funcName = match.groupValues[1]
            val argsStr = match.groupValues[2].trim()

            // 함수 정의(fun 키워드 뒤)가 아닌 실제 호출인지 확인
            val matchStart = match.range.first
            val prefix = if (matchStart >= 4) code.substring(matchStart - 4, matchStart) else ""
            if (prefix.contains("fun ") || prefix.endsWith("fun")) {
                return@forEach
            }

            // main, init 같은 특수 함수는 제외
            if (funcName in listOf("main", "init")) {
                return@forEach
            }

            val args = extractArgumentsInOrder(argsStr)
            if (args.isNotEmpty()) {
                calls[funcName] = args
            }
        }

        return calls
    }

    /**
     * 인자를 원래 순서대로 추출
     * 문자열 인자("value")와 숫자 인자(123) 지원
     */
    private fun extractArgumentsInOrder(argsStr: String): List<String> {
        if (argsStr.isEmpty()) return emptyList()

        val args = mutableListOf<String>()
        var index = 0

        while (index < argsStr.length) {
            // 공백 스킵
            while (index < argsStr.length && argsStr[index].isWhitespace()) {
                index++
            }

            if (index >= argsStr.length) break

            when {
                // 문자열 인자 (큰따옴표)
                argsStr[index] == '"' -> {
                    val endIndex = argsStr.indexOf('"', index + 1)
                    if (endIndex != -1) {
                        args.add(argsStr.substring(index + 1, endIndex))
                        index = endIndex + 1
                    } else {
                        index++
                    }
                }
                // 숫자 인자 (음수 포함)
                argsStr[index].isDigit() || (argsStr[index] == '-' && index + 1 < argsStr.length && argsStr[index + 1].isDigit()) -> {
                    val startIndex = index
                    if (argsStr[index] == '-') index++
                    while (index < argsStr.length &&
                        (argsStr[index].isDigit() || argsStr[index] == '.')) {
                        index++
                    }
                    args.add(argsStr.substring(startIndex, index))
                }
                // 쉼표 스킵
                argsStr[index] == ',' -> {
                    index++
                }
                else -> {
                    index++
                }
            }
        }

        return args
    }
}
