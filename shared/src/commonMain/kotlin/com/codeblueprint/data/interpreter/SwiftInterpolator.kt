package com.codeblueprint.data.interpreter

/**
 * Swift 문자열 보간 처리기
 * 지원 패턴:
 * - \(변수명): 단순 변수 참조
 * - \(표현식): 표현식 평가
 */
class SwiftInterpolator : StringInterpolator {

    // \(expression) 패턴
    private val interpolationPattern = """\\\(([^)]+)\)""".toRegex()

    override fun interpolate(template: String, variables: Map<String, String>): String {
        return interpolationPattern.replace(template) { match ->
            val expression = match.groupValues[1].trim()

            // 단순 변수 참조
            if (expression.matches("""\w+""".toRegex())) {
                return@replace variables[expression] ?: match.value
            }

            // 표현식 평가
            ExpressionEvaluator.evaluate(expression, variables)
        }
    }
}
