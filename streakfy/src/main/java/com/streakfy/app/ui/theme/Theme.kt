package com.streakfy.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StreakFyDarkColorScheme = darkColorScheme(
    primary = Purple600,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = Blue600,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    tertiary = Orange500,
    background = Background,
    onBackground = Foreground,
    surface = Card,
    onSurface = Foreground,
    surfaceVariant = Card,
    onSurfaceVariant = Muted,
    outline = Border
)

@Composable
fun StreakFyTheme(
    darkTheme: Boolean = true, // Always use dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StreakFyDarkColorScheme,
        typography = Typography,
        content = content
    )
}