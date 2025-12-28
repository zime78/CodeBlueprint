package com.codeblueprint.domain.model

import kotlinx.serialization.Serializable

/**
 * 코드 예제의 샘플 입력 데이터
 *
 * @property name 입력 변수명 (예: "arr", "target")
 * @property value 입력 값 (예: "[1, 2, 3]", "5")
 * @property type 입력 타입 (예: "IntArray", "Int")
 * @property description 입력 설명 (선택)
 */
@Serializable
data class SampleInput(
    val name: String,
    val value: String,
    val type: String,
    val description: String = ""
)
