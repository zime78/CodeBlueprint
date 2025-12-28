package com.codeblueprint.ui.pattern.detail

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
import com.codeblueprint.domain.model.CodeExample
import com.codeblueprint.domain.model.Difficulty
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.presentation.pattern.detail.PatternDetailEvent
import com.codeblueprint.presentation.pattern.detail.PatternDetailUiModel
import com.codeblueprint.presentation.pattern.detail.PatternDetailUiState
import com.codeblueprint.presentation.pattern.detail.PatternDetailViewModel
import com.codeblueprint.presentation.pattern.detail.RelatedPatternUiModel
import com.codeblueprint.ui.navigation.PatternDetailComponent
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * 패턴 상세 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternDetailScreen(
    component: PatternDetailComponent,
    viewModel: PatternDetailViewModel = koinInject { parametersOf(component.patternId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val state = uiState
                    if (state is PatternDetailUiState.Success) {
                        Text(
                            text = state.pattern.name,
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
                    if (state is PatternDetailUiState.Success) {
                        IconButton(onClick = { viewModel.onEvent(PatternDetailEvent.OnBookmarkToggle) }) {
                            Icon(
                                imageVector = if (state.pattern.isBookmarked) {
                                    Icons.Default.Bookmark
                                } else {
                                    Icons.Default.BookmarkBorder
                                },
                                contentDescription = "북마크",
                                tint = if (state.pattern.isBookmarked) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(PatternDetailEvent.OnShareClick) }) {
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
                is PatternDetailUiState.Loading -> {
                    LoadingContent()
                }

                is PatternDetailUiState.Success -> {
                    DetailContent(
                        pattern = state.pattern,
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { language ->
                            viewModel.onEvent(PatternDetailEvent.OnLanguageChange(language))
                        },
                        onRelatedPatternClick = { component.onRelatedPatternClick(it) },
                        onCodePlaygroundClick = { component.onCodePlaygroundClick() }
                    )
                }

                is PatternDetailUiState.Error -> {
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
    pattern: PatternDetailUiModel,
    selectedLanguage: ProgrammingLanguage,
    onLanguageChange: (ProgrammingLanguage) -> Unit,
    onRelatedPatternClick: (String) -> Unit,
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
            PatternHeader(pattern = pattern)
        }

        // 목적 섹션
        item {
            SectionCard(
                title = "목적",
                icon = "🎯"
            ) {
                Text(
                    text = pattern.purpose,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // 특징 섹션
        if (pattern.characteristics.isNotEmpty()) {
            item {
                SectionCard(
                    title = "특징",
                    icon = "📌"
                ) {
                    BulletList(items = pattern.characteristics)
                }
            }
        }

        // 장점 섹션
        item {
            SectionCard(
                title = "장점",
                icon = "✅"
            ) {
                BulletList(items = pattern.advantages)
            }
        }

        // 단점 섹션
        item {
            SectionCard(
                title = "단점",
                icon = "❌"
            ) {
                BulletList(items = pattern.disadvantages)
            }
        }

        // 활용 예시 섹션
        item {
            SectionCard(
                title = "활용 예시",
                icon = "💡"
            ) {
                BulletList(items = pattern.useCases)
            }
        }

        // 코드 예시 섹션
        item {
            CodeExampleSection(
                codeExamples = pattern.codeExamples,
                selectedLanguage = selectedLanguage,
                onLanguageChange = onLanguageChange,
                onCodePlaygroundClick = onCodePlaygroundClick
            )
        }

        // 클래스 다이어그램 섹션
        if (pattern.diagram.isNotBlank()) {
            item {
                DiagramSection(diagram = pattern.diagram)
            }
        }

        // 관련 패턴 섹션
        if (pattern.relatedPatterns.isNotEmpty()) {
            item {
                RelatedPatternsSection(
                    relatedPatterns = pattern.relatedPatterns,
                    onPatternClick = onRelatedPatternClick
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
 * 패턴 헤더
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PatternHeader(pattern: PatternDetailUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 한글 이름
        Text(
            text = pattern.koreanName,
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
            CategoryChip(category = pattern.category)

            // 난이도 태그
            DifficultyChip(difficulty = pattern.difficulty)

            // 사용 빈도 태그
            FrequencyChip(frequency = pattern.frequency)
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()
    }
}

/**
 * 카테고리 칩
 */
@Composable
private fun CategoryChip(category: PatternCategory) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = when (category) {
                PatternCategory.CREATIONAL -> "생성"
                PatternCategory.STRUCTURAL -> "구조"
                PatternCategory.BEHAVIORAL -> "행위"
            },
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
 * 다이어그램 섹션
 */
@Composable
private fun DiagramSection(diagram: String) {
    SectionCard(
        title = "클래스 다이어그램",
        icon = "📊"
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = diagram,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 관련 패턴 섹션
 */
@Composable
private fun RelatedPatternsSection(
    relatedPatterns: List<RelatedPatternUiModel>,
    onPatternClick: (String) -> Unit
) {
    SectionCard(
        title = "관련 패턴",
        icon = "🔗"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            relatedPatterns.forEach { pattern ->
                AssistChip(
                    onClick = { onPatternClick(pattern.id) },
                    label = {
                        Column {
                            Text(
                                text = pattern.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = pattern.koreanName,
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
