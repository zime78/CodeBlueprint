package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.DesignPattern
import com.codeblueprint.domain.model.LearningProgress
import com.codeblueprint.domain.model.PatternCategory
import kotlinx.coroutines.flow.Flow

/**
 * 패턴 데이터 접근을 추상화하는 Repository 인터페이스
 *
 * Clean Architecture의 원칙에 따라 Domain Layer에서 인터페이스를 정의하고,
 * Data Layer에서 구현합니다.
 */
interface PatternRepository {

    /**
     * 모든 패턴 목록을 Flow로 반환
     */
    fun getAllPatterns(): Flow<List<DesignPattern>>

    /**
     * 카테고리별 패턴 목록 조회
     */
    fun getPatternsByCategory(category: PatternCategory): Flow<List<DesignPattern>>

    /**
     * 패턴 ID로 단일 패턴 조회
     */
    suspend fun getPatternById(patternId: String): DesignPattern?

    /**
     * 패턴 검색
     */
    fun searchPatterns(query: String): Flow<List<DesignPattern>>

    /**
     * 관련 패턴 목록 조회
     */
    suspend fun getRelatedPatterns(patternIds: List<String>): List<DesignPattern>

    /**
     * 학습 진도 조회
     */
    fun getLearningProgress(patternId: String): Flow<LearningProgress?>

    /**
     * 모든 학습 진도 조회
     */
    fun getAllLearningProgress(): Flow<List<LearningProgress>>

    /**
     * 학습 진도 저장/업데이트
     */
    suspend fun saveLearningProgress(progress: LearningProgress)

    /**
     * 북마크된 패턴 목록 조회
     */
    fun getBookmarkedPatterns(): Flow<List<DesignPattern>>

    /**
     * 북마크 토글
     */
    suspend fun toggleBookmark(patternId: String)

    /**
     * 학습 완료 토글
     */
    suspend fun toggleComplete(patternId: String)

    /**
     * 전체 학습 진도율 계산 (0.0 ~ 1.0)
     */
    fun getOverallProgress(): Flow<Float>
}
