package com.alexmls.lazypizza.cart.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class AddonUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val priceCents: Int
)