package com.codeblueprint.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codeblueprint.data.local.entity.LearningProgressEntity
import com.codeblueprint.data.local.entity.PatternEntity

/**
 * Room 데이터베이스 정의
 *
 * 패턴 데이터와 학습 진도를 저장합니다.
 */
@Database(
    entities = [
        PatternEntity::class,
        LearningProgressEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patternDao(): PatternDao
    abstract fun learningProgressDao(): LearningProgressDao

    companion object {
        const val DATABASE_NAME = "codeblueprint.db"
    }
}
