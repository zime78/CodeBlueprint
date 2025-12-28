package com.codeblueprint.ui.algorithm.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.TimeComplexity
import com.codeblueprint.presentation.algorithm.detail.AlgorithmDetailEvent
import com.codeblueprint.presentation.algorithm.detail.AlgorithmDetailUiModel
import com.codeblueprint.presentation.algorithm.detail.AlgorithmDetailUiState
import com.codeblueprint.presentation.algorithm.detail.AlgorithmDetailViewModel
import com.codeblueprint.presentation.algorithm.detail.RelatedAlgorithmUiModel
import com.codeblueprint.ui.navigation.AlgorithmDetailComponent
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * 알고리즘 상세 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmDetailScreen(
    component: AlgorithmDetailComponent,
    viewModel: AlgorithmDetailViewModel = koinInject { parametersOf(component.algorithmId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val state = uiState
                    if (state is AlgorithmDetailUiState.Success) {
                        Text(
                            text = state.algorithm.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { component.onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                },
                actions = {
                    val state = uiState
                    if (state is AlgorithmDetailUiState.Success) {
                        IconButton(onClick = { viewModel.onEvent(AlgorithmDetailEvent.OnBookmarkToggle) }) {
                            Icon(
                                imageVector = if (state.algorithm.isBookmarked) {
                                    Icons.Default.Bookmark
                                } else {
                                    Icons.Default.BookmarkBorder
                                },
                                contentDescription = "북마크",
                                tint = if (state.algorithm.isBookmarked) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(AlgorithmDetailEvent.OnShareClick) }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "공유"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is AlgorithmDetailUiState.Loading -> {
                    LoadingContent()
                }

                is AlgorithmDetailUiState.Success -> {
                    DetailContent(
                        algorithm = state.algorithm,
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { language ->
                            viewModel.onEvent(AlgorithmDetailEvent.OnLanguageChange(language))
                        },
                        onRelatedAlgorithmClick = { component.onRelatedAlgorithmClick(it) },
                        onCodePlaygroundClick = { component.onCodePlaygroundClick() }
                    )
                }

                is AlgorithmDetailUiState.Error -> {
                    ErrorContent(message = state.message)
                }
            }
        }
    }
}

/**
 * 로딩 화면
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * 에러 화면
 */
@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * 상세 컨텐츠
 */
@Composable
private fun DetailContent(
    algorithm: AlgorithmDetailUiModel,
    selectedLanguage: ProgrammingLanguage,
    onLanguageChange: (ProgrammingLanguage) -> Unit,
    onRelatedAlgorithmClick: (String) -> Unit,
    onCodePlaygroundClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 헤더 정보
        item {
            AlgorithmHeader(algorithm = algorithm)
        }

        // 목적 섹션
        item {
            SectionCard(
                title = "목적",
                icon = "🎯"
            ) {
                Text(
                    text = algorithm.purpose,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // 복잡도 섹션
        item {
            ComplexitySection(
                timeComplexity = algorithm.timeComplexity,
                spaceComplexity = algorithm.spaceComplexity
            )
        }

        // 특징 섹션
        if (algorithm.characteristics.isNotEmpty()) {
            item {
                SectionCard(
                    title = "특징",
                    icon = "📌"
                ) {
                    BulletList(items = algorithm.characteristics)
                }
            }
        }

        // 장점 섹션
        item {
            SectionCard(
                title = "장점",
                icon = "✅"
            ) {
                BulletList(items = algorithm.advantages)
            }
        }

        // 단점 섹션
        item {
            SectionCard(
                title = "단점",
                icon = "❌"
            ) {
                BulletList(items = algorithm.disadvantages)
            }
        }

        // 활용 예시 섹션
        item {
            SectionCard(
                title = "활용 예시",
                icon = "💡"
            ) {
                BulletList(items = algorithm.useCases)
            }
        }

        // 코드 예시 섹션
        item {
            CodeExampleSection(
                codeExamples = algorithm.codeExamples,
                selectedLanguage = selectedLanguage,
                onLanguageChange = onLanguageChange,
                onCodePlaygroundClick = onCodePlaygroundClick
            )
        }

        // 관련 알고리즘 섹션
        if (algorithm.relatedAlgorithms.isNotEmpty()) {
            item {
                RelatedAlgorithmsSection(
                    relatedAlgorithms = algorithm.relatedAlgorithms,
                    onAlgorithmClick = onRelatedAlgorithmClick
                )
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 알고리즘 헤더
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlgorithmHeader(algorithm: AlgorithmDetailUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 한글 이름
        Text(
            text = algorithm.koreanName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 태그들
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 카테고리 태그
            CategoryChip(category = algorithm.category)

            // 난이도 태그
            DifficultyChip(difficulty = algorithm.difficulty)

            // 사용 빈도 태그
            FrequencyChip(frequency = algorithm.frequency)
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()
    }
}

/**
 * 카테고리 칩
 */
@Composable
private fun CategoryChip(category: AlgorithmCategory) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = category.koreanName,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * 난이도 칩
 */
@Composable
private fun DifficultyChip(difficulty: Difficulty) {
    val (text, color) = when (difficulty) {
        Difficulty.LOW -> "쉬움" to MaterialTheme.colorScheme.tertiary
        Difficulty.MEDIUM -> "보통" to MaterialTheme.colorScheme.secondary
        Difficulty.HIGH -> "어려움" to MaterialTheme.colorScheme.error
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "난이도: $text",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

/**
 * 사용 빈도 칩
 */
@Composable
private fun FrequencyChip(frequency: Int) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "빈도: ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            repeat(5) { index ->
                Text(
                    text = "★",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index < frequency) {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.3f)
                    }
                )
            }
        }
    }
}

/**
 * 복잡도 섹션
 */
@Composable
private fun ComplexitySection(
    timeComplexity: TimeComplexity,
    spaceComplexity: String
) {
    SectionCard(
        title = "복잡도",
        icon = "⏱️"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 시간 복잡도
            Text(
                text = "시간 복잡도",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ComplexityBadge(label = "Best", complexity = timeComplexity.best)
                ComplexityBadge(label = "Average", complexity = timeComplexity.average)
                ComplexityBadge(label = "Worst", complexity = timeComplexity.worst)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 공간 복잡도
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "공간 복잡도: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = spaceComplexity,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

/**
 * 복잡도 배지
 */
@Composable
private fun ComplexityBadge(label: String, complexity: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = complexity,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * 섹션 카드
 */
@Composable
private fun SectionCard(
    title: String,
    icon: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

/**
 * 불릿 리스트
 */
@Composable
private fun BulletList(items: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Row {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * 코드 예시 섹션
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CodeExampleSection(
    codeExamples: List<CodeExample>,
    selectedLanguage: ProgrammingLanguage,
    onLanguageChange: (ProgrammingLanguage) -> Unit,
    onCodePlaygroundClick: () -> Unit
) {
    val availableLanguages = codeExamples.map { it.language }.distinct()
    val currentExample = codeExamples.find { it.language == selectedLanguage }
        ?: codeExamples.firstOrNull()

    SectionCard(
        title = "코드 예시",
        icon = "💻"
    ) {
        Column {
            // 언어 선택 칩
            if (availableLanguages.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableLanguages.forEach { language ->
                        FilterChip(
                            selected = language == selectedLanguage,
                            onClick = { onLanguageChange(language) },
                            label = {
                                Text(text = language.displayName)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // 코드 블록
            currentExample?.let { example ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = example.code,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (example.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = example.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 코드 실행 버튼
                FilledTonalButton(
                    onClick = onCodePlaygroundClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("코드 플레이그라운드에서 실행")
                }
            }
        }
    }
}

/**
 * 관련 알고리즘 섹션
 */
@Composable
private fun RelatedAlgorithmsSection(
    relatedAlgorithms: List<RelatedAlgorithmUiModel>,
    onAlgorithmClick: (String) -> Unit
) {
    SectionCard(
        title = "관련 알고리즘",
        icon = "🔗"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            relatedAlgorithms.forEach { algorithm ->
                AssistChip(
                    onClick = { onAlgorithmClick(algorithm.id) },
                    label = {
                        Column {
                            Text(
                                text = algorithm.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = algorithm.koreanName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * 프로그래밍 언어 표시 이름 확장 프로퍼티
 */
private val ProgrammingLanguage.displayName: String
    get() = when (this) {
        ProgrammingLanguage.KOTLIN -> "Kotlin"
        ProgrammingLanguage.JAVA -> "Java"
        ProgrammingLanguage.SWIFT -> "Swift"
        ProgrammingLanguage.PYTHON -> "Python"
        ProgrammingLanguage.JAVASCRIPT -> "JavaScript"
    }
