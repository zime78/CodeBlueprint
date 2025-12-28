package com.codeblueprint.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codeblueprint.data.local.entity.PatternEntity
import kotlinx.coroutines.flow.Flow

/**
 * 패턴 데이터 접근 객체 (DAO)
 */
@Dao
interface PatternDao {

    /**
     * 모든 패턴 조회
     */
    @Query("SELECT * FROM patterns ORDER BY name ASC")
    fun getAllPatterns(): Flow<List<PatternEntity>>

    /**
     * 카테고리별 패턴 조회
     */
    @Query("SELECT * FROM patterns WHERE category = :category ORDER BY name ASC")
    fun getPatternsByCategory(category: String): Flow<List<PatternEntity>>

    /**
     * ID로 단일 패턴 조회
     */
    @Query("SELECT * FROM patterns WHERE id = :patternId")
    suspend fun getPatternById(patternId: String): PatternEntity?

    /**
     * 패턴 검색 (이름, 한글명, 목적에서 검색)
     */
    @Query("""
        SELECT * FROM patterns
        WHERE name LIKE '%' || :query || '%'
           OR koreanName LIKE '%' || :query || '%'
           OR purpose LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun searchPatterns(query: String): Flow<List<PatternEntity>>

    /**
     * ID 목록으로 패턴 조회
     */
    @Query("SELECT * FROM patterns WHERE id IN (:patternIds)")
    suspend fun getPatternsByIds(patternIds: List<String>): List<PatternEntity>

    /**
     * 패턴 삽입 (충돌 시 교체)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatterns(patterns: List<PatternEntity>)

    /**
     * 전체 패턴 수 조회
     */
    @Query("SELECT COUNT(*) FROM patterns")
    suspend fun getPatternCount(): Int
}
