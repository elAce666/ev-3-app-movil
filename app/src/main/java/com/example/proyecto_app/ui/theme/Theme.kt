package com.example.proyecto_app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // <-- IMPORT AÑADIDO
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = GuauMiauBlue,
    secondary = GuauMiauOrange,
    tertiary = Pink40,
    background = GuauMiauLightGray,
    surface = GuauMiauWhite,
    onPrimary = GuauMiauWhite,
    onSecondary = GuauMiauWhite,
    onTertiary = GuauMiauWhite,
    onBackground = GuauMiauDarkGray,
    onSurface = GuauMiauDarkGray,
    error = GuauMiauRed,
    onError = GuauMiauWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = GuauMiauBlue,
    secondary = GuauMiauOrange,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2C2A31),
    onPrimary = GuauMiauWhite,
    onSecondary = GuauMiauDarkGray,
    onTertiary = GuauMiauDarkGray,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    error = GuauMiauRed,
    onError = GuauMiauWhite
)

@Composable
fun Proyecto_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
