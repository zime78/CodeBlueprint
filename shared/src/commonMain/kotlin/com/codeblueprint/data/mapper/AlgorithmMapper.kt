package com.codeblueprint.data.mapper

import com.codeblueprint.db.AlgorithmEntity
import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * AlgorithmEntity <-> Algorithm 변환 매퍼
 */
class AlgorithmMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: AlgorithmEntity): Algorithm {
        return Algorithm(
            id = entity.id,
            name = entity.name,
            koreanName = entity.korean_name,
            category = AlgorithmCategory.fromString(entity.category),
            purpose = entity.purpose,
            timeComplexity = TimeComplexity(
                best = entity.time_complexity_best,
                average = entity.time_complexity_average,
                worst = entity.time_complexity_worst
            ),
            spaceComplexity = entity.space_complexity,
            characteristics = parseStringList(entity.characteristics),
            advantages = parseStringList(entity.advantages),
            disadvantages = parseStringList(entity.disadvantages),
            useCases = parseStringList(entity.use_cases),
            codeExamples = parseCodeExamples(entity.code_examples),
            relatedAlgorithmIds = parseStringList(entity.related_algorithm_ids),
            difficulty = Difficulty.valueOf(entity.difficulty),
            frequency = entity.frequency.toInt()
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<AlgorithmEntity>): List<Algorithm> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain 모델 -> Entity 값 목록 반환 (insert용)
     */
    fun toEntityValues(domain: Algorithm): EntityValues {
        return EntityValues(
            id = domain.id,
            name = domain.name,
            koreanName = domain.koreanName,
            category = domain.category.name,
            purpose = domain.purpose,
            timeComplexityBest = domain.timeComplexity.best,
            timeComplexityAverage = domain.timeComplexity.average,
            timeComplexityWorst = domain.timeComplexity.worst,
            spaceComplexity = domain.spaceComplexity,
            characteristics = serializeStringList(domain.characteristics),
            advantages = serializeStringList(domain.advantages),
            disadvantages = serializeStringList(domain.disadvantages),
            useCases = serializeStringList(domain.useCases),
            codeExamples = serializeCodeExamples(domain.codeExamples),
            relatedAlgorithmIds = serializeStringList(domain.relatedAlgorithmIds),
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
        val timeComplexityBest: String,
        val timeComplexityAverage: String,
        val timeComplexityWorst: String,
        val spaceComplexity: String,
        val characteristics: String,
        val advantages: String,
        val disadvantages: String,
        val useCases: String,
        val codeExamples: String,
        val relatedAlgorithmIds: String,
        val difficulty: String,
        val frequency: Long
    )
}
