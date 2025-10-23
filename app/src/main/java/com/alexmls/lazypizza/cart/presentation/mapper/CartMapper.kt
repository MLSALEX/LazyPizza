package com.alexmls.lazypizza.cart.presentation.mapper

import com.alexmls.lazypizza.cart.domain.model.CartLine
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi

fun CartLine.toUi(): CartItemUi = CartItemUi(
    id = id,
    name = name,
    imageUrl = imageUrl,
    qty = qty,
    unitPriceCents = unitPriceCents,
    extrasLines = toppings.map { t ->
        if (t.units == 1) "1 × ${t.name}" else "${t.units} × ${t.name}"
    }
)