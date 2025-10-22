package com.alexmls.lazypizza.cart.presentation.model

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.cart.domain.model.CartLineId

@Immutable
data class CartItemUi(
    val id: CartLineId,
    val name: String,
    val imageUrl: String,
    val qty: Int,
    val unitPriceCents: Int,
    val extrasLines: List<String>
) {
    val totalCents: Int get() = unitPriceCents * qty
}