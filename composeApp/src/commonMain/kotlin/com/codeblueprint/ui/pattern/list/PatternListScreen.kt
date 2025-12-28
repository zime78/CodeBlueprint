package com.codeblueprint.ui.pattern.list

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codeblueprint.domain.model.PatternCategory
import com.codeblueprint.presentation.pattern.list.PatternListEvent
import com.codeblueprint.presentation.pattern.list.PatternListUiState
import com.codeblueprint.presentation.pattern.list.PatternListViewModel
import com.codeblueprint.presentation.pattern.list.PatternUiModel
import com.codeblueprint.ui.navigation.PatternListComponent
import org.koin.compose.koinInject

/**
 * 패턴 목록 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternListScreen(
    component: PatternListComponent,
    viewModel: PatternListViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val expandedCategories by viewModel.expandedCategories.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CodeBlueprint",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = { component.onArchitectureClick() }) {
                        Icon(
                            imageVector = Icons.Default.AccountTree,
                            contentDescription = "아키텍처"
                        )
                    }
                    IconButton(onClick = { component.onAlgorithmClick() }) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "알고리즘"
                        )
                    }
                    IconButton(onClick = { component.onBookmarksClick() }) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = "북마크"
                        )
                    }
                    IconButton(onClick = { component.onSearchClick() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }
                    IconButton(onClick = { component.onSettingsClick() }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "설정"
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
            PatternListContent(
                uiState = uiState,
                expandedCategories = expandedCategories,
                onPatternClick = { component.onPatternClick(it) },
                onBookmarkToggle = { patternId ->
                    viewModel.onEvent(PatternListEvent.OnBookmarkToggle(patternId))
                },
                onCategoryToggle = { category ->
                    viewModel.onEvent(PatternListEvent.OnCategoryToggle(category))
                }
            )
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
 * 패턴 목록 컨텐츠 (MainScreen에서 재사용)
 */
@Composable
fun PatternListContent(
    uiState: PatternListUiState,
    expandedCategories: Set<PatternCategory>,
    onPatternClick: (String) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onCategoryToggle: (PatternCategory) -> Unit
) {
    when (uiState) {
        is PatternListUiState.Loading -> LoadingContent()
        is PatternListUiState.Error -> ErrorContent(uiState.message)
        is PatternListUiState.Success -> PatternSuccessContent(
            state = uiState,
            expandedCategories = expandedCategories,
            onPatternClick = onPatternClick,
            onBookmarkToggle = onBookmarkToggle,
            onCategoryToggle = onCategoryToggle
        )
    }
}

/**
 * 성공 화면 컨텐츠
 */
@Composable
private fun PatternSuccessContent(
    state: PatternListUiState.Success,
    expandedCategories: Set<PatternCategory>,
    onPatternClick: (String) -> Unit,
    onBookmarkToggle: (String) -> Unit,
    onCategoryToggle: (PatternCategory) -> Unit
) {
    // 북마크된 패턴들 추출
    val bookmarkedPatterns = state.patternsByCategory.values
        .flatten()
        .filter { it.isBookmarked }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 빠른 접근 섹션 (북마크된 패턴이 있을 때만 표시)
        if (bookmarkedPatterns.isNotEmpty()) {
            item(key = "quick_access_header") {
                Text(
                    text = "빠른 접근",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item(key = "quick_access_row") {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.width(8.dp)) }
                    items(
                        items = bookmarkedPatterns,
                        key = { "quick_${it.id}" }
                    ) { pattern ->
                        QuickAccessChip(
                            pattern = pattern,
                            onClick = { onPatternClick(pattern.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.width(8.dp)) }
                }
            }

            item(key = "quick_access_divider") {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // 카테고리별 패턴 목록
        PatternCategory.entries.forEach { category ->
            val patterns = state.patternsByCategory[category] ?: emptyList()
            val isExpanded = expandedCategories.contains(category)

            item(key = "category_${category.name}") {
                CategoryHeader(
                    category = category,
                    patternCount = patterns.size,
                    isExpanded = isExpanded,
                    onToggle = { onCategoryToggle(category) }
                )
            }

            if (isExpanded) {
                items(
                    items = patterns,
                    key = { it.id }
                ) { pattern ->
                    PatternItem(
                        pattern = pattern,
                        onClick = { onPatternClick(pattern.id) },
                        onBookmarkClick = { onBookmarkToggle(pattern.id) }
                    )
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
 * 빠른 접근 칩
 */
@Composable
private fun QuickAccessChip(
    pattern: PatternUiModel,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = pattern.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * 카테고리 헤더
 */
@Composable
private fun CategoryHeader(
    category: PatternCategory,
    patternCount: Int,
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
                    text = getCategoryDisplayName(category),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = patternCount.toString(),
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
 * 패턴 아이템
 */
@Composable
private fun PatternItem(
    pattern: PatternUiModel,
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
            // 패턴 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pattern.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pattern.koreanName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pattern.purpose,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 난이도 및 사용 빈도
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DifficultyIndicator(difficulty = pattern.difficulty)
                    FrequencyIndicator(frequency = pattern.frequency)
                }
            }

            // 북마크 버튼
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (pattern.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (pattern.isBookmarked) "북마크 해제" else "북마크",
                    tint = if (pattern.isBookmarked) {
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
 * 카테고리 표시 이름
 */
private fun getCategoryDisplayName(category: PatternCategory): String {
    return when (category) {
        PatternCategory.CREATIONAL -> "생성 패턴"
        PatternCategory.STRUCTURAL -> "구조 패턴"
        PatternCategory.BEHAVIORAL -> "행위 패턴"
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
