package com.alexmls.lazypizza.catalog.presentation.mapper

import com.alexmls.lazypizza.catalog.domain.model.Category
import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi

fun Product.toUi(): ProductUi {
    return ProductUi(
        id = id,
        name = name,
        description = description?.trim().orEmpty(),
        priceCents = priceCents,
        category = category.toUi(),
        imageUrl = imageUrl
    )
}

private fun Category.toUi() = when (this) {
    Category.Pizza -> CategoryUi.Pizza
    Category.Drinks -> CategoryUi.Drinks
    Category.Sauces -> CategoryUi.Sauces
    Category.IceCream -> CategoryUi.IceCream
}