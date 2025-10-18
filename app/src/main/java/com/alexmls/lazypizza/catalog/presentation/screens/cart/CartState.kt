package com.alexmls.lazypizza.catalog.presentation.screens.cart

import androidx.compose.runtime.Immutable

@Immutable
data class CartState(
    val itemCount: Int = 0
) {
    val isEmpty: Boolean get() = itemCount == 0
}