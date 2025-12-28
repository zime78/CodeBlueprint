package com.codeblueprint.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.codeblueprint.presentation.algorithm.list.AlgorithmListEvent
import com.codeblueprint.presentation.algorithm.list.AlgorithmListUiState
import com.codeblueprint.presentation.algorithm.list.AlgorithmListViewModel
import com.codeblueprint.presentation.architecture.list.ArchitectureListUiState
import com.codeblueprint.presentation.architecture.list.ArchitectureListViewModel
import com.codeblueprint.presentation.pattern.list.PatternListEvent
import com.codeblueprint.presentation.pattern.list.PatternListUiState
import com.codeblueprint.presentation.pattern.list.PatternListViewModel
import com.codeblueprint.ui.algorithm.list.AlgorithmListContent
import com.codeblueprint.ui.architecture.ArchitectureListContent
import com.codeblueprint.ui.navigation.MainComponent
import com.codeblueprint.ui.pattern.list.PatternListContent
import org.koin.compose.koinInject

/**
 * 탭 기반 메인 화면
 * 패턴, 알고리즘, 아키텍처 목록을 탭으로 전환
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    component: MainComponent,
    patternViewModel: PatternListViewModel = koinInject(),
    algorithmViewModel: AlgorithmListViewModel = koinInject(),
    architectureViewModel: ArchitectureListViewModel = koinInject()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    // 각 ViewModel의 상태 수집
    val patternUiState by patternViewModel.uiState.collectAsState()
    val patternExpandedCategories by patternViewModel.expandedCategories.collectAsState()

    val algorithmUiState by algorithmViewModel.uiState.collectAsState()
    val algorithmExpandedCategories by algorithmViewModel.expandedCategories.collectAsState()

    val architectureUiState by architectureViewModel.uiState.collectAsState()

    // 각 카테고리별 총 개수 계산
    val patternCount = when (val state = patternUiState) {
        is PatternListUiState.Success -> state.patternsByCategory.values.flatten().size
        else -> 0
    }
    val algorithmCount = when (val state = algorithmUiState) {
        is AlgorithmListUiState.Success -> state.algorithmsByCategory.values.flatten().size
        else -> 0
    }
    val architectureCount = when (val state = architectureUiState) {
        is ArchitectureListUiState.Success -> state.architectures.size
        else -> 0
    }

    // 탭 제목 (개수 포함)
    val tabs = listOf(
        "패턴($patternCount)",
        "알고리즘($algorithmCount)",
        "아키텍처($architectureCount)"
    )

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 탭 UI
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // 탭별 컨텐츠
            when (selectedTabIndex) {
                0 -> PatternListContent(
                    uiState = patternUiState,
                    expandedCategories = patternExpandedCategories,
                    onPatternClick = { component.onPatternClick(it) },
                    onBookmarkToggle = { patternId ->
                        patternViewModel.onEvent(PatternListEvent.OnBookmarkToggle(patternId))
                    },
                    onCategoryToggle = { category ->
                        patternViewModel.onEvent(PatternListEvent.OnCategoryToggle(category))
                    }
                )
                1 -> AlgorithmListContent(
                    uiState = algorithmUiState,
                    expandedCategories = algorithmExpandedCategories,
                    onAlgorithmClick = { component.onAlgorithmClick(it) },
                    onBookmarkToggle = { algorithmId ->
                        algorithmViewModel.onEvent(AlgorithmListEvent.OnBookmarkToggle(algorithmId))
                    },
                    onCategoryToggle = { category ->
                        algorithmViewModel.onEvent(AlgorithmListEvent.OnCategoryToggle(category))
                    }
                )
                2 -> ArchitectureListContent(
                    uiState = architectureUiState,
                    onArchitectureClick = { component.onArchitectureClick(it) }
                )
            }
        }
    }
}
