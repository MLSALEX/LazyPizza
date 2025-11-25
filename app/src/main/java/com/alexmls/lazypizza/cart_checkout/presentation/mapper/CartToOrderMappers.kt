package com.alexmls.lazypizza.cart_checkout.presentation.mapper

import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.model.OrderItemUi
import com.alexmls.lazypizza.core.domain.order.AddonItem
import com.alexmls.lazypizza.core.domain.order.OrderItem

fun OrderItemUi.toDomain(): OrderItem {
    return OrderItem(
        productId = id,
        name = name,
        quantity = qty,
        priceCents = unitPriceCents.toLong(),
        addons = emptyList()
    )
}

// Maps UI addon to domain addon
fun AddonUi.toDomain(): AddonItem {
    return AddonItem(
        id = id,
        name = name,
        priceCents = priceCents.toLong()
    )
}