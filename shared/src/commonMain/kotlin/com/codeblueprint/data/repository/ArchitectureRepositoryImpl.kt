package com.codeblueprint.data.repository

import com.codeblueprint.data.local.ArchitectureDataProvider
import com.codeblueprint.domain.model.ArchitecturePattern
import com.codeblueprint.domain.repository.ArchitectureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 아키텍처 패턴 Repository 구현
 *
 * 인메모리 데이터 사용 (정적 콘텐츠)
 */
class ArchitectureRepositoryImpl : ArchitectureRepository {

    private val architectures = ArchitectureDataProvider.getArchitectures()

    override fun getAllArchitectures(): Flow<List<ArchitecturePattern>> = flow {
        emit(architectures)
    }

    override suspend fun getArchitectureById(id: String): ArchitecturePattern? {
        return architectures.find { it.id == id }
    }

    override fun getRecommendedArchitectures(): Flow<List<ArchitecturePattern>> = flow {
        emit(architectures.filter { it.androidRecommendation })
    }
}
