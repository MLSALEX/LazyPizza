package com.alexmls.lazypizza.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/* ---------- Design tokens (Light only) ---------- */
/* Note: ARGB -> 0xAA RRGGBB; 0x14 ≈ 8%, 0x80 = 50% */

// Text
val TextPrimary       = Color(0xFF03131F)   // #03131F
val TextSecondary     = Color(0xFF627686)   // #627686
val TextSecondary8    = Color(0x14627686)   // #627686 @ 8%
val TextOnPrimary     = Color(0xFFFFFFFF)   // #FFFFFF

// Background / Surfaces
val BG                = Color(0xFFE6E7ED)   // #E6E7ED
val SurfaceHigher     = Color(0xFFFFFFFF)   // #FFFFFF
val SurfaceHighest    = Color(0xFFF0F3F6)   // #F0F3F6

// Outline
val Outline           = Color(0xFFE6E7ED)   // #E6E7ED
val Outline50         = Color(0x80E6E7ED)   // #E6E7ED @ 50%

// Primary
val Primary           = Color(0xFFF36B50)   // #F36B50
val Primary8          = Color(0x14F36B50)   // #F36B50 @ 8%
val PrimaryGradStart  = Color(0xFFF9966F)   // #F9966F
val PrimaryGradEnd    = Color(0xFFF36B50)   // #F36B50
val PrimaryGradient   = Brush.horizontalGradient(listOf(PrimaryGradStart, PrimaryGradEnd))



