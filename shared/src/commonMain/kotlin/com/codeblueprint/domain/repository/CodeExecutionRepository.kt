package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage

/**
 * 코드 실행 Repository 인터페이스
 */
interface CodeExecutionRepository {
    /**
     * 코드 실행
     */
    suspend fun executeCode(code: String, language: ProgrammingLanguage): CodeExecutionResult

    /**
     * 지원 언어 목록
     */
    fun getSupportedLanguages(): List<ProgrammingLanguage>
}
