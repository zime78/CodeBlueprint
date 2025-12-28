package com.codeblueprint.presentation.search

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codeblueprint.R
import com.codeblueprint.domain.model.PatternCategory

/**
 * 검색 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onPatternClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 화면 진입 시 검색창에 포커스
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 검색 입력 필드
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onEvent(SearchEvent.OnQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(text = stringResource(R.string.search_hint))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onEvent(SearchEvent.OnClearQuery) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "지우기",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onEvent(SearchEvent.OnSearch)
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 검색 결과
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val state = uiState) {
                    is SearchUiState.Initial -> {
                        InitialContent()
                    }

                    is SearchUiState.Searching -> {
                        LoadingContent()
                    }

                    is SearchUiState.Results -> {
                        ResultsContent(
                            results = state.patterns,
                            totalCount = state.totalCount,
                            onPatternClick = onPatternClick,
                            onBookmarkToggle = { patternId ->
                                viewModel.onEvent(SearchEvent.OnBookmarkToggle(patternId))
                            }
                        )
                    }

                    is SearchUiState.Empty -> {
                        EmptyContent(query = state.query)
                    }

                    is SearchUiState.Error -> {
                        ErrorContent(message = state.message)
                    }
                }
            }
        }
    }
}

/**
 * 초기 화면 (검색 전)
 */
@Composable
private fun InitialContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "패턴명, 목적, 활용 예시로 검색해보세요",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
 * 결과 없음 화면
 */
@Composable
private fun EmptyContent(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "'$query'에 대한 검색 결과가 없습니다",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
 * 검색 결과 목록
 */
@Composable
private fun ResultsContent(
    results: List<SearchResultUiModel>,
    totalCount: Int,
    onPatternClick: (String) -> Unit,
    onBookmarkToggle: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 결과 수 표시
        item {
            Text(
                text = "$totalCount개의 결과",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 결과 아이템들
        items(
            items = results,
            key = { it.id }
        ) { result ->
            SearchResultItem(
                result = result,
                onClick = { onPatternClick(result.id) },
                onBookmarkClick = { onBookmarkToggle(result.id) }
            )
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 검색 결과 아이템
 */
@Composable
private fun SearchResultItem(
    result: SearchResultUiModel,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
            if (result.isCompleted) {
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
                    // 카테고리 태그
                    CategoryTag(category = result.category)
                    Spacer(modifier = Modifier.width(8.dp))

                    // 일치 필드 태그
                    MatchedFieldTag(matchedField = result.matchedField)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = result.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = result.koreanName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = result.purpose,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 북마크 버튼
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (result.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (result.isBookmarked) "북마크 해제" else "북마크",
                    tint = if (result.isBookmarked) {
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
 * 카테고리 태그
 */
@Composable
private fun CategoryTag(category: PatternCategory) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = when (category) {
                PatternCategory.CREATIONAL -> "생성"
                PatternCategory.STRUCTURAL -> "구조"
                PatternCategory.BEHAVIORAL -> "행위"
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * 일치 필드 태그
 */
@Composable
private fun MatchedFieldTag(matchedField: MatchedField) {
    val text = when (matchedField) {
        MatchedField.NAME -> "이름 일치"
        MatchedField.KOREAN_NAME -> "한글명 일치"
        MatchedField.PURPOSE -> "목적 일치"
        MatchedField.CHARACTERISTICS -> "특징 일치"
        MatchedField.USE_CASES -> "활용 예시 일치"
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}
