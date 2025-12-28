package com.codeblueprint.data.local

import com.codeblueprint.data.local.algorithm.BacktrackingAlgorithms
import com.codeblueprint.data.local.algorithm.DivideConquerAlgorithms
import com.codeblueprint.data.local.algorithm.DynamicProgrammingAlgorithms
import com.codeblueprint.data.local.algorithm.GraphAlgorithms
import com.codeblueprint.data.local.algorithm.GreedyAlgorithms
import com.codeblueprint.data.local.algorithm.MathAlgorithms
import com.codeblueprint.data.local.algorithm.SearchingAlgorithms
import com.codeblueprint.data.local.algorithm.SortingAlgorithms
import com.codeblueprint.data.local.algorithm.StringAlgorithms
import com.codeblueprint.data.mapper.AlgorithmMapper
import com.codeblueprint.data.mapper.CodeExampleMapper
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.model.Algorithm

/**
 * 알고리즘 초기 데이터 생성기
 *
 * 데이터베이스가 최초 생성될 때 50개의 핵심 알고리즘 데이터를 삽입합니다.
 * 각 카테고리별 알고리즘은 별도 파일로 분리되어 있습니다.
 *
 * @see SortingAlgorithms 정렬 알고리즘 (13개)
 * @see SearchingAlgorithms 탐색 알고리즘 (8개)
 * @see GraphAlgorithms 그래프 알고리즘 (12개)
 * @see DynamicProgrammingAlgorithms 동적 프로그래밍 (8개)
 * @see DivideConquerAlgorithms 분할 정복 (3개)
 * @see StringAlgorithms 문자열 알고리즘 (2개)
 * @see MathAlgorithms 수학 알고리즘 (2개)
 * @see BacktrackingAlgorithms 백트래킹 (1개)
 * @see GreedyAlgorithms 탐욕 알고리즘 (1개)
 */
class AlgorithmDataProvider(
    private val database: CodeBlueprintDatabase,
    private val algorithmMapper: AlgorithmMapper,
    private val codeExampleMapper: CodeExampleMapper = CodeExampleMapper()
) {
    private val queries = database.codeBlueprintQueries

    /**
     * 초기 알고리즘 데이터 삽입
     */
    suspend fun initializeIfNeeded() {
        val count = queries.getAlgorithmCount().executeAsOne()
        if (count == 0L) {
            insertInitialAlgorithms()
        }
    }

    private fun insertInitialAlgorithms() {
        val algorithms = createInitialAlgorithms()
        algorithms.forEach { algorithm ->
            // 알고리즘 삽입 (기존 로직 유지)
            val values = algorithmMapper.toEntityValues(algorithm)
            queries.insertAlgorithm(
                id = values.id,
                name = values.name,
                korean_name = values.koreanName,
                category = values.category,
                purpose = values.purpose,
                time_complexity_best = values.timeComplexityBest,
                time_complexity_average = values.timeComplexityAverage,
                time_complexity_worst = values.timeComplexityWorst,
                space_complexity = values.spaceComplexity,
                characteristics = values.characteristics,
                advantages = values.advantages,
                disadvantages = values.disadvantages,
                use_cases = values.useCases,
                code_examples = values.codeExamples,
                related_algorithm_ids = values.relatedAlgorithmIds,
                difficulty = values.difficulty,
                frequency = values.frequency
            )

            // 코드 예제를 새 테이블에 삽입
            algorithm.codeExamples.forEachIndexed { index, codeExample ->
                val exampleValues = codeExampleMapper.toEntityValues(
                    domain = codeExample,
                    patternId = null,
                    algorithmId = algorithm.id,
                    index = index
                )
                queries.insertCodeExample(
                    id = exampleValues.id,
                    pattern_id = exampleValues.patternId,
                    algorithm_id = exampleValues.algorithmId,
                    language = exampleValues.language,
                    code = exampleValues.code,
                    explanation = exampleValues.explanation,
                    sample_input = exampleValues.sampleInput,
                    expected_output = exampleValues.expectedOutput,
                    display_order = exampleValues.displayOrder,
                    created_at = exampleValues.createdAt,
                    updated_at = exampleValues.updatedAt
                )
            }
        }
    }

    /**
     * 모든 카테고리의 알고리즘을 조합하여 반환
     */
    private fun createInitialAlgorithms(): List<Algorithm> {
        return SortingAlgorithms.getAll() +
                SearchingAlgorithms.getAll() +
                GraphAlgorithms.getAll() +
                DynamicProgrammingAlgorithms.getAll() +
                DivideConquerAlgorithms.getAll() +
                StringAlgorithms.getAll() +
                MathAlgorithms.getAll() +
                BacktrackingAlgorithms.getAll() +
                GreedyAlgorithms.getAll()
    }
}
