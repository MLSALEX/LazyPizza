package com.alexmls.lazypizza.catalog.data.remote

import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.core.common.buildImageUrl

fun RemoteTopping.toDomain(): Topping? {
    if (!is_active || id.isBlank()) return null
    val url = when {
        !image_url.isNullOrBlank() -> image_url
        !image_path.isNullOrBlank() -> buildImageUrl(image_path)
        else -> ""
    }
    return Topping(
        id = id,
        name = name,
        priceCents = price_cents,
        imageUrl = url,
        maxUnits = (max_units ?: 3).coerceIn(1, 9)
    )
}