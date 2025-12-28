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

    private val databasePath: String by lazy { resolveDatabasePath() }
    private val databaseFile: File by lazy { File(databasePath) }

    /**
     * JDBC SQLite 드라이버 생성
     * 스키마 버전을 확인하고 필요시 마이그레이션 수행
     */
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")

        if (!databaseFile.exists()) {
            // 새 데이터베이스 생성 (Seed DB가 없는 경우 fallback)
            CodeBlueprintDatabase.Schema.create(driver)
            setSchemaVersion(databasePath, CodeBlueprintDatabase.Schema.version)
        } else {
            // 기존 데이터베이스 스키마 마이그레이션
            migrateSchemaIfNeeded(driver)
        }

        return driver
    }

    /**
     * Seed DB에서 초기화 (필요시)
     *
     * 첫 실행 시 Seed DB를 사용자 폴더로 복사하고,
     * 기존 DB가 있으면 버전 체크 후 데이터 마이그레이션을 수행합니다.
     *
     * @return true: 초기화/마이그레이션 수행됨, false: 기존 DB 사용
     */
    actual suspend fun initializeFromSeedIfNeeded(): Boolean {
        ensureDirectoryExists()

        if (!databaseFile.exists()) {
            // 첫 실행: Seed DB 복사
            return copySeedDatabase()
        }

        // 기존 DB 존재: 데이터 버전 체크 후 마이그레이션
        return checkAndMigrateDataIfNeeded()
    }

    /**
     * Seed DB를 사용자 폴더로 복사
     */
    private fun copySeedDatabase(): Boolean {
        val seedStream = javaClass.classLoader.getResourceAsStream("codeblueprint_seed.db")

        if (seedStream != null) {
            seedStream.use { input ->
                databaseFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            println("Seed database copied to: $databasePath")
            return true
        } else {
            // Seed DB가 없으면 런타임 초기화로 fallback
            println("Seed database not found, will initialize at runtime")
            return false
        }
    }

    /**
     * 데이터 버전 체크 후 마이그레이션 수행
     */
    private fun checkAndMigrateDataIfNeeded(): Boolean {
        val currentDataVersion = getMetadataFromDb("data_version")
        val bundledDataVersion = getBundledDataVersion()

        if (bundledDataVersion == null) {
            // Seed DB가 없으면 마이그레이션 불필요
            return false
        }

        if (currentDataVersion == null || needsDataMigration(currentDataVersion, bundledDataVersion)) {
            migrateData(currentDataVersion, bundledDataVersion)
            return true
        }

        return false
    }

    /**
     * 번들된 Seed DB의 데이터 버전 조회
     */
    private fun getBundledDataVersion(): String? {
        val seedStream = javaClass.classLoader.getResourceAsStream("codeblueprint_seed.db")
            ?: return null

        // 임시 파일로 복사해서 버전 조회
        val tempFile = File.createTempFile("seed_check", ".db")
        try {
            seedStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            return getMetadataFromFile(tempFile.absolutePath, "data_version")
        } finally {
            tempFile.delete()
        }
    }

    /**
     * DB 파일에서 메타데이터 조회
     */
    private fun getMetadataFromFile(dbPath: String, key: String): String? {
        return try {
            DriverManager.getConnection("jdbc:sqlite:$dbPath").use { connection ->
                connection.createStatement().use { statement ->
                    val resultSet = statement.executeQuery(
                        "SELECT value FROM AppMetadata WHERE key = '$key'"
                    )
                    if (resultSet.next()) {
                        resultSet.getString(1)
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
        if (!databaseFile.exists()) return null
        return getMetadataFromFile(databasePath, key)
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
            // 버전 파싱 실패 시 마이그레이션 필요
            true
        }
    }

    /**
     * 버전 문자열을 비교 가능한 숫자로 변환 (예: "1.2.3" -> 10203)
     */
    private fun parseVersion(version: String): Int {
        val parts = version.split(".").map { it.toIntOrNull() ?: 0 }
        return parts.getOrElse(0) { 0 } * 10000 +
                parts.getOrElse(1) { 0 } * 100 +
                parts.getOrElse(2) { 0 }
    }

    /**
     * 데이터 마이그레이션 수행
     *
     * 사용자 데이터(학습 진도, 북마크)를 보존하면서 정적 데이터를 업데이트합니다.
     */
    private fun migrateData(fromVersion: String?, toVersion: String) {
        println("Migrating data from $fromVersion to $toVersion...")

        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")
        val database = CodeBlueprintDatabase(driver)
        val queries = database.codeBlueprintQueries

        try {
            // 1. 사용자 데이터 백업 (메모리)
            val patternProgress = queries.getAllProgress().executeAsList()
            val algorithmProgress = queries.getAllAlgorithmProgress().executeAsList()
            val settings = queries.getSettings().executeAsOneOrNull()

            // 2. Seed DB에서 정적 데이터 복사 (패턴, 알고리즘, 코드예제)
            updateStaticDataFromSeed(driver)

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
    private fun updateStaticDataFromSeed(targetDriver: SqlDriver) {
        val seedStream = javaClass.classLoader.getResourceAsStream("codeblueprint_seed.db")
            ?: return

        // 임시 파일로 Seed DB 복사
        val tempSeedFile = File.createTempFile("seed_migration", ".db")
        try {
            seedStream.use { input ->
                tempSeedFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // ATTACH DATABASE로 Seed DB 연결 후 데이터 복사
            DriverManager.getConnection("jdbc:sqlite:$databasePath").use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute("ATTACH DATABASE '${tempSeedFile.absolutePath}' AS seed")

                    // 패턴 데이터 교체
                    statement.execute("DELETE FROM PatternEntity")
                    statement.execute("INSERT INTO PatternEntity SELECT * FROM seed.PatternEntity")

                    // 알고리즘 데이터 교체
                    statement.execute("DELETE FROM AlgorithmEntity")
                    statement.execute("INSERT INTO AlgorithmEntity SELECT * FROM seed.AlgorithmEntity")

                    // 코드 예제 데이터 교체
                    statement.execute("DELETE FROM CodeExampleEntity")
                    statement.execute("INSERT INTO CodeExampleEntity SELECT * FROM seed.CodeExampleEntity")

                    statement.execute("DETACH DATABASE seed")
                }
            }
        } finally {
            tempSeedFile.delete()
        }
    }

    /**
     * 스키마 버전을 확인하고 필요시 마이그레이션 수행
     */
    private fun migrateSchemaIfNeeded(driver: SqlDriver) {
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
    private fun getSchemaVersion(dbPath: String): Long {
        return try {
            DriverManager.getConnection("jdbc:sqlite:$dbPath").use { connection ->
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
    private fun setSchemaVersion(dbPath: String, version: Long) {
        try {
            DriverManager.getConnection("jdbc:sqlite:$dbPath").use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute("PRAGMA user_version = $version;")
                }
            }
        } catch (e: Exception) {
            // 버전 설정 실패 시 무시
        }
    }

    /**
     * 앱 데이터 디렉토리 생성
     */
    private fun ensureDirectoryExists() {
        val appDir = databaseFile.parentFile
        if (appDir != null && !appDir.exists()) {
            appDir.mkdirs()
        }
    }

    /**
     * 데이터베이스 파일 경로 반환
     */
    private fun resolveDatabasePath(): String {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".codeblueprint")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return File(appDir, "codeblueprint.db").absolutePath
    }
}
