package com.alexmls.lazypizza.core.designsystem.card_style

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class LpCardStyle(
    val shape: Shape,
    val container: Color,
    val borderDefault: BorderStroke,
    val borderActive: BorderStroke,
    val shadowSpot: Color,
    val shadowAmbient: Color,
)

@Composable
fun rememberLpCardStyle(): LpCardStyle {
    val cs = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.medium
    val border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f))
    val active = BorderStroke(1.dp, cs.primary.copy(alpha = 0.7f))
    return LpCardStyle(
        shape = shape,
        container = cs.surface,
        borderDefault = border,
        borderActive = active,
        shadowSpot = cs.onPrimaryContainer.copy(alpha = 0.06f),
        shadowAmbient = cs.onPrimaryContainer
    )
}