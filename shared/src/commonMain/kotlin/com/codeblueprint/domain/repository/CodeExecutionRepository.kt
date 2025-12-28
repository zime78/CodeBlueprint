package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage

/**
 * 코드 실행 Repository 인터페이스
 */
interface CodeExecutionRepository {
    /**
     * 코드 실행
     *
     * @param code 실행할 소스 코드
     * @param language 프로그래밍 언어
     * @param expectedOutput 미리 정의된 예상 출력값 (있으면 시뮬레이션 대신 이 값 반환)
     * @return 코드 실행 결과
     */
    suspend fun executeCode(
        code: String,
        language: ProgrammingLanguage,
        expectedOutput: String? = null
    ): CodeExecutionResult

    /**
     * 지원 언어 목록
     */
    fun getSupportedLanguages(): List<ProgrammingLanguage>
}
