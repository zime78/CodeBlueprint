package com.codeblueprint.data.mapper

import com.codeblueprint.data.local.entity.PatternEntity
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * PatternEntity <-> DesignPattern 변환 매퍼
 */
class PatternMapper @Inject constructor(
    private val gson: Gson
) {
    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: PatternEntity): DesignPattern {
        return DesignPattern(
            id = entity.id,
            name = entity.name,
            koreanName = entity.koreanName,
            category = PatternCategory.valueOf(entity.category),
            purpose = entity.purpose,
            characteristics = entity.characteristics,
            advantages = entity.advantages,
            disadvantages = entity.disadvantages,
            useCases = entity.useCases,
            codeExamples = parseCodeExamples(entity.codeExamples),
            diagram = entity.diagram,
            relatedPatternIds = entity.relatedPatternIds,
            difficulty = Difficulty.valueOf(entity.difficulty),
            frequency = entity.frequency
        )
    }

    /**
     * Domain 모델 -> Entity 변환
     */
    fun toEntity(domain: DesignPattern): PatternEntity {
        return PatternEntity(
            id = domain.id,
            name = domain.name,
            koreanName = domain.koreanName,
            category = domain.category.name,
            purpose = domain.purpose,
            characteristics = domain.characteristics,
            advantages = domain.advantages,
            disadvantages = domain.disadvantages,
            useCases = domain.useCases,
            codeExamples = serializeCodeExamples(domain.codeExamples),
            diagram = domain.diagram,
            relatedPatternIds = domain.relatedPatternIds,
            difficulty = domain.difficulty.name,
            frequency = domain.frequency
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<PatternEntity>): List<DesignPattern> {
        return entities.map { toDomain(it) }
    }

    /**
     * JSON 문자열 -> CodeExample 리스트 파싱
     */
    private fun parseCodeExamples(json: String): List<CodeExample> {
        return try {
            val type = object : TypeToken<List<CodeExampleDto>>() {}.type
            val dtos: List<CodeExampleDto> = gson.fromJson(json, type)
            dtos.map { dto ->
                CodeExample(
                    language = ProgrammingLanguage.valueOf(dto.language),
                    code = dto.code,
                    explanation = dto.explanation
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * CodeExample 리스트 -> JSON 문자열 직렬화
     */
    private fun serializeCodeExamples(examples: List<CodeExample>): String {
        val dtos = examples.map { example ->
            CodeExampleDto(
                language = example.language.name,
                code = example.code,
                explanation = example.explanation
            )
        }
        return gson.toJson(dtos)
    }

    /**
     * 코드 예시 DTO (JSON 직렬화용)
     */
    private data class CodeExampleDto(
        val language: String,
        val code: String,
        val explanation: String
    )
}
