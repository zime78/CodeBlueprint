package com.codeblueprint.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.codeblueprint.ui.ai.AIAdvisorScreen
import com.codeblueprint.ui.algorithm.detail.AlgorithmDetailScreen
import com.codeblueprint.ui.algorithm.list.AlgorithmListScreen
import com.codeblueprint.ui.architecture.ArchitectureDetailScreen
import com.codeblueprint.ui.architecture.ArchitectureListScreen
import com.codeblueprint.ui.bookmarks.BookmarksScreen
import com.codeblueprint.ui.navigation.RootComponent
import com.codeblueprint.ui.pattern.detail.PatternDetailScreen
import com.codeblueprint.ui.pattern.list.PatternListScreen
import com.codeblueprint.ui.playground.CodePlaygroundScreen
import com.codeblueprint.ui.search.SearchScreen
import com.codeblueprint.ui.settings.SettingsScreen
import com.codeblueprint.ui.theme.CodeBlueprintTheme

/**
 * 앱 루트 컴포저블
 */
@Composable
fun App(
    component: RootComponent,
    darkTheme: Boolean = false
) {
    CodeBlueprintTheme(darkTheme = darkTheme) {
        val childStack by component.childStack.subscribeAsState()

        Children(
            stack = childStack,
            animation = stackAnimation(fade() + scale())
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.PatternList -> {
                    PatternListScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.PatternDetail -> {
                    PatternDetailScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.Search -> {
                    SearchScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.Bookmarks -> {
                    BookmarksScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.Settings -> {
                    SettingsScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.ArchitectureList -> {
                    ArchitectureListScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.ArchitectureDetail -> {
                    ArchitectureDetailScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.AIAdvisor -> {
                    AIAdvisorScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.CodePlayground -> {
                    CodePlaygroundScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.AlgorithmList -> {
                    AlgorithmListScreen(
                        component = instance.component
                    )
                }
                is RootComponent.Child.AlgorithmDetail -> {
                    AlgorithmDetailScreen(
                        component = instance.component
                    )
                }
            }
        }
    }
}
