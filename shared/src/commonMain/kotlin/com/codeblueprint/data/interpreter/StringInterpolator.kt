package com.codeblueprint.data.interpreter

/**
 * 문자열 보간 처리 인터페이스
 * 각 프로그래밍 언어별로 구현하여 문자열 템플릿을 처리
 */
interface StringInterpolator {

    /**
     * 문자열 내의 변수/표현식을 실제 값으로 치환
     *
     * @param template 템플릿 문자열 (예: "Value: $x")
     * @param variables 변수 맵 (이름 -> 값)
     * @return 치환된 문자열
     */
    fun interpolate(template: String, variables: Map<String, String>): String
}
