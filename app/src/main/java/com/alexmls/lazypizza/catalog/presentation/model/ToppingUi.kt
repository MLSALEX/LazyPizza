package com.alexmls.lazypizza.catalog.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ToppingUi(
    val id: String,
    val name: String,
    val priceCents: Int,
    val imageUrl: String,
    val maxUnits: Int
)