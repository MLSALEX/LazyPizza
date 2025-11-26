package com.alexmls.lazypizza.cart_checkout.domain

import com.alexmls.lazypizza.cart_checkout.domain.model.CartLineId
import com.alexmls.lazypizza.core.domain.cart.ToppingEntry

fun buildId(productId: String, toppings: List<ToppingEntry>): CartLineId {
    val k = toppings.sortedBy { it.id }
        .joinToString("+") { "${it.id}×${it.units}" }
    return CartLineId("$productId|$k")
}