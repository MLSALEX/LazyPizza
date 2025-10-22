package com.alexmls.lazypizza.cart.domain.model

@JvmInline
value class CartLineId(val value: String)

data class CartLine(
    val id: CartLineId,
    val productId: String,
    val name: String,
    val imageUrl: String,
    val qty: Int,
    val unitPriceCents: Int,           // base + sum(toppings*price)
    val toppings: List<CartTopping>
)

data class CartTopping(
    val name: String,
    val units: Int
)