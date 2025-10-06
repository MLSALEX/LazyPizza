package com.alexmls.lazypizza.feature.catalog.presentation.mapper

import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.feature.catalog.domain.model.Category
import com.alexmls.lazypizza.feature.catalog.domain.model.Product
import com.alexmls.lazypizza.feature.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.feature.catalog.presentation.model.ProductUi

private val localImageRes: Map<String, Int> = mapOf(
    "pizza_margherita" to R.drawable.pizza_margherita,
    "pizza_pepperoni"  to R.drawable.pizza_pepperoni,
    "drink_mineral"    to R.drawable.mineral_water,
    "sauce_bbq"        to R.drawable.sauce_bbq
)

fun Product.toUi(): ProductUi {
    return ProductUi(
        id = id,
        name = name,
        description = description?.trim().orEmpty(),
        priceCents = priceCents,
        category = category.toUi(),
        imageResId = localImageRes[id] ?: R.drawable.ic_placeholder
    )
}

private fun Category.toUi() = when (this) {
    Category.Pizza -> CategoryUi.Pizza
    Category.Drinks -> CategoryUi.Drinks
    Category.Sauces -> CategoryUi.Sauces
    Category.IceCream -> CategoryUi.IceCream
}

/*
 // LATER (when switching to URLs):
 // 1) set url.
 // 2) Swap Image(painterResource(...)) -> AsyncImage(model = url).
*/