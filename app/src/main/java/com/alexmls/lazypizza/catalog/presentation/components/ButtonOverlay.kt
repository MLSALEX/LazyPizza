package com.alexmls.lazypizza.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton

@Composable
fun ButtonOverlay(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrimHeight: Dp = 100.dp,
    buttonHeight: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.surface
) {
    Box(
        modifier
            .height(scrimHeight)
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to color.copy(alpha = 0f),
                        0.40f to color.copy(alpha = 1f),
                        1.00f to color.copy(alpha = 1f)
                    )
                )
            )
    ) {
        LpPrimaryButton(
            text = text,
            onClick = onClick,
            height = buttonHeight,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}