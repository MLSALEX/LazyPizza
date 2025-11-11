package com.alexmls.lazypizza.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/* ---------- Design tokens (Light only) ---------- */

val TextPrimary = Color(0xFF03131F)
val TextSecondary = Color(0xFF627686)
val TextSecondary8 = Color(0xFF627686).copy(alpha = 0.08f)
val TextOnPrimary = Color(0xFFFFFFFF)

val BG = Color(0xFFFAFBFC)
val SurfaceHigher = Color(0xFFFFFFFF)
val SurfaceHighest = Color(0xFFF0F3F6)

val Outline = Color(0xFFE6E7ED)
val Outline50 = Color(0xFFE6E7ED).copy(alpha = 0.5f)

val Primary = Color(0xFFF36B50)
val Primary8 = Color(0xFFF36B50).copy(alpha = 0.08f)
val PrimaryGradientEnd = Color(0xFFFF966F)
val PrimaryGradientStart = Primary

val Success = Color(0xFF2E7D32)
val Warning = Color(0xFFF9A825)
val TextTertiary = Color(0xFF101C28)

object BrandColors {
    // Gradients
    val btGradient: Brush      = Brush.horizontalGradient(listOf(PrimaryGradientStart, PrimaryGradientEnd))
}

val ColorScheme.success: Color
    @Composable get() = Success

val ColorScheme.warning: Color
    @Composable get() = Warning

val ColorScheme.text3: Color
    @Composable get() = TextTertiary


