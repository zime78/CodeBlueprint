package com.codeblueprint.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.codeblueprint.db.CodeBlueprintDatabase

/**
 * iOS용 SQLDelight 드라이버 팩토리
 */
actual class DatabaseDriverFactory {
    /**
     * Native SQLite 드라이버 생성
     */
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = CodeBlueprintDatabase.Schema,
            name = "codeblueprint.db"
        )
    }
}
