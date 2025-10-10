package com.alexmls.lazypizza.catalog.data.remote

import com.alexmls.lazypizza.catalog.domain.model.Category
import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.core.common.buildImageUrl

private fun String.toCategoryOrNull(): Category? = when (lowercase()) {
    "pizza" -> Category.Pizza
    "drink", "drinks" -> Category.Drinks
    "sauce", "sauces" -> Category.Sauces
    "icecream", "ice_cream", "ice-cream", "ice cream" -> Category.IceCream
    else -> null
}

/**
 * Safe mapping from RemoteProduct to domain Product.
 * Returns null if product should not appear in the catalog (inactive, bad data).
 */
fun RemoteProduct.toDomainOrNull(): Product? {
    if (!is_active) return null

    val categoryEnum = category.toCategoryOrNull() ?: return null

    val finalUrl = when {
        !image_url.isNullOrBlank() -> image_url
        !image_path.isNullOrBlank() -> buildImageUrl(image_path)
        else -> null
    } ?: return null

    val safeDescription: String? = description.takeIf { it.isNotBlank() }

    return Product(
        id = id,
        name = name,
        description = safeDescription,
        imageUrl = finalUrl,
        priceCents = price_cents,
        category = categoryEnum
    )
}