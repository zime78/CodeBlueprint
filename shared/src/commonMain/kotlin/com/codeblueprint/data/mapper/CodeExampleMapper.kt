package com.codeblueprint.data.mapper

import com.codeblueprint.db.CodeExampleEntity
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.SampleInput
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * CodeExampleEntity <-> CodeExample 변환 매퍼
 */
class CodeExampleMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: CodeExampleEntity): CodeExample {
        return CodeExample(
            id = entity.id,
            language = try {
                ProgrammingLanguage.valueOf(entity.language)
            } catch (e: Exception) {
                ProgrammingLanguage.KOTLIN
            },
            code = entity.code,
            explanation = entity.explanation,
            sampleInput = parseSampleInput(entity.sample_input),
            expectedOutput = entity.expected_output ?: "",
            displayOrder = entity.display_order.toInt()
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<CodeExampleEntity>): List<CodeExample> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain 모델 -> Entity 값 목록 반환 (insert용)
     *
     * @param domain CodeExample 도메인 모델
     * @param patternId 연관된 패턴 ID (패턴 코드 예제인 경우)
     * @param algorithmId 연관된 알고리즘 ID (알고리즘 코드 예제인 경우)
     * @param index 표시 순서 (displayOrder)
     */
    fun toEntityValues(
        domain: CodeExample,
        patternId: String?,
        algorithmId: String?,
        index: Int = 0
    ): EntityValues {
        val now = Clock.System.now().toEpochMilliseconds()
        val id = domain.id.ifEmpty {
            generateId(patternId, algorithmId, domain.language, index)
        }

        return EntityValues(
            id = id,
            patternId = patternId,
            algorithmId = algorithmId,
            language = domain.language.name,
            code = domain.code,
            explanation = domain.explanation,
            sampleInput = serializeSampleInput(domain.sampleInput),
            expectedOutput = domain.expectedOutput.takeIf { it.isNotBlank() },
            displayOrder = domain.displayOrder.toLong().takeIf { it > 0 } ?: index.toLong(),
            createdAt = now,
            updatedAt = now
        )
    }

    /**
     * 코드 예제 ID 생성
     * 형식: {parent_id}_{language}_{index}
     */
    fun generateId(
        patternId: String?,
        algorithmId: String?,
        language: ProgrammingLanguage,
        index: Int
    ): String {
        val parentId = patternId ?: algorithmId ?: "unknown"
        return "${parentId}_${language.name.lowercase()}_$index"
    }

    /**
     * sample_input JSON 문자열 -> SampleInput 리스트 파싱
     */
    fun parseSampleInput(jsonString: String?): List<SampleInput> {
        if (jsonString.isNullOrBlank()) return emptyList()
        return try {
            json.decodeFromString<List<SampleInput>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * SampleInput 리스트 -> JSON 문자열 직렬화
     */
    fun serializeSampleInput(inputs: List<SampleInput>): String? {
        if (inputs.isEmpty()) return null
        return json.encodeToString(inputs)
    }

    /**
     * Entity 값 저장용 데이터 클래스
     */
    data class EntityValues(
        val id: String,
        val patternId: String?,
        val algorithmId: String?,
        val language: String,
        val code: String,
        val explanation: String,
        val sampleInput: String?,
        val expectedOutput: String?,
        val displayOrder: Long,
        val createdAt: Long,
        val updatedAt: Long
    )
}
