package com.codeblueprint.presentation.navigation

/**
 * 앱의 화면 경로를 정의하는 sealed class
 *
 * Navigation Compose에서 사용되는 라우트를 중앙에서 관리합니다.
 */
sealed class Screen(val route: String) {

    // Bottom Navigation 탭
    data object PatternList : Screen("pattern_list")
    data object ArchitectureList : Screen("architecture_list")
    data object AIAdvisor : Screen("ai_advisor")
    data object Settings : Screen("settings")

    // 상세 화면
    data object PatternDetail : Screen("pattern_detail/{patternId}") {
        fun createRoute(patternId: String) = "pattern_detail/$patternId"
    }

    data object ArchitectureDetail : Screen("architecture_detail/{architectureId}") {
        fun createRoute(architectureId: String) = "architecture_detail/$architectureId"
    }

    data object CodePlayground : Screen("code_playground/{patternId}") {
        fun createRoute(patternId: String) = "code_playground/$patternId"
    }

    // 검색
    data object Search : Screen("search")

    // 북마크
    data object Bookmarks : Screen("bookmarks")

    companion object {
        const val PATTERN_ID_ARG = "patternId"
        const val ARCHITECTURE_ID_ARG = "architectureId"
    }
}
