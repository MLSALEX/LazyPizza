package com.alexmls.lazypizza.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Material 3 Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = Primary8,
    onPrimaryContainer = TextPrimary,

    secondary = TextSecondary,
    onSecondary = TextOnPrimary,
    secondaryContainer = TextSecondary8,
    onSecondaryContainer = TextPrimary,

    background = BG,
    onBackground = TextPrimary,

    surface = SurfaceHigher,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceHighest,
    onSurfaceVariant = TextSecondary,

    outline = Outline,
    outlineVariant = Outline50,

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
        shapes      = Shapes,
    )
}