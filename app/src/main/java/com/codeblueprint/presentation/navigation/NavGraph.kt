package com.codeblueprint.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codeblueprint.R
import com.codeblueprint.presentation.pattern.list.PatternListScreen
import com.codeblueprint.presentation.pattern.detail.PatternDetailScreen
import com.codeblueprint.presentation.search.SearchScreen

/**
 * 하단 네비게이션 아이템 정의
 */
data class BottomNavItem(
    val screen: Screen,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * 하단 네비게이션 아이템 목록
 */
val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.PatternList,
        titleResId = R.string.nav_patterns,
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook
    ),
    BottomNavItem(
        screen = Screen.ArchitectureList,
        titleResId = R.string.nav_architecture,
        selectedIcon = Icons.Filled.Architecture,
        unselectedIcon = Icons.Outlined.Architecture
    ),
    BottomNavItem(
        screen = Screen.AIAdvisor,
        titleResId = R.string.nav_ai,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    ),
    BottomNavItem(
        screen = Screen.Settings,
        titleResId = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

/**
 * 앱의 메인 네비게이션 호스트
 *
 * Bottom Navigation과 화면 전환을 관리합니다.
 */
@Composable
fun CodeBlueprintNavHost(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            CodeBlueprintBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.PatternList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 패턴 목록
            composable(route = Screen.PatternList.route) {
                PatternListScreen(
                    onPatternClick = { patternId ->
                        navController.navigate(Screen.PatternDetail.createRoute(patternId))
                    },
                    onSearchClick = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            // 패턴 상세
            composable(
                route = Screen.PatternDetail.route,
                arguments = listOf(
                    navArgument(Screen.PATTERN_ID_ARG) { type = NavType.StringType }
                )
            ) {
                PatternDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onRelatedPatternClick = { relatedPatternId ->
                        navController.navigate(Screen.PatternDetail.createRoute(relatedPatternId))
                    }
                )
            }

            // 아키텍처 목록 (임시)
            composable(route = Screen.ArchitectureList.route) {
                // TODO: ArchitectureListScreen 구현
                Text("아키텍처 패턴 목록")
            }

            // AI 추천 (임시)
            composable(route = Screen.AIAdvisor.route) {
                // TODO: AIAdvisorScreen 구현
                Text("AI 패턴 어드바이저")
            }

            // 설정 (임시)
            composable(route = Screen.Settings.route) {
                // TODO: SettingsScreen 구현
                Text("설정")
            }

            // 검색
            composable(route = Screen.Search.route) {
                SearchScreen(
                    onBackClick = { navController.popBackStack() },
                    onPatternClick = { patternId ->
                        navController.navigate(Screen.PatternDetail.createRoute(patternId))
                    }
                )
            }

            // 코드 플레이그라운드 (임시)
            composable(
                route = Screen.CodePlayground.route,
                arguments = listOf(
                    navArgument(Screen.PATTERN_ID_ARG) { type = NavType.StringType }
                )
            ) {
                // TODO: CodePlaygroundScreen 구현
                Text("코드 플레이그라운드")
            }
        }
    }
}

/**
 * 하단 네비게이션 바
 */
@Composable
private fun CodeBlueprintBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 하단 네비게이션이 표시되어야 하는 화면인지 확인
    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
    }

    if (showBottomBar) {
        NavigationBar {
            bottomNavItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.screen.route
                } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            // 백스택 관리
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.titleResId)
                        )
                    },
                    label = {
                        Text(text = stringResource(item.titleResId))
                    }
                )
            }
        }
    }
}
