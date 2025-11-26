package com.alexmls.lazypizza.cart_checkout.presentation.screens.cart

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.model.OrderItemUi

@Immutable
data class CartState(
    val items: List<OrderItemUi> = emptyList(),
    val totalCents: Int = 0,
    val addons: List<AddonUi> = emptyList()
) {
    val isEmpty: Boolean get() = items.isEmpty()
}