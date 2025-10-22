package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi

@Immutable
data class CartState(
    val items: List<CartItemUi> = emptyList(),
    val totalCents: Int = 0
) {
    val isEmpty: Boolean get() = items.isEmpty()
}