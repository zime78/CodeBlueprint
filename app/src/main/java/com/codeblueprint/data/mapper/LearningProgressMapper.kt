package com.codeblueprint.data.mapper

import com.codeblueprint.data.local.entity.LearningProgressEntity
import com.codeblueprint.domain.model.LearningProgress
import javax.inject.Inject

/**
 * LearningProgressEntity <-> LearningProgress 변환 매퍼
 */
class LearningProgressMapper @Inject constructor() {

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: LearningProgressEntity): LearningProgress {
        return LearningProgress(
            patternId = entity.patternId,
            isCompleted = entity.isCompleted,
            lastViewedAt = entity.lastViewedAt,
            notes = entity.notes,
            isBookmarked = entity.isBookmarked
        )
    }

    /**
     * Domain 모델 -> Entity 변환
     */
    fun toEntity(domain: LearningProgress): LearningProgressEntity {
        return LearningProgressEntity(
            patternId = domain.patternId,
            isCompleted = domain.isCompleted,
            lastViewedAt = domain.lastViewedAt,
            notes = domain.notes,
            isBookmarked = domain.isBookmarked
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<LearningProgressEntity>): List<LearningProgress> {
        return entities.map { toDomain(it) }
    }
}
