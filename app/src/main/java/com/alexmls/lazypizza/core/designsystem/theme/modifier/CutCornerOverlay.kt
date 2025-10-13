package com.alexmls.lazypizza.core.designsystem.theme.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Which single corner to round & overlay.
 */
enum class Corner { TopStart, TopEnd, BottomStart, BottomEnd }

/**
 * Build a RoundedCornerShape with only one rounded corner.
 */
private fun oneCornerShape(corner: Corner, radius: Dp) = when (corner) {
    Corner.TopStart    -> RoundedCornerShape(topStart = radius)
    Corner.TopEnd      -> RoundedCornerShape(topEnd = radius)
    Corner.BottomStart -> RoundedCornerShape(bottomStart = radius)
    Corner.BottomEnd   -> RoundedCornerShape(bottomEnd = radius)
}
fun Modifier.cutCornerOverlay(
    corner: Corner,
    radius: Dp,
    overlayColor: Color
): Modifier = this
    .clip(oneCornerShape(corner, radius))
    .background(overlayColor)