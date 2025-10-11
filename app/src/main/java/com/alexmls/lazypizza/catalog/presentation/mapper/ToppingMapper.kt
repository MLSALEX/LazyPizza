package com.alexmls.lazypizza.catalog.presentation.mapper

import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi

fun Topping.toUi(): ToppingUi = ToppingUi(
    id = id,
    name = name,
    priceCents = priceCents,
    imageUrl = imageUrl,
    maxUnits = maxUnits
)