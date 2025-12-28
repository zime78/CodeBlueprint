package com.codeblueprint.data.mapper

import com.codeblueprint.db.LearningProgressEntity
import com.codeblueprint.domain.model.LearningProgress

/**
 * LearningProgressEntity <-> LearningProgress 변환 매퍼
 */
class LearningProgressMapper {

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: LearningProgressEntity): LearningProgress {
        return LearningProgress(
            patternId = entity.pattern_id,
            isCompleted = entity.is_completed == 1L,
            lastViewedAt = entity.last_viewed_at,
            notes = entity.notes,
            isBookmarked = entity.is_bookmarked == 1L
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<LearningProgressEntity>): List<LearningProgress> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain 모델 -> Entity 값 목록 반환 (insert용)
     */
    fun toEntityValues(domain: LearningProgress): EntityValues {
        return EntityValues(
            patternId = domain.patternId,
            isCompleted = if (domain.isCompleted) 1L else 0L,
            lastViewedAt = domain.lastViewedAt,
            notes = domain.notes,
            isBookmarked = if (domain.isBookmarked) 1L else 0L
        )
    }

    /**
     * Entity 값 저장용 데이터 클래스
     */
    data class EntityValues(
        val patternId: String,
        val isCompleted: Long,
        val lastViewedAt: Long,
        val notes: String?,
        val isBookmarked: Long
    )
}
