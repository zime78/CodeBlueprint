package com.codeblueprint.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.codeblueprint.data.mapper.AlgorithmLearningProgressMapper
import com.codeblueprint.data.mapper.AlgorithmMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.repository.AlgorithmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * AlgorithmRepository 구현체
 *
 * SQLDelight 데이터베이스를 통해 알고리즘 데이터를 관리합니다.
 */
class AlgorithmRepositoryImpl(
    private val database: CodeBlueprintDatabase,
    private val algorithmMapper: AlgorithmMapper,
    private val progressMapper: AlgorithmLearningProgressMapper
) : AlgorithmRepository {

    private val queries = database.codeBlueprintQueries

    override fun getAllAlgorithms(): Flow<List<Algorithm>> {
        return queries.getAllAlgorithms()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                algorithmMapper.toDomainList(entities)
            }
    }

    override fun getAlgorithmsByCategory(category: AlgorithmCategory): Flow<List<Algorithm>> {
        return queries.getAlgorithmsByCategory(category.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                algorithmMapper.toDomainList(entities)
            }
    }

    override suspend fun getAlgorithmById(id: String): Algorithm? {
        return queries.getAlgorithmById(id)
            .executeAsOneOrNull()
            ?.let { algorithmMapper.toDomain(it) }
    }

    override fun searchAlgorithms(query: String): Flow<List<Algorithm>> {
        return queries.searchAlgorithms(query, query, query, query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                algorithmMapper.toDomainList(entities)
            }
    }

    override suspend fun getRelatedAlgorithms(ids: List<String>): List<Algorithm> {
        return queries.getAlgorithmsByIds(ids)
            .executeAsList()
            .map { algorithmMapper.toDomain(it) }
    }

    override fun getLearningProgress(algorithmId: String): Flow<LearningProgress?> {
        return queries.getAlgorithmProgress(algorithmId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                entity?.let { progressMapper.toDomain(it) }
            }
    }

    override fun getAllAlgorithmLearningProgress(): Flow<List<LearningProgress>> {
        return queries.getAllAlgorithmProgress()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                progressMapper.toDomainList(entities)
            }
    }

    override suspend fun saveLearningProgress(progress: LearningProgress) {
        val values = progressMapper.toEntityValues(progress)
        queries.upsertAlgorithmProgress(
            algorithm_id = values.algorithmId,
            is_completed = values.isCompleted,
            last_viewed_at = values.lastViewedAt,
            notes = values.notes,
            is_bookmarked = values.isBookmarked
        )
    }

    override fun getBookmarkedAlgorithms(): Flow<List<Algorithm>> {
        return queries.getBookmarkedAlgorithmIds()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .combine(getAllAlgorithms()) { bookmarkedIds, allAlgorithms ->
                allAlgorithms.filter { it.id in bookmarkedIds }
            }
    }

    override suspend fun toggleBookmark(algorithmId: String) {
        val existing = queries.getAlgorithmProgress(algorithmId).executeAsOneOrNull()
        val currentTime = Clock.System.now().toEpochMilliseconds()

        if (existing != null) {
            queries.upsertAlgorithmProgress(
                algorithm_id = algorithmId,
                is_completed = existing.is_completed,
                last_viewed_at = currentTime,
                notes = existing.notes,
                is_bookmarked = if (existing.is_bookmarked == 1L) 0L else 1L
            )
        } else {
            queries.upsertAlgorithmProgress(
                algorithm_id = algorithmId,
                is_completed = 0L,
                last_viewed_at = currentTime,
                notes = null,
                is_bookmarked = 1L
            )
        }
    }

    override suspend fun toggleComplete(algorithmId: String) {
        val existing = queries.getAlgorithmProgress(algorithmId).executeAsOneOrNull()
        val currentTime = Clock.System.now().toEpochMilliseconds()

        if (existing != null) {
            queries.upsertAlgorithmProgress(
                algorithm_id = algorithmId,
                is_completed = if (existing.is_completed == 1L) 0L else 1L,
                last_viewed_at = currentTime,
                notes = existing.notes,
                is_bookmarked = existing.is_bookmarked
            )
        } else {
            queries.upsertAlgorithmProgress(
                algorithm_id = algorithmId,
                is_completed = 1L,
                last_viewed_at = currentTime,
                notes = null,
                is_bookmarked = 0L
            )
        }
    }

    override fun getOverallProgress(): Flow<Float> {
        return queries.getCompletedAlgorithmCount()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .combine(
                queries.getAlgorithmCount()
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.Default)
            ) { completedCount, totalCount ->
                val completed = completedCount ?: 0L
                val total = totalCount ?: 0L
                if (total == 0L) 0f else completed.toFloat() / total.toFloat()
            }
    }

    override suspend fun getTotalCount(): Int {
        return queries.getAlgorithmCount().executeAsOne().toInt()
    }

    override fun getCompletedCount(): Flow<Int> {
        return queries.getCompletedAlgorithmCount()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { (it ?: 0L).toInt() }
    }
}
