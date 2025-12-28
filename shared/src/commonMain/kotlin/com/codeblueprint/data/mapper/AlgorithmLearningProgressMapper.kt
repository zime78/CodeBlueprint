package com.codeblueprint.data.mapper

import com.codeblueprint.db.AlgorithmLearningProgressEntity
import com.codeblueprint.domain.model.LearningProgress

/**
 * AlgorithmLearningProgressEntity <-> LearningProgress 변환 매퍼
 *
 * 알고리즘 학습 진도용 매퍼
 */
class AlgorithmLearningProgressMapper {

    /**
     * Entity -> Domain 모델 변환
     */
    fun toDomain(entity: AlgorithmLearningProgressEntity): LearningProgress {
        return LearningProgress(
            patternId = entity.algorithm_id, // patternId를 algorithmId로 사용
            isCompleted = entity.is_completed == 1L,
            lastViewedAt = entity.last_viewed_at,
            notes = entity.notes,
            isBookmarked = entity.is_bookmarked == 1L
        )
    }

    /**
     * Entity 리스트 -> Domain 모델 리스트 변환
     */
    fun toDomainList(entities: List<AlgorithmLearningProgressEntity>): List<LearningProgress> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain 모델 -> Entity 값 목록 반환 (insert용)
     */
    fun toEntityValues(domain: LearningProgress): EntityValues {
        return EntityValues(
            algorithmId = domain.patternId,
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
        val algorithmId: String,
        val isCompleted: Long,
        val lastViewedAt: Long,
        val notes: String?,
        val isBookmarked: Long
    )
}
