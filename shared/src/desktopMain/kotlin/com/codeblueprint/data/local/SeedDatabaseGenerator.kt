package com.codeblueprint.data.local

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.codeblueprint.data.mapper.AlgorithmMapper
import com.codeblueprint.data.mapper.CodeExampleMapper
import com.codeblueprint.data.mapper.PatternMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import java.io.File

/**
 * Seed 데이터베이스 생성기
 *
 * Pre-built DB 파일을 생성합니다.
 * 이 DB 파일은 앱 리소스에 포함되어 첫 실행 시 사용자 폴더로 복사됩니다.
 */
object SeedDatabaseGenerator {

    private const val DATA_VERSION = "1.0.0"

    /**
     * Seed 데이터베이스 생성
     *
     * @param outputPath 생성할 DB 파일 경로
     * @return 생성된 DB 파일
     */
    fun generate(outputPath: String): File {
        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()

        // 기존 파일 삭제
        if (outputFile.exists()) {
            outputFile.delete()
        }

        // 드라이버 생성 및 스키마 생성
        val driver = JdbcSqliteDriver("jdbc:sqlite:$outputPath")
        CodeBlueprintDatabase.Schema.create(driver)

        val database = CodeBlueprintDatabase(driver)
        val queries = database.codeBlueprintQueries

        // Mapper 생성
        val patternMapper = PatternMapper()
        val algorithmMapper = AlgorithmMapper()
        val codeExampleMapper = CodeExampleMapper()

        // PatternDataInitializer 생성 및 데이터 삽입
        val patternInitializer = PatternDataInitializer(database, patternMapper, codeExampleMapper)
        // initializeIfNeeded 대신 직접 초기 데이터 생성
        insertPatternData(database, patternMapper, codeExampleMapper)

        // AlgorithmDataProvider 생성 및 데이터 삽입
        insertAlgorithmData(database, algorithmMapper, codeExampleMapper)

        // AppMetadata 삽입
        insertMetadata(queries)

        // 드라이버 종료
        driver.close()

        println("Seed database generated: $outputPath")
        println("  - Patterns: ${getPatternCount(outputPath)}")
        println("  - Algorithms: ${getAlgorithmCount(outputPath)}")
        println("  - Data version: $DATA_VERSION")

        return outputFile
    }

    /**
     * 패턴 데이터 삽입
     */
    private fun insertPatternData(
        database: CodeBlueprintDatabase,
        patternMapper: PatternMapper,
        codeExampleMapper: CodeExampleMapper
    ) {
        val initializer = PatternDataInitializer(database, patternMapper, codeExampleMapper)
        // initializeIfNeeded는 suspend 함수이므로 직접 삽입 로직 호출
        // 여기서는 리플렉션 없이 새로 데이터 생성
        val queries = database.codeBlueprintQueries

        val patterns = createPatternDataInitializer(database, patternMapper, codeExampleMapper)
            .let { initializer ->
                // PatternDataInitializer의 createInitialPatterns를 호출해야 하지만
                // private이므로 새로 인스턴스 생성 후 initializeIfNeeded 호출
                // count가 0이므로 자동으로 데이터 삽입됨
                kotlinx.coroutines.runBlocking {
                    initializer.initializeIfNeeded()
                }
            }
    }

    private fun createPatternDataInitializer(
        database: CodeBlueprintDatabase,
        patternMapper: PatternMapper,
        codeExampleMapper: CodeExampleMapper
    ) = PatternDataInitializer(database, patternMapper, codeExampleMapper)

    /**
     * 알고리즘 데이터 삽입
     */
    private fun insertAlgorithmData(
        database: CodeBlueprintDatabase,
        algorithmMapper: AlgorithmMapper,
        codeExampleMapper: CodeExampleMapper
    ) {
        val provider = AlgorithmDataProvider(database, algorithmMapper, codeExampleMapper)
        kotlinx.coroutines.runBlocking {
            provider.initializeIfNeeded()
        }
    }

    /**
     * AppMetadata 삽입
     */
    private fun insertMetadata(queries: com.codeblueprint.db.CodeBlueprintQueries) {
        queries.upsertMetadata("data_version", DATA_VERSION)
        queries.upsertMetadata("created_at", System.currentTimeMillis().toString())
        queries.upsertMetadata("schema_version", CodeBlueprintDatabase.Schema.version.toString())
    }

    /**
     * 패턴 개수 조회 (검증용)
     */
    private fun getPatternCount(dbPath: String): Long {
        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbPath")
        val database = CodeBlueprintDatabase(driver)
        val count = database.codeBlueprintQueries.getPatternCount().executeAsOne()
        driver.close()
        return count
    }

    /**
     * 알고리즘 개수 조회 (검증용)
     */
    private fun getAlgorithmCount(dbPath: String): Long {
        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbPath")
        val database = CodeBlueprintDatabase(driver)
        val count = database.codeBlueprintQueries.getAlgorithmCount().executeAsOne()
        driver.close()
        return count
    }
}
