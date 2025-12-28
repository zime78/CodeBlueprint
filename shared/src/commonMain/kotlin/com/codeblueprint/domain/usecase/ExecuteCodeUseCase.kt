package com.codeblueprint.domain.usecase

import com.codeblueprint.domain.model.CodeExecutionResult
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.repository.CodeExecutionRepository

/**
 * 코드 실행 UseCase
 */
class ExecuteCodeUseCase(
    private val codeExecutionRepository: CodeExecutionRepository
) {
    /**
     * 코드 실행
     */
    suspend operator fun invoke(code: String, language: ProgrammingLanguage): CodeExecutionResult {
        return codeExecutionRepository.executeCode(code, language)
    }
}
