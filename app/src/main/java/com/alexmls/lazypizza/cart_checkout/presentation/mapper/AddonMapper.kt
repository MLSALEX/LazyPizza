package com.alexmls.lazypizza.cart_checkout.presentation.mapper

import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.core.domain.catalog.Addon

fun Addon.toUi(): AddonUi = AddonUi(
    id = id,
    name = name,
    imageUrl = imageUrl,
    priceCents = priceCents
)