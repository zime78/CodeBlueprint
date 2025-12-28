package com.codeblueprint.domain.repository

import com.codeblueprint.domain.model.ArchitecturePattern
import kotlinx.coroutines.flow.Flow

/**
 * 아키텍처 패턴 Repository 인터페이스
 */
interface ArchitectureRepository {

    /**
     * 모든 아키텍처 패턴 조회
     */
    fun getAllArchitectures(): Flow<List<ArchitecturePattern>>

    /**
     * 특정 아키텍처 패턴 조회
     */
    suspend fun getArchitectureById(id: String): ArchitecturePattern?

    /**
     * Android 권장 아키텍처 조회
     */
    fun getRecommendedArchitectures(): Flow<List<ArchitecturePattern>>
}
