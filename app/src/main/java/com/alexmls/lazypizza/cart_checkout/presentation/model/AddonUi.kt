package com.alexmls.lazypizza.cart_checkout.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class AddonUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val priceCents: Int
)