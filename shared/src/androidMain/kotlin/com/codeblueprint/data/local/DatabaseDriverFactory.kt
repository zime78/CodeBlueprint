package com.codeblueprint.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.codeblueprint.db.CodeBlueprintDatabase

/**
 * Android용 SQLDelight 드라이버 팩토리
 */
actual class DatabaseDriverFactory(
    private val context: Context
) {
    /**
     * Android SQLite 드라이버 생성
     */
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = CodeBlueprintDatabase.Schema,
            context = context,
            name = "codeblueprint.db"
        )
    }
}
