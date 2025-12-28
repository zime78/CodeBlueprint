package com.codeblueprint.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codeblueprint.data.local.entity.LearningProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * 학습 진도 데이터 접근 객체 (DAO)
 */
@Dao
interface LearningProgressDao {

    /**
     * 특정 패턴의 학습 진도 조회
     */
    @Query("SELECT * FROM learning_progress WHERE patternId = :patternId")
    fun getProgress(patternId: String): Flow<LearningProgressEntity?>

    /**
     * 모든 학습 진도 조회
     */
    @Query("SELECT * FROM learning_progress")
    fun getAllProgress(): Flow<List<LearningProgressEntity>>

    /**
     * 북마크된 패턴 ID 목록 조회
     */
    @Query("SELECT patternId FROM learning_progress WHERE isBookmarked = 1")
    fun getBookmarkedPatternIds(): Flow<List<String>>

    /**
     * 학습 완료된 패턴 수 조회
     */
    @Query("SELECT COUNT(*) FROM learning_progress WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    /**
     * 학습 진도 저장/업데이트
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: LearningProgressEntity)

    /**
     * 특정 패턴의 학습 진도 조회 (suspend)
     */
    @Query("SELECT * FROM learning_progress WHERE patternId = :patternId")
    suspend fun getProgressSync(patternId: String): LearningProgressEntity?

    /**
     * 마지막 조회 시간 업데이트
     */
    @Query("UPDATE learning_progress SET lastViewedAt = :timestamp WHERE patternId = :patternId")
    suspend fun updateLastViewed(patternId: String, timestamp: Long)
}
