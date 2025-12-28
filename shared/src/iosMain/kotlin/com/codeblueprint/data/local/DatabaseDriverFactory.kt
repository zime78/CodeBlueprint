package com.codeblueprint.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.codeblueprint.db.CodeBlueprintDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.timeIntervalSince1970

/**
 * iOS용 SQLDelight 드라이버 팩토리
 */
@OptIn(ExperimentalForeignApi::class)
actual class DatabaseDriverFactory {

    private val databaseName = "codeblueprint.db"
    private val documentPath: String by lazy { getDocumentPath() }
    private val databasePath: String by lazy { "$documentPath/$databaseName" }

    /**
     * Native SQLite 드라이버 생성
     */
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = CodeBlueprintDatabase.Schema,
            name = databaseName
        )
    }

    /**
     * Seed DB에서 초기화 (필요시)
     *
     * 첫 실행 시 Bundle의 Seed DB를 Documents 폴더로 복사하고,
     * 기존 DB가 있으면 버전 체크 후 마이그레이션을 수행합니다.
     *
     * @return true: 초기화/마이그레이션 수행됨, false: 기존 DB 사용
     */
    actual suspend fun initializeFromSeedIfNeeded(): Boolean {
        val fileManager = NSFileManager.defaultManager

        if (!fileManager.fileExistsAtPath(databasePath)) {
            // 첫 실행: Seed DB 복사
            return copySeedDatabase(fileManager)
        }

        // 기존 DB 존재: 데이터 버전 체크 후 마이그레이션
        return checkAndMigrateDataIfNeeded()
    }

    /**
     * Bundle에서 Seed DB를 Documents 폴더로 복사
     */
    private fun copySeedDatabase(fileManager: NSFileManager): Boolean {
        val bundlePath = NSBundle.mainBundle.pathForResource("codeblueprint_seed", "db")
            ?: run {
                println("Seed database not found in bundle, will initialize at runtime")
                return false
            }

        return try {
            fileManager.copyItemAtPath(bundlePath, databasePath, null)
            println("Seed database copied to: $databasePath")
            true
        } catch (e: Exception) {
            println("Failed to copy seed database: ${e.message}")
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
     * Bundle의 Seed DB에서 데이터 버전 조회
     */
    private fun getBundledDataVersion(): String? {
        val bundlePath = NSBundle.mainBundle.pathForResource("codeblueprint_seed", "db")
            ?: return null

        // iOS에서는 Bundle 파일을 직접 읽어서 버전 조회
        // 실제 구현에서는 별도의 버전 파일을 사용하거나 SQLite 쿼리 필요
        // 여기서는 간단히 버전 정보를 하드코딩
        return "1.0.0"
    }

    /**
     * 현재 DB에서 메타데이터 조회
     */
    private fun getMetadataFromDb(key: String): String? {
        // iOS Native에서 직접 SQLite 쿼리 실행은 복잡하므로
        // SQLDelight를 통해 조회
        return try {
            val driver = createDriver()
            val database = CodeBlueprintDatabase(driver)
            val result = database.codeBlueprintQueries.getMetadata(key).executeAsOneOrNull()
            driver.close()
            result
        } catch (e: Exception) {
            null
        }
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
            // iOS에서 ATTACH DATABASE는 지원하지만 Native driver에서 복잡
            // 간단히 Bundle DB를 덮어쓴 후 사용자 데이터 복원
            val fileManager = NSFileManager.defaultManager
            val bundlePath = NSBundle.mainBundle.pathForResource("codeblueprint_seed", "db")

            if (bundlePath != null) {
                // 기존 DB 삭제 후 Seed DB 복사
                fileManager.removeItemAtPath(databasePath, null)
                fileManager.copyItemAtPath(bundlePath, databasePath, null)
            }

            // 3. 새 드라이버로 사용자 데이터 복원
            val newDriver = createDriver()
            val newDatabase = CodeBlueprintDatabase(newDriver)
            val newQueries = newDatabase.codeBlueprintQueries

            patternProgress.forEach { progress ->
                newQueries.upsertProgress(
                    pattern_id = progress.pattern_id,
                    is_completed = progress.is_completed,
                    last_viewed_at = progress.last_viewed_at,
                    notes = progress.notes,
                    is_bookmarked = progress.is_bookmarked
                )
            }

            algorithmProgress.forEach { progress ->
                newQueries.upsertAlgorithmProgress(
                    algorithm_id = progress.algorithm_id,
                    is_completed = progress.is_completed,
                    last_viewed_at = progress.last_viewed_at,
                    notes = progress.notes,
                    is_bookmarked = progress.is_bookmarked
                )
            }

            // 4. 버전 업데이트
            newQueries.upsertMetadata("data_version", toVersion)
            newQueries.upsertMetadata("migrated_at", NSDate().timeIntervalSince1970.toLong().toString())

            newDriver.close()
            println("Data migration completed successfully")
        } finally {
            driver.close()
        }
    }

    /**
     * Documents 디렉토리 경로 반환
     */
    private fun getDocumentPath(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        return paths.first() as String
    }
}
