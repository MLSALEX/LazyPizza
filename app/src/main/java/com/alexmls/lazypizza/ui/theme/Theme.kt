package com.alexmls.lazypizza.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextOnPrimary,

    background = BG,
    onBackground = TextPrimary,

    surface = SurfaceHigher,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceHighest,
    onSurfaceVariant = TextSecondary,

    outline = Outline,
    error = Color(0xFFB3261E)
)

@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}