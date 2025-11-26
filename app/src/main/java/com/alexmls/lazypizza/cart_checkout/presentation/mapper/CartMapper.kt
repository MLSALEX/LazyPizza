package com.alexmls.lazypizza.cart_checkout.presentation.mapper

import com.alexmls.lazypizza.cart_checkout.domain.model.CartLine
import com.alexmls.lazypizza.cart_checkout.presentation.model.OrderItemUi

fun CartLine.toUi(): OrderItemUi = OrderItemUi(
    id = id.value,
    name = name,
    imageUrl = imageUrl,
    qty = qty,
    unitPriceCents = unitPriceCents,
    extrasLines = toppings.map { t ->
        if (t.units == 1) "1 × ${t.name}" else "${t.units} × ${t.name}"
    }
)