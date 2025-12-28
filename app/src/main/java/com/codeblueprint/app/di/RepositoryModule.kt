package com.codeblueprint.app.di

import com.codeblueprint.data.repository.PatternRepositoryImpl
import com.codeblueprint.domain.repository.PatternRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository 바인딩 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * PatternRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindPatternRepository(
        impl: PatternRepositoryImpl
    ): PatternRepository
}
