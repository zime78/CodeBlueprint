package com.codeblueprint.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room 데이터베이스 타입 변환기
 *
 * 복잡한 타입을 저장 가능한 형태로 변환합니다.
 */
class Converters {

    private val gson = Gson()

    /**
     * List<String> -> JSON String
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    /**
     * JSON String -> List<String>
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return try {
            gson.fromJson(value, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
