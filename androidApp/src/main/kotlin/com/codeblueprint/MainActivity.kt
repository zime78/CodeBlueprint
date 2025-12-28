package com.codeblueprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.codeblueprint.ui.App
import com.codeblueprint.ui.navigation.DefaultRootComponent

/**
 * Android 메인 액티비티
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext()
        )

        setContent {
            App(component = rootComponent)
        }
    }
}
