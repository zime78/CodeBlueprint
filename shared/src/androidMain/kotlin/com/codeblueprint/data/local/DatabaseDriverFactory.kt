package com.codeblueprint.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.codeblueprint.db.CodeBlueprintDatabase
import java.io.File
import java.io.FileOutputStream

/**
 * Android용 SQLDelight 드라이버 팩토리
 */
actual class DatabaseDriverFactory(
    private val context: Context
) {
    private val databaseName = "codeblueprint.db"
    private val databasePath: File by lazy { context.getDatabasePath(databaseName) }

    /**
     * Android SQLite 드라이버 생성
     */
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = CodeBlueprintDatabase.Schema,
            context = context,
            name = databaseName
        )
    }

    /**
     * Seed DB에서 초기화 (필요시)
     *
     * 첫 실행 시 assets의 Seed DB를 앱 데이터 폴더로 복사하고,
     * 기존 DB가 있으면 버전 체크 후 마이그레이션을 수행합니다.
     *
     * @return true: 초기화/마이그레이션 수행됨, false: 기존 DB 사용
     */
    actual suspend fun initializeFromSeedIfNeeded(): Boolean {
        if (!databasePath.exists()) {
            // 첫 실행: Seed DB 복사
            return copySeedDatabase()
        }

        // 기존 DB 존재: 데이터 버전 체크 후 마이그레이션
        return checkAndMigrateDataIfNeeded()
    }

    /**
     * assets에서 Seed DB를 앱 데이터 폴더로 복사
     */
    private fun copySeedDatabase(): Boolean {
        return try {
            context.assets.open("databases/codeblueprint_seed.db").use { input ->
                databasePath.parentFile?.mkdirs()
                FileOutputStream(databasePath).use { output ->
                    input.copyTo(output)
                }
            }
            println("Seed database copied to: ${databasePath.absolutePath}")
            true
        } catch (e: Exception) {
            // Seed DB가 없으면 런타임 초기화로 fallback
            println("Seed database not found in assets, will initialize at runtime: ${e.message}")
            false
        }
    }

    /**
     * 데이터 버전 체크 후 마이그레이션 수행
     */
    private fun checkAndMigrateDataIfNeeded(): Boolean {
        val currentDataVersion = getMetadataFromDb("data_version")
        val bundledDataVersion = getBundledDataVersion()

        if (bundledDataVersion == null) {
            return false
        }

        if (currentDataVersion == null || needsDataMigration(currentDataVersion, bundledDataVersion)) {
            migrateData(currentDataVersion, bundledDataVersion)
            return true
        }

        return false
    }

    /**
     * assets의 Seed DB에서 데이터 버전 조회
     */
    private fun getBundledDataVersion(): String? {
        return try {
            // 임시 파일로 복사
            val tempFile = File.createTempFile("seed_check", ".db", context.cacheDir)
            try {
                context.assets.open("databases/codeblueprint_seed.db").use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }
                getMetadataFromFile(tempFile.absolutePath, "data_version")
            } finally {
                tempFile.delete()
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * DB 파일에서 메타데이터 조회
     */
    private fun getMetadataFromFile(dbPath: String, key: String): String? {
        return try {
            val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
            db.use {
                val cursor = it.rawQuery(
                    "SELECT value FROM AppMetadata WHERE key = ?",
                    arrayOf(key)
                )
                cursor.use { c ->
                    if (c.moveToFirst()) {
                        c.getString(0)
                    } else {
                        null
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 현재 DB에서 메타데이터 조회
     */
    private fun getMetadataFromDb(key: String): String? {
        if (!databasePath.exists()) return null
        return getMetadataFromFile(databasePath.absolutePath, key)
    }

    /**
     * 데이터 마이그레이션 필요 여부 확인
     */
    private fun needsDataMigration(currentVersion: String, bundledVersion: String): Boolean {
        return try {
            val current = parseVersion(currentVersion)
            val bundled = parseVersion(bundledVersion)
            bundled > current
        } catch (e: Exception) {
            true
        }
    }

    /**
     * 버전 문자열을 비교 가능한 숫자로 변환
     */
    private fun parseVersion(version: String): Int {
        val parts = version.split(".").map { it.toIntOrNull() ?: 0 }
        return parts.getOrElse(0) { 0 } * 10000 +
                parts.getOrElse(1) { 0 } * 100 +
                parts.getOrElse(2) { 0 }
    }

    /**
     * 데이터 마이그레이션 수행
     */
    private fun migrateData(fromVersion: String?, toVersion: String) {
        println("Migrating data from $fromVersion to $toVersion...")

        val driver = createDriver()
        val database = CodeBlueprintDatabase(driver)
        val queries = database.codeBlueprintQueries

        try {
            // 1. 사용자 데이터 백업
            val patternProgress = queries.getAllProgress().executeAsList()
            val algorithmProgress = queries.getAllAlgorithmProgress().executeAsList()

            // 2. Seed DB에서 정적 데이터 복사
            updateStaticDataFromSeed()

            // 3. 사용자 데이터 복원
            patternProgress.forEach { progress ->
                queries.upsertProgress(
                    pattern_id = progress.pattern_id,
                    is_completed = progress.is_completed,
                    last_viewed_at = progress.last_viewed_at,
                    notes = progress.notes,
                    is_bookmarked = progress.is_bookmarked
                )
            }

            algorithmProgress.forEach { progress ->
                queries.upsertAlgorithmProgress(
                    algorithm_id = progress.algorithm_id,
                    is_completed = progress.is_completed,
                    last_viewed_at = progress.last_viewed_at,
                    notes = progress.notes,
                    is_bookmarked = progress.is_bookmarked
                )
            }

            // 4. 버전 업데이트
            queries.upsertMetadata("data_version", toVersion)
            queries.upsertMetadata("migrated_at", System.currentTimeMillis().toString())

            println("Data migration completed successfully")
        } finally {
            driver.close()
        }
    }

    /**
     * Seed DB에서 정적 데이터 복사
     */
    private fun updateStaticDataFromSeed() {
        try {
            // 임시 파일로 Seed DB 복사
            val tempSeedFile = File.createTempFile("seed_migration", ".db", context.cacheDir)
            try {
                context.assets.open("databases/codeblueprint_seed.db").use { input ->
                    FileOutputStream(tempSeedFile).use { output ->
                        input.copyTo(output)
                    }
                }

                // ATTACH DATABASE로 Seed DB 연결 후 데이터 복사
                val db = SQLiteDatabase.openDatabase(
                    databasePath.absolutePath,
                    null,
                    SQLiteDatabase.OPEN_READWRITE
                )
                db.use {
                    it.execSQL("ATTACH DATABASE '${tempSeedFile.absolutePath}' AS seed")

                    it.execSQL("DELETE FROM PatternEntity")
                    it.execSQL("INSERT INTO PatternEntity SELECT * FROM seed.PatternEntity")

                    it.execSQL("DELETE FROM AlgorithmEntity")
                    it.execSQL("INSERT INTO AlgorithmEntity SELECT * FROM seed.AlgorithmEntity")

                    it.execSQL("DELETE FROM CodeExampleEntity")
                    it.execSQL("INSERT INTO CodeExampleEntity SELECT * FROM seed.CodeExampleEntity")

                    it.execSQL("DETACH DATABASE seed")
                }
            } finally {
                tempSeedFile.delete()
            }
        } catch (e: Exception) {
            println("Failed to update static data from seed: ${e.message}")
        }
    }
}
