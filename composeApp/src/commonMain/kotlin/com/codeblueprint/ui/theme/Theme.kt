package com.codeblueprint.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * CodeBlueprint 앱 테마 색상
 */

// Primary colors
private val PrimaryLight = Color(0xFF1976D2)
private val PrimaryDark = Color(0xFF90CAF9)
private val OnPrimaryLight = Color.White
private val OnPrimaryDark = Color(0xFF003258)

// Secondary colors
private val SecondaryLight = Color(0xFF03A9F4)
private val SecondaryDark = Color(0xFF81D4FA)
private val OnSecondaryLight = Color.White
private val OnSecondaryDark = Color(0xFF00344F)

// Tertiary colors
private val TertiaryLight = Color(0xFF00BCD4)
private val TertiaryDark = Color(0xFF80DEEA)

// Background colors
private val BackgroundLight = Color(0xFFFFFBFE)
private val BackgroundDark = Color(0xFF1C1B1F)

// Surface colors
private val SurfaceLight = Color(0xFFFFFBFE)
private val SurfaceDark = Color(0xFF1C1B1F)

// Error colors
private val ErrorLight = Color(0xFFBA1A1A)
private val ErrorDark = Color(0xFFFFB4AB)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = Color(0xFFD1E4FF),
    onSecondaryContainer = Color(0xFF001D36),
    tertiary = TertiaryLight,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBDE9EC),
    onTertiaryContainer = Color(0xFF002022),
    error = ErrorLight,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = BackgroundLight,
    onBackground = Color(0xFF1C1B1F),
    surface = SurfaceLight,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = Color(0xFF004881),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = Color(0xFF004D6F),
    onSecondaryContainer = Color(0xFFCBE6FF),
    tertiary = TertiaryDark,
    onTertiary = Color(0xFF00363A),
    tertiaryContainer = Color(0xFF004F54),
    onTertiaryContainer = Color(0xFFBDE9EC),
    error = ErrorDark,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = BackgroundDark,
    onBackground = Color(0xFFE6E1E5),
    surface = SurfaceDark,
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
)

/**
 * CodeBlueprint 앱 테마
 */
@Composable
fun CodeBlueprintTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
