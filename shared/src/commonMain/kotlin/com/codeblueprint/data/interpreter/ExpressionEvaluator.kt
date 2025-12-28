package com.codeblueprint.data.interpreter

/**
 * 간단한 표현식 평가기
 * 지원 표현식:
 * - 숫자 연산: 1 + 1, 2 * 3, 10 / 2, 5 - 3
 * - 비교 연산: a == b, a != b, a === b
 * - 문자열 연결: "a" + "b"
 */
object ExpressionEvaluator {

    /**
     * 표현식을 평가하여 결과 문자열 반환
     *
     * @param expression 평가할 표현식
     * @param variables 변수 맵 (이름 -> 값)
     * @return 평가된 결과 문자열
     */
    fun evaluate(expression: String, variables: Map<String, String>): String {
        val trimmed = expression.trim()

        // 1. 단순 변수 참조
        if (trimmed.matches("""\w+""".toRegex())) {
            return variables[trimmed] ?: trimmed
        }

        // 2. 숫자 연산 (1 + 1, 2 * 3, etc.)
        evaluateArithmetic(trimmed, variables)?.let { return it }

        // 3. 동등 비교 (a == b, a === b, a != b)
        evaluateEquality(trimmed, variables)?.let { return it }

        // 4. 문자열 연결 ("a" + "b" 또는 변수 + 변수)
        evaluateStringConcat(trimmed, variables)?.let { return it }

        // 평가 불가 시 원본 반환
        return trimmed
    }

    /**
     * 산술 연산 평가
     * 지원: +, -, *, /
     */
    private fun evaluateArithmetic(
        expr: String,
        variables: Map<String, String>
    ): String? {
        // 패턴: operand operator operand
        val pattern = """(.+?)\s*([+\-*/])\s*(.+)""".toRegex()
        val match = pattern.find(expr) ?: return null

        val leftStr = match.groupValues[1].trim()
        val op = match.groupValues[2]
        val rightStr = match.groupValues[3].trim()

        // 변수 치환 후 숫자로 변환
        val left = resolveToNumber(leftStr, variables) ?: return null
        val right = resolveToNumber(rightStr, variables) ?: return null

        val result = when (op) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> if (right != 0.0) left / right else return "Infinity"
            else -> return null
        }

        // 정수 결과면 정수로 반환
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    /**
     * 동등 비교 평가
     * 지원: ==, ===, !=
     */
    private fun evaluateEquality(
        expr: String,
        variables: Map<String, String>
    ): String? {
        // 패턴: a === b 또는 a == b 또는 a != b
        val pattern = """(.+?)\s*(===?|!=)\s*(.+)""".toRegex()
        val match = pattern.find(expr) ?: return null

        val leftStr = match.groupValues[1].trim()
        val operator = match.groupValues[2]
        val rightStr = match.groupValues[3].trim()

        val left = resolveValue(leftStr, variables)
        val right = resolveValue(rightStr, variables)

        return when (operator) {
            "==", "===" -> (left == right).toString()
            "!=" -> (left != right).toString()
            else -> null
        }
    }

    /**
     * 문자열 연결 평가
     * 지원: "str1" + "str2" 또는 변수 + 변수
     */
    private fun evaluateStringConcat(
        expr: String,
        variables: Map<String, String>
    ): String? {
        if (!expr.contains("+")) return null

        // 문자열 리터럴 또는 변수로 구성된 + 연산만 처리
        val parts = splitByPlus(expr)
        if (parts.isEmpty()) return null

        val result = StringBuilder()

        for (part in parts) {
            val trimmed = part.trim()
            when {
                // 문자열 리터럴 (큰따옴표)
                trimmed.startsWith("\"") && trimmed.endsWith("\"") -> {
                    result.append(trimmed.removeSurrounding("\""))
                }
                // 문자열 리터럴 (작은따옴표)
                trimmed.startsWith("'") && trimmed.endsWith("'") -> {
                    result.append(trimmed.removeSurrounding("'"))
                }
                // 변수
                variables.containsKey(trimmed) -> {
                    result.append(variables[trimmed])
                }
                // 숫자
                trimmed.toDoubleOrNull() != null -> {
                    result.append(trimmed)
                }
                else -> return null // 처리 불가
            }
        }

        return result.toString()
    }

    /**
     * + 연산자로 문자열 분리 (문자열 리터럴 내부 + 제외)
     */
    private fun splitByPlus(expr: String): List<String> {
        val parts = mutableListOf<String>()
        var current = StringBuilder()
        var inString = false
        var stringChar = ' '

        for (i in expr.indices) {
            val c = expr[i]

            when {
                !inString && (c == '"' || c == '\'') -> {
                    inString = true
                    stringChar = c
                    current.append(c)
                }
                inString && c == stringChar -> {
                    inString = false
                    current.append(c)
                }
                !inString && c == '+' -> {
                    if (current.isNotBlank()) {
                        parts.add(current.toString().trim())
                    }
                    current = StringBuilder()
                }
                else -> current.append(c)
            }
        }

        if (current.isNotBlank()) {
            parts.add(current.toString().trim())
        }

        return parts
    }

    /**
     * 문자열을 숫자로 변환 (변수 참조 포함)
     */
    private fun resolveToNumber(str: String, variables: Map<String, String>): Double? {
        // 직접 숫자인 경우
        str.toDoubleOrNull()?.let { return it }

        // 변수인 경우
        val varValue = variables[str]
        return varValue?.toDoubleOrNull()
    }

    /**
     * 값 해석 (변수 또는 리터럴)
     */
    private fun resolveValue(str: String, variables: Map<String, String>): String {
        // 문자열 리터럴
        if ((str.startsWith("\"") && str.endsWith("\"")) ||
            (str.startsWith("'") && str.endsWith("'"))
        ) {
            return str.removeSurrounding("\"").removeSurrounding("'")
        }

        // 변수
        return variables[str] ?: str
    }
}
