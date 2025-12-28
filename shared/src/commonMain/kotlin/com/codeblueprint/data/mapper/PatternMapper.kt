package com.codeblueprint.data.mapper

import com.codeblueprint.db.PatternEntity
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.model.ProgrammingLanguage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * PatternEntity <-> DesignPattern 변환 매퍼
 */
class PatternMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: PatternEntity): DesignPattern {
        return DesignPattern(
            id = entity.id,
            name = entity.name,
            koreanName = entity.korean_name,
            category = PatternCategory.valueOf(entity.category),
            purpose = entity.purpose,
            characteristics = parseStringList(entity.characteristics),
            advantages = parseStringList(entity.advantages),
            disadvantages = parseStringList(entity.disadvantages),
            useCases = parseStringList(entity.use_cases),
            codeExamples = parseCodeExamples(entity.code_examples),
            diagram = entity.diagram,
            relatedPatternIds = parseStringList(entity.related_pattern_ids),
            difficulty = Difficulty.valueOf(entity.difficulty),
            frequency = entity.frequency.toInt()
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<PatternEntity>): List<DesignPattern> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain 모델 -> Entity 값 목록 반환 (insert용)
     */
    fun toEntityValues(domain: DesignPattern): EntityValues {
        return EntityValues(
            id = domain.id,
            name = domain.name,
            koreanName = domain.koreanName,
            category = domain.category.name,
            purpose = domain.purpose,
            characteristics = serializeStringList(domain.characteristics),
            advantages = serializeStringList(domain.advantages),
            disadvantages = serializeStringList(domain.disadvantages),
            useCases = serializeStringList(domain.useCases),
            codeExamples = serializeCodeExamples(domain.codeExamples),
            diagram = domain.diagram,
            relatedPatternIds = serializeStringList(domain.relatedPatternIds),
            difficulty = domain.difficulty.name,
            frequency = domain.frequency.toLong()
        )
    }

    /**
     * JSON 문자열 -> 문자열 리스트 파싱
     */
    private fun parseStringList(jsonString: String): List<String> {
        return try {
            json.decodeFromString<List<String>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 문자열 리스트 -> JSON 문자열 직렬화
     */
    private fun serializeStringList(list: List<String>): String {
        return json.encodeToString(list)
    }

    /**
     * JSON 문자열 -> CodeExample 리스트 파싱
     */
    private fun parseCodeExamples(jsonString: String): List<CodeExample> {
        return try {
            val dtos = json.decodeFromString<List<CodeExampleDto>>(jsonString)
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
        return json.encodeToString(dtos)
    }

    /**
     * 코드 예시 DTO (JSON 직렬화용)
     */
    @Serializable
    private data class CodeExampleDto(
        val language: String,
        val code: String,
        val explanation: String
    )

    /**
     * Entity 값 저장용 데이터 클래스
     */
    data class EntityValues(
        val id: String,
        val name: String,
        val koreanName: String,
        val category: String,
        val purpose: String,
        val characteristics: String,
        val advantages: String,
        val disadvantages: String,
        val useCases: String,
        val codeExamples: String,
        val diagram: String,
        val relatedPatternIds: String,
        val difficulty: String,
        val frequency: Long
    )
}
