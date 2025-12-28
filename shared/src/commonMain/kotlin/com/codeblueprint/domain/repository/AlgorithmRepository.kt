package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.Algorithm
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.LearningProgress
import kotlinx.coroutines.flow.Flow

/**
 * 알고리즘 데이터 접근 추상화
 *
 * 알고리즘 조회, 검색, 학습 진도 관리 기능 제공
 */
interface AlgorithmRepository {

    // ===== 알고리즘 조회 =====

    /**
     * 전체 알고리즘 목록 조회
     */
    fun getAllAlgorithms(): Flow<List<Algorithm>>

    /**
     * 카테고리별 알고리즘 목록 조회
     */
    fun getAlgorithmsByCategory(category: AlgorithmCategory): Flow<List<Algorithm>>

    /**
     * ID로 알고리즘 조회
     */
    suspend fun getAlgorithmById(id: String): Algorithm?

    /**
     * 키워드로 알고리즘 검색
     * 이름, 한글명, 목적, 활용 예시에서 검색
     */
    fun searchAlgorithms(query: String): Flow<List<Algorithm>>

    /**
     * 관련 알고리즘 목록 조회
     */
    suspend fun getRelatedAlgorithms(ids: List<String>): List<Algorithm>

    // ===== 학습 진도 관리 =====

    /**
     * 특정 알고리즘의 학습 진도 조회
     */
    fun getLearningProgress(algorithmId: String): Flow<LearningProgress?>

    /**
     * 전체 알고리즘 학습 진도 목록 조회
     */
    fun getAllAlgorithmLearningProgress(): Flow<List<LearningProgress>>

    /**
     * 학습 진도 저장
     */
    suspend fun saveLearningProgress(progress: LearningProgress)

    // ===== 북마크 관리 =====

    /**
     * 북마크된 알고리즘 목록 조회
     */
    fun getBookmarkedAlgorithms(): Flow<List<Algorithm>>

    /**
     * 북마크 토글
     */
    suspend fun toggleBookmark(algorithmId: String)

    /**
     * 학습 완료 토글
     */
    suspend fun toggleComplete(algorithmId: String)

    // ===== 통계 =====

    /**
     * 전체 학습 진도율 (0.0 ~ 1.0)
     */
    fun getOverallProgress(): Flow<Float>

    /**
     * 전체 알고리즘 개수
     */
    suspend fun getTotalCount(): Int

    /**
     * 완료된 알고리즘 개수
     */
    fun getCompletedCount(): Flow<Int>
}
