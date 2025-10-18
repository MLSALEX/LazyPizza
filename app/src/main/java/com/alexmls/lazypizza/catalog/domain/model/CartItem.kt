package com.alexmls.lazypizza.catalog.domain.model

data class CartItem(
    val productId: String,
    val toppings: Map<String, Int>,
    val quantity: Int
)