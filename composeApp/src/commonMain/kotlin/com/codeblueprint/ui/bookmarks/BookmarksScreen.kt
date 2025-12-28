package com.codeblueprint.ui.bookmarks

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
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codeblueprint.presentation.bookmarks.BookmarkedPatternUiModel
import com.codeblueprint.presentation.bookmarks.BookmarksEvent
import com.codeblueprint.presentation.bookmarks.BookmarksUiState
import com.codeblueprint.presentation.bookmarks.BookmarksViewModel
import com.codeblueprint.ui.navigation.BookmarksComponent
import org.koin.compose.koinInject

/**
 * 북마크 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    component: BookmarksComponent,
    viewModel: BookmarksViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "북마크",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
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
                is BookmarksUiState.Loading -> {
                    LoadingContent()
                }

                is BookmarksUiState.Success -> {
                    if (state.isEmpty) {
                        EmptyContent()
                    } else {
                        SuccessContent(
                            patterns = state.bookmarkedPatterns,
                            onPatternClick = { component.onPatternClick(it) },
                            onRemoveBookmark = { patternId ->
                                viewModel.onEvent(BookmarksEvent.OnRemoveBookmark(patternId))
                            }
                        )
                    }
                }

                is BookmarksUiState.Error -> {
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
 * 빈 상태 화면
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "북마크된 패턴이 없습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "패턴 목록에서 북마크 아이콘을 눌러\n관심 있는 패턴을 저장해보세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
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
    patterns: List<BookmarkedPatternUiModel>,
    onPatternClick: (String) -> Unit,
    onRemoveBookmark: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            items = patterns,
            key = { it.id }
        ) { pattern ->
            BookmarkedPatternItem(
                pattern = pattern,
                onClick = { onPatternClick(pattern.id) },
                onRemoveBookmark = { onRemoveBookmark(pattern.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 북마크된 패턴 아이템
 */
@Composable
private fun BookmarkedPatternItem(
    pattern: BookmarkedPatternUiModel,
    onClick: () -> Unit,
    onRemoveBookmark: () -> Unit
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
            if (pattern.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "완료",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

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

                // 카테고리 및 난이도
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryChip(categoryName = pattern.categoryName)
                    DifficultyIndicator(difficulty = pattern.difficulty)
                }
            }

            // 북마크 제거 버튼
            IconButton(onClick = onRemoveBookmark) {
                Icon(
                    imageVector = Icons.Default.BookmarkRemove,
                    contentDescription = "북마크 해제",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * 카테고리 칩
 */
@Composable
private fun CategoryChip(categoryName: String) {
    Text(
        text = categoryName,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 2.dp)
    )
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
 * 난이도별 색상
 */
@Composable
private fun getDifficultyColor(difficulty: Int) = when (difficulty) {
    1 -> MaterialTheme.colorScheme.tertiary
    2 -> MaterialTheme.colorScheme.secondary
    3 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}
