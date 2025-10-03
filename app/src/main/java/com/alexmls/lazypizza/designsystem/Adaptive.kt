package com.alexmls.lazypizza.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalConfiguration

@Immutable
enum class LayoutType { Mobile, Wide }

/**
 * - width < 840dp  -> Mobile
 * - width >= 840dp -> Wide
 */
@Composable
fun rememberLayoutType(): LayoutType {
    val widthDp = LocalConfiguration.current.screenWidthDp
    return if (widthDp >= 840) LayoutType.Wide else LayoutType.Mobile
}

/**
 * Convenience wrapper to branch UI by layout type.
 */
@Composable
inline fun Adaptive(
    layout: LayoutType = rememberLayoutType(),
    mobile: @Composable () -> Unit,
    wide: @Composable () -> Unit
) {
    when (layout) {
        LayoutType.Mobile -> mobile()
        LayoutType.Wide   -> wide()
    }
}