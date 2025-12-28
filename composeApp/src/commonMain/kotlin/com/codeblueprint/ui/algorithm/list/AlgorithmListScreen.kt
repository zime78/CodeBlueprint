package com.codeblueprint.ui.algorithm.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codeblueprint.domain.model.AlgorithmCategory
import com.codeblueprint.presentation.algorithm.list.AlgorithmListEvent
import com.codeblueprint.presentation.algorithm.list.AlgorithmListUiState
import com.codeblueprint.presentation.algorithm.list.AlgorithmListViewModel
import com.codeblueprint.presentation.algorithm.list.AlgorithmUiModel
import com.codeblueprint.ui.navigation.AlgorithmListComponent
import org.koin.compose.koinInject

/**
 * 알고리즘 목록 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmListScreen(
    component: AlgorithmListComponent,
    viewModel: AlgorithmListViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val expandedCategories by viewModel.expandedCategories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "알고리즘",
                        style = MaterialTheme.typography.headlineSmall
                    )
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
                    IconButton(onClick = { component.onSearchClick() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
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
                is AlgorithmListUiState.Loading -> {
                    LoadingContent()
                }

                is AlgorithmListUiState.Success -> {
                    SuccessContent(
                        state = state,
                        expandedCategories = expandedCategories,
                        onAlgorithmClick = { component.onAlgorithmClick(it) },
                        onBookmarkToggle = { algorithmId ->
                            viewModel.onEvent(AlgorithmListEvent.OnBookmarkToggle(algorithmId))
                        },
                        onCategoryToggle = { category ->
                            viewModel.onEvent(AlgorithmListEvent.OnCategoryToggle(category))
                        }
                    )
                }

                is AlgorithmListUiState.Error -> {
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
 * 성공 화면 컨텐츠
 */
@Composable
private fun SuccessContent(
    state: AlgorithmListUiState.Success,
    expandedCategories: Set<AlgorithmCategory>,
    onAlgorithmClick: (String) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onCategoryToggle: (AlgorithmCategory) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 학습 진도 카드
        item {
            LearningProgressCard(
                progress = state.learningProgress,
                completedCount = state.completedCount,
                totalCount = state.totalCount
            )
        }

        // 카테고리별 알고리즘 목록
        AlgorithmCategory.entries.forEach { category ->
            val algorithms = state.algorithmsByCategory[category] ?: emptyList()
            if (algorithms.isNotEmpty()) {
                val isExpanded = expandedCategories.contains(category)

                item(key = "category_${category.name}") {
                    CategoryHeader(
                        category = category,
                        algorithmCount = algorithms.size,
                        isExpanded = isExpanded,
                        onToggle = { onCategoryToggle(category) }
                    )
                }

                if (isExpanded) {
                    items(
                        items = algorithms,
                        key = { it.id }
                    ) { algorithm ->
                        AlgorithmItem(
                            algorithm = algorithm,
                            onClick = { onAlgorithmClick(algorithm.id) },
                            onBookmarkClick = { onBookmarkToggle(algorithm.id) }
                        )
                    }
                }
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 학습 진도 카드
 */
@Composable
private fun LearningProgressCard(
    progress: Float,
    completedCount: Int,
    totalCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "알고리즘 학습 진도",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$completedCount/$totalCount (${(progress * 100).toInt()}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
        }
    }
}

/**
 * 카테고리 헤더
 */
@Composable
private fun CategoryHeader(
    category: AlgorithmCategory,
    algorithmCount: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.koreanName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = algorithmCount.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "축소" else "확장",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 알고리즘 아이템
 */
@Composable
private fun AlgorithmItem(
    algorithm: AlgorithmUiModel,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 완료 표시
            if (algorithm.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "완료",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // 알고리즘 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = algorithm.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = algorithm.koreanName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = algorithm.purpose,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 복잡도, 난이도 및 사용 빈도
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ComplexityBadge(complexity = algorithm.timeComplexity)
                    DifficultyIndicator(difficulty = algorithm.difficulty)
                    FrequencyIndicator(frequency = algorithm.frequency)
                }
            }

            // 북마크 버튼
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (algorithm.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (algorithm.isBookmarked) "북마크 해제" else "북마크",
                    tint = if (algorithm.isBookmarked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

/**
 * 복잡도 배지
 */
@Composable
private fun ComplexityBadge(complexity: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = complexity,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * 난이도 표시
 */
@Composable
private fun DifficultyIndicator(difficulty: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "난이도",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        repeat(3) { index ->
            Text(
                text = "●",
                style = MaterialTheme.typography.labelSmall,
                color = if (index < difficulty) {
                    getDifficultyColor(difficulty)
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            )
        }
    }
}

/**
 * 사용 빈도 표시
 */
@Composable
private fun FrequencyIndicator(frequency: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "빈도",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        repeat(5) { index ->
            Text(
                text = "★",
                style = MaterialTheme.typography.labelSmall,
                color = if (index < frequency) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            )
        }
    }
}

/**
 * 난이도별 색상
 */
@Composable
private fun getDifficultyColor(difficulty: Int) = when (difficulty) {
    1 -> MaterialTheme.colorScheme.tertiary
    2 -> MaterialTheme.colorScheme.secondary
    3 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}
