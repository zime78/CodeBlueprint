package com.codeblueprint

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.codeblueprint.di.initKoin
import com.codeblueprint.ui.App
import com.codeblueprint.ui.navigation.DefaultRootComponent

/**
 * Desktop 앱 진입점
 */
fun main() = application {
    // Koin 초기화
    initKoin()

    val lifecycle = LifecycleRegistry()
    val rootComponent = DefaultRootComponent(
        componentContext = DefaultComponentContext(lifecycle = lifecycle)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "CodeBlueprint - 디자인 패턴 학습",
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        )
    ) {
        App(component = rootComponent)
    }
}
