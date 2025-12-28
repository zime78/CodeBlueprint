package com.codeblueprint.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.codeblueprint.presentation.navigation.CodeBlueprintNavHost
import com.codeblueprint.presentation.theme.CodeBlueprintTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 앱의 메인 액티비티
 *
 * Jetpack Compose를 사용하여 UI를 구성하고,
 * Navigation을 통해 화면 전환을 관리합니다.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CodeBlueprintTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CodeBlueprintNavHost()
                }
            }
        }
    }
}
