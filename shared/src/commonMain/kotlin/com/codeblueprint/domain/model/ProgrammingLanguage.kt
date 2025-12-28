package com.codeblueprint.domain.model

/**
 * 코드 예시에서 지원하는 프로그래밍 언어
 */
enum class ProgrammingLanguage(
    val displayName: String,
    val extension: String
) {
    KOTLIN(displayName = "Kotlin", extension = "kt"),
    JAVA(displayName = "Java", extension = "java"),
    SWIFT(displayName = "Swift", extension = "swift"),
    PYTHON(displayName = "Python", extension = "py"),
    JAVASCRIPT(displayName = "JavaScript", extension = "js");

    companion object {
        fun fromString(value: String): ProgrammingLanguage {
            return entries.find { it.name == value } ?: KOTLIN
        }
    }
}
