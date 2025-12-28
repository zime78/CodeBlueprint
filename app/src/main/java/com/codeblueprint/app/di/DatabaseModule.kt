package com.codeblueprint.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codeblueprint.data.local.db.AppDatabase
import com.codeblueprint.data.local.db.LearningProgressDao
import com.codeblueprint.data.local.db.PatternDao
import com.codeblueprint.data.local.datasource.PatternDataInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

/**
 * 데이터베이스 의존성 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Room 데이터베이스 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        patternDataInitializer: Provider<PatternDataInitializer>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // 최초 생성 시 초기 데이터 삽입
                    CoroutineScope(Dispatchers.IO).launch {
                        patternDataInitializer.get().initializePatterns()
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * PatternDao 제공
     */
    @Provides
    fun providePatternDao(database: AppDatabase): PatternDao {
        return database.patternDao()
    }

    /**
     * LearningProgressDao 제공
     */
    @Provides
    fun provideLearningProgressDao(database: AppDatabase): LearningProgressDao {
        return database.learningProgressDao()
    }
}
