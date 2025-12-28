package com.codeblueprint

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.codeblueprint.ui.App
import com.codeblueprint.ui.navigation.DefaultRootComponent

/**
 * iOS 메인 ViewController 생성
 *
 * Swift에서 호출되어 Compose UI를 표시합니다.
 */
fun MainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()
    val rootComponent = DefaultRootComponent(
        componentContext = DefaultComponentContext(lifecycle = lifecycle)
    )

    App(component = rootComponent)
}
