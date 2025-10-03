package com.alexmls.lazypizza.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.alexmls.lazypizza.R

val InstrumentSans = FontFamily(
    Font(
        resId = R.font.instrument_sans_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.instrument_sans_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.instrument_sans_semibold,
        weight = FontWeight.SemiBold
    ),
)

val Typography = Typography(
    // Title
    titleLarge = TextStyle( // Title-1
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle( // Title-2
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle( // Title-3
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),

    // Body
    bodyLarge = TextStyle( // Body-1 Regular
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle( // Body-3 Regular
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    bodySmall = TextStyle( // Body-4 Regular
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),

    // Label
    labelMedium = TextStyle( // Label-2 SemiBold
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

   // Body-1-Medium 16/22
val Typography.bodyLargeMedium: TextStyle
    get() = bodyLarge.copy(fontWeight = FontWeight.Medium)

 // Body-3-Medium 14/18
val Typography.bodyMediumMedium: TextStyle
    get() = bodyMedium.copy(fontWeight = FontWeight.Medium)

val Typography.bodyMediumBold: TextStyle
    get() = bodyMedium.copy(fontWeight = FontWeight.Bold)