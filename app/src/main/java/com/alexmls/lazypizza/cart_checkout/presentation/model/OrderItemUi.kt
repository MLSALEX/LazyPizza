package com.alexmls.lazypizza.cart_checkout.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class OrderItemUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val qty: Int,
    val unitPriceCents: Int,
    val extrasLines: List<String>
) {
    val totalCents: Int get() = unitPriceCents * qty
}