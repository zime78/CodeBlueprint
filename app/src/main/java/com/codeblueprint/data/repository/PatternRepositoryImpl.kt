package com.codeblueprint.data.repository

import com.codeblueprint.data.local.db.LearningProgressDao
import com.codeblueprint.data.local.db.PatternDao
import com.codeblueprint.data.local.entity.LearningProgressEntity
import com.codeblueprint.data.mapper.LearningProgressMapper
import com.codeblueprint.data.mapper.PatternMapper
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.repository.PatternRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * PatternRepository 구현체
 *
 * Room 데이터베이스를 통해 패턴 데이터를 관리합니다.
 */
class PatternRepositoryImpl @Inject constructor(
    private val patternDao: PatternDao,
    private val progressDao: LearningProgressDao,
    private val patternMapper: PatternMapper,
    private val progressMapper: LearningProgressMapper
) : PatternRepository {

    override fun getAllPatterns(): Flow<List<DesignPattern>> {
        return patternDao.getAllPatterns().map { entities ->
            patternMapper.toDomainList(entities)
        }
    }

    override fun getPatternsByCategory(category: PatternCategory): Flow<List<DesignPattern>> {
        return patternDao.getPatternsByCategory(category.name).map { entities ->
            patternMapper.toDomainList(entities)
        }
    }

    override suspend fun getPatternById(patternId: String): DesignPattern? {
        return patternDao.getPatternById(patternId)?.let { entity ->
            patternMapper.toDomain(entity)
        }
    }

    override fun searchPatterns(query: String): Flow<List<DesignPattern>> {
        return patternDao.searchPatterns(query).map { entities ->
            patternMapper.toDomainList(entities)
        }
    }

    override suspend fun getRelatedPatterns(patternIds: List<String>): List<DesignPattern> {
        return patternDao.getPatternsByIds(patternIds).map { entity ->
            patternMapper.toDomain(entity)
        }
    }

    override fun getLearningProgress(patternId: String): Flow<LearningProgress?> {
        return progressDao.getProgress(patternId).map { entity ->
            entity?.let { progressMapper.toDomain(it) }
        }
    }

    override fun getAllLearningProgress(): Flow<List<LearningProgress>> {
        return progressDao.getAllProgress().map { entities ->
            progressMapper.toDomainList(entities)
        }
    }

    override suspend fun saveLearningProgress(progress: LearningProgress) {
        progressDao.upsertProgress(progressMapper.toEntity(progress))
    }

    override fun getBookmarkedPatterns(): Flow<List<DesignPattern>> {
        return progressDao.getBookmarkedPatternIds()
            .combine(patternDao.getAllPatterns()) { bookmarkedIds, allPatterns ->
                allPatterns.filter { it.id in bookmarkedIds }
                    .map { patternMapper.toDomain(it) }
            }
    }

    override suspend fun toggleBookmark(patternId: String) {
        val existing = progressDao.getProgressSync(patternId)
        if (existing != null) {
            progressDao.upsertProgress(existing.copy(isBookmarked = !existing.isBookmarked))
        } else {
            progressDao.upsertProgress(
                LearningProgressEntity(
                    patternId = patternId,
                    isBookmarked = true
                )
            )
        }
    }

    override suspend fun toggleComplete(patternId: String) {
        val existing = progressDao.getProgressSync(patternId)
        if (existing != null) {
            progressDao.upsertProgress(existing.copy(isCompleted = !existing.isCompleted))
        } else {
            progressDao.upsertProgress(
                LearningProgressEntity(
                    patternId = patternId,
                    isCompleted = true
                )
            )
        }
    }

    override fun getOverallProgress(): Flow<Float> {
        return progressDao.getCompletedCount().combine(
            patternDao.getAllPatterns()
        ) { completedCount, allPatterns ->
            if (allPatterns.isEmpty()) 0f
            else completedCount.toFloat() / allPatterns.size
        }
    }
}
