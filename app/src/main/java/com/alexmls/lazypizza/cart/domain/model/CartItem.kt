package com.alexmls.lazypizza.cart.domain.model

internal data class CartItem(
    val productId: String,
    val toppings: Map<String, Int>,
    val quantity: Int
)