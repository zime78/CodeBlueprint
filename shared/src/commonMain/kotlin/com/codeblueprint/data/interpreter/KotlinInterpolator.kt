package com.codeblueprint.data.interpreter

/**
 * Kotlin 문자열 템플릿 처리기
 * 지원 패턴:
 * - $변수명: 단순 변수 참조
 * - ${표현식}: 표현식 평가
 */
class KotlinInterpolator : StringInterpolator {

    // ${expression} 패턴
    private val bracedPattern = """\$\{([^}]+)\}""".toRegex()

    // $variableName 패턴 ($ 뒤에 영문자/언더스코어로 시작하는 식별자)
    private val simplePattern = """\$([a-zA-Z_][a-zA-Z0-9_]*)""".toRegex()

    override fun interpolate(template: String, variables: Map<String, String>): String {
        var result = template

        // 1. ${expression} 처리 (먼저 처리해야 $variable이 중첩되지 않음)
        result = bracedPattern.replace(result) { match ->
            val expression = match.groupValues[1]
            ExpressionEvaluator.evaluate(expression, variables)
        }

        // 2. $variableName 처리
        result = simplePattern.replace(result) { match ->
            val varName = match.groupValues[1]
            variables[varName] ?: match.value // 변수가 없으면 원본 유지
        }

        return result
    }
}
