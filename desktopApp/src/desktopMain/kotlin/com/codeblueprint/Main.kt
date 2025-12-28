package com.codeblueprint

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.codeblueprint.data.local.SeedDatabaseGenerator
import com.codeblueprint.di.initKoin
import com.codeblueprint.ui.App
import com.codeblueprint.ui.navigation.DefaultRootComponent
import kotlin.system.exitProcess

/**
 * Desktop 앱 진입점
 *
 * 옵션:
 * --generate-seed-db <output-path>: Seed 데이터베이스 생성
 */
fun main(args: Array<String>) {
    // Seed DB 생성 모드 처리
    if (args.isNotEmpty() && args[0] == "--generate-seed-db") {
        val outputPath = if (args.size > 1) {
            args[1]
        } else {
            "shared/src/desktopMain/resources/codeblueprint_seed.db"
        }
        SeedDatabaseGenerator.generate(outputPath)
        exitProcess(0)
    }

    // 일반 앱 실행
    application {
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
}
