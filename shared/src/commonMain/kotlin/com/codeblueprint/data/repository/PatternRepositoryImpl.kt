package com.codeblueprint.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.codeblueprint.data.mapper.LearningProgressMapper
import com.codeblueprint.data.mapper.PatternMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.repository.PatternRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * PatternRepository 구현체
 *
 * SQLDelight 데이터베이스를 통해 패턴 데이터를 관리합니다.
 */
class PatternRepositoryImpl(
    private val database: CodeBlueprintDatabase,
    private val patternMapper: PatternMapper,
    private val progressMapper: LearningProgressMapper
) : PatternRepository {

    private val queries = database.codeBlueprintQueries

    override fun getAllPatterns(): Flow<List<DesignPattern>> {
        return queries.getAllPatterns()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                patternMapper.toDomainList(entities)
            }
    }

    override fun getPatternsByCategory(category: PatternCategory): Flow<List<DesignPattern>> {
        return queries.getPatternsByCategory(category.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                patternMapper.toDomainList(entities)
            }
    }

    override suspend fun getPatternById(patternId: String): DesignPattern? {
        return queries.getPatternById(patternId)
            .executeAsOneOrNull()
            ?.let { patternMapper.toDomain(it) }
    }

    override fun searchPatterns(query: String): Flow<List<DesignPattern>> {
        return queries.searchPatterns(query, query, query, query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                patternMapper.toDomainList(entities)
            }
    }

    override suspend fun getRelatedPatterns(patternIds: List<String>): List<DesignPattern> {
        return queries.getPatternsByIds(patternIds)
            .executeAsList()
            .map { patternMapper.toDomain(it) }
    }

    override fun getLearningProgress(patternId: String): Flow<LearningProgress?> {
        return queries.getProgress(patternId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                entity?.let { progressMapper.toDomain(it) }
            }
    }

    override fun getAllLearningProgress(): Flow<List<LearningProgress>> {
        return queries.getAllProgress()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                progressMapper.toDomainList(entities)
            }
    }

    override suspend fun saveLearningProgress(progress: LearningProgress) {
        val values = progressMapper.toEntityValues(progress)
        queries.upsertProgress(
            pattern_id = values.patternId,
            is_completed = values.isCompleted,
            last_viewed_at = values.lastViewedAt,
            notes = values.notes,
            is_bookmarked = values.isBookmarked
        )
    }

    override fun getBookmarkedPatterns(): Flow<List<DesignPattern>> {
        return queries.getBookmarkedPatternIds()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .combine(getAllPatterns()) { bookmarkedIds, allPatterns ->
                allPatterns.filter { it.id in bookmarkedIds }
            }
    }

    override suspend fun toggleBookmark(patternId: String) {
        val existing = queries.getProgress(patternId).executeAsOneOrNull()
        val currentTime = Clock.System.now().toEpochMilliseconds()

        if (existing != null) {
            queries.upsertProgress(
                pattern_id = patternId,
                is_completed = existing.is_completed,
                last_viewed_at = currentTime,
                notes = existing.notes,
                is_bookmarked = if (existing.is_bookmarked == 1L) 0L else 1L
            )
        } else {
            queries.upsertProgress(
                pattern_id = patternId,
                is_completed = 0L,
                last_viewed_at = currentTime,
                notes = null,
                is_bookmarked = 1L
            )
        }
    }

    override suspend fun toggleComplete(patternId: String) {
        val existing = queries.getProgress(patternId).executeAsOneOrNull()
        val currentTime = Clock.System.now().toEpochMilliseconds()

        if (existing != null) {
            queries.upsertProgress(
                pattern_id = patternId,
                is_completed = if (existing.is_completed == 1L) 0L else 1L,
                last_viewed_at = currentTime,
                notes = existing.notes,
                is_bookmarked = existing.is_bookmarked
            )
        } else {
            queries.upsertProgress(
                pattern_id = patternId,
                is_completed = 1L,
                last_viewed_at = currentTime,
                notes = null,
                is_bookmarked = 0L
            )
        }
    }

    override fun getOverallProgress(): Flow<Float> {
        return queries.getCompletedCount()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .combine(
                queries.getPatternCount()
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.Default)
            ) { completedCount, totalCount ->
                val completed = completedCount ?: 0L
                val total = totalCount ?: 0L
                if (total == 0L) 0f else completed.toFloat() / total.toFloat()
            }
    }
}
