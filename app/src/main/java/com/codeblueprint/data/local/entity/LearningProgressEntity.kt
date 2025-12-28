package com.codeblueprint.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room 데이터베이스의 학습 진도 Entity
 */
@Entity(tableName = "learning_progress")
data class LearningProgressEntity(
    @PrimaryKey
    val patternId: String,
    val isCompleted: Boolean = false,
    val lastViewedAt: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val isBookmarked: Boolean = false
)
