package com.codeblueprint.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codeblueprint.data.local.db.Converters

/**
 * Room 데이터베이스의 패턴 Entity
 */
@Entity(tableName = "patterns")
@TypeConverters(Converters::class)
data class PatternEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val koreanName: String,
    val category: String,           // CREATIONAL, STRUCTURAL, BEHAVIORAL
    val purpose: String,
    val characteristics: List<String>,
    val advantages: List<String>,
    val disadvantages: List<String>,
    val useCases: List<String>,
    val codeExamples: String,       // JSON 문자열로 저장
    val diagram: String,
    val relatedPatternIds: List<String>,
    val difficulty: String,         // LOW, MEDIUM, HIGH
    val frequency: Int
)
