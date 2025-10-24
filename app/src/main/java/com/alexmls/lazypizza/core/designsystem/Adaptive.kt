package com.alexmls.lazypizza.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
enum class LayoutType { Mobile, Wide }

object Breakpoints { val Wide = 840.dp }

val LocalLayoutType = staticCompositionLocalOf { LayoutType.Mobile }

@Composable
fun rememberLayoutType(breakpoint: Dp = Breakpoints.Wide): LayoutType {
    val window = LocalWindowInfo.current
    val density = LocalDensity.current
    val widthDp = with(density) { window.containerSize.width.toDp() }
    return if (widthDp >= breakpoint) LayoutType.Wide else LayoutType.Mobile
}

/**
 * Convenience wrapper to branch UI by layout type.
 */

@Composable
fun Adaptive(
    layout: LayoutType,
    mobile: @Composable () -> Unit,
    wide: @Composable () -> Unit
) {
    when (layout) {
        LayoutType.Mobile -> mobile()
        LayoutType.Wide   -> wide()
    }
}