package com.alexmls.lazypizza.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DropShadowAbove(
    modifier: Modifier = Modifier,
    color: Color = Color(0x0F03131F),
    offsetY: Dp = (-4).dp,
    blur: Dp = 16.dp,
    cornerRadius: Dp = 12.dp
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(blur)
            .offset(y = offsetY)
            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
            .background(
                Brush.verticalGradient(
                    listOf(color, color.copy(alpha = 0f))
                )
            )
    )
}