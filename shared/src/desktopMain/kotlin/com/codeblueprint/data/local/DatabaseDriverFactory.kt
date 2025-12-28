package com.codeblueprint.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.codeblueprint.db.CodeBlueprintDatabase
import java.io.File
import java.sql.DriverManager

/**
 * Desktop(JVM)용 SQLDelight 드라이버 팩토리
 */
actual class DatabaseDriverFactory {
    /**
     * JDBC SQLite 드라이버 생성
     * 스키마 버전을 확인하고 필요시 마이그레이션 수행
     */
    actual fun createDriver(): SqlDriver {
        val databasePath = getDatabasePath()
        val databaseFile = File(databasePath)
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")

        if (!databaseFile.exists()) {
            // 새 데이터베이스 생성
            CodeBlueprintDatabase.Schema.create(driver)
            setSchemaVersion(databasePath, CodeBlueprintDatabase.Schema.version)
        } else {
            // 기존 데이터베이스 스키마 마이그레이션
            migrateIfNeeded(driver, databasePath)
        }

        return driver
    }

    /**
     * 스키마 버전을 확인하고 필요시 마이그레이션 수행
     */
    private fun migrateIfNeeded(driver: SqlDriver, databasePath: String) {
        val currentVersion = getSchemaVersion(databasePath)
        val latestVersion = CodeBlueprintDatabase.Schema.version

        if (currentVersion < latestVersion) {
            CodeBlueprintDatabase.Schema.migrate(driver, currentVersion, latestVersion)
            setSchemaVersion(databasePath, latestVersion)
        }
    }

    /**
     * JDBC를 사용하여 스키마 버전 조회
     */
    private fun getSchemaVersion(databasePath: String): Long {
        return try {
            DriverManager.getConnection("jdbc:sqlite:$databasePath").use { connection ->
                connection.createStatement().use { statement ->
                    val resultSet = statement.executeQuery("PRAGMA user_version;")
                    if (resultSet.next()) {
                        resultSet.getLong(1)
                    } else {
                        0L
                    }
                }
            }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * JDBC를 사용하여 스키마 버전 설정
     */
    private fun setSchemaVersion(databasePath: String, version: Long) {
        try {
            DriverManager.getConnection("jdbc:sqlite:$databasePath").use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute("PRAGMA user_version = $version;")
                }
            }
        } catch (e: Exception) {
            // 버전 설정 실패 시 무시
        }
    }

    /**
     * 데이터베이스 파일 경로 반환
     */
    private fun getDatabasePath(): String {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".codeblueprint")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return File(appDir, "codeblueprint.db").absolutePath
    }
}
