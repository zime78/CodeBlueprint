package com.codeblueprint.ui.ai

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codeblueprint.domain.model.PatternRecommendation
import com.codeblueprint.presentation.ai.AIAdvisorEvent
import com.codeblueprint.presentation.ai.AIAdvisorUiState
import com.codeblueprint.presentation.ai.AIAdvisorViewModel
import com.codeblueprint.ui.navigation.AIAdvisorComponent
import org.koin.compose.koinInject

/**
 * AI Advisor 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAdvisorScreen(
    component: AIAdvisorComponent,
    viewModel: AIAdvisorViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 패턴 어드바이저") },
                navigationIcon = {
                    IconButton(onClick = { component.onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 입력 영역
            QueryInputSection(
                query = query,
                isLoading = uiState is AIAdvisorUiState.Loading,
                onQueryChange = { viewModel.onEvent(AIAdvisorEvent.OnQueryChange(it)) },
                onSubmit = { viewModel.onEvent(AIAdvisorEvent.OnSubmit) },
                onClear = { viewModel.onEvent(AIAdvisorEvent.OnClearQuery) }
            )

            // 결과 영역
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (val state = uiState) {
                    is AIAdvisorUiState.Idle -> {
                        IdleContent()
                    }
                    is AIAdvisorUiState.Loading -> {
                        LoadingContent()
                    }
                    is AIAdvisorUiState.Success -> {
                        SuccessContent(
                            recommendations = state.recommendations,
                            onPatternClick = { component.onPatternClick(it) }
                        )
                    }
                    is AIAdvisorUiState.Error -> {
                        ErrorContent(message = state.message)
                    }
                }
            }
        }
    }
}

@Composable
private fun QueryInputSection(
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "문제 상황을 설명해 주세요",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("예: 데이터베이스 연결을 앱 전체에서 하나만 사용하고 싶어요")
                },
                enabled = !isLoading,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                trailingIcon = {
                    Row {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = onClear) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "지우기"
                                )
                            }
                        }
                        IconButton(
                            onClick = onSubmit,
                            enabled = query.isNotBlank() && !isLoading
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "추천 받기"
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun IdleContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Psychology,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "문제 상황을 설명하면",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "적합한 디자인 패턴을 추천해 드립니다",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "예시 질문:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExampleQueries()
    }
}

@Composable
private fun ExampleQueries() {
    val examples = listOf(
        "전역 인스턴스가 하나만 필요해요",
        "객체 생성 로직을 분리하고 싶어요",
        "이벤트 발생 시 여러 객체에 알려야 해요",
        "알고리즘을 런타임에 바꾸고 싶어요"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        examples.forEach { example ->
            Text(
                text = "• $example",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "패턴을 분석하고 있습니다...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SuccessContent(
    recommendations: List<PatternRecommendation>,
    onPatternClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "추천 패턴",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(recommendations) { recommendation ->
            RecommendationCard(
                recommendation = recommendation,
                onClick = { onPatternClick(recommendation.patternId) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "※ AI 추천은 참고용이며, 실제 상황에 따라 다를 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecommendationCard(
    recommendation: PatternRecommendation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = recommendation.patternName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${recommendation.koreanName})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                MatchRateBadge(matchRate = recommendation.matchRate)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 매칭률 바
            LinearProgressIndicator(
                progress = { recommendation.matchRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = getMatchRateColor(recommendation.matchRate),
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = recommendation.reasoning,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "자세히 보기 →",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun MatchRateBadge(matchRate: Float) {
    val percentage = (matchRate * 100).toInt()

    Surface(
        color = getMatchRateColor(matchRate).copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "${percentage}% 일치",
            style = MaterialTheme.typography.labelMedium,
            color = getMatchRateColor(matchRate),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun getMatchRateColor(matchRate: Float) = when {
    matchRate >= 0.8f -> MaterialTheme.colorScheme.tertiary
    matchRate >= 0.6f -> MaterialTheme.colorScheme.secondary
    else -> MaterialTheme.colorScheme.outline
}

@Composable
private fun ErrorContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}
