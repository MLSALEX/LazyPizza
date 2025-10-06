package com.alexmls.lazypizza.feature.catalog.presentation.mapper

import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.feature.catalog.domain.model.Category
import com.alexmls.lazypizza.feature.catalog.domain.model.Product
import com.alexmls.lazypizza.feature.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.feature.catalog.presentation.model.ProductUi
import java.text.NumberFormat
import java.util.Locale

private val localImageRes: Map<String, Int> = mapOf(
    "pizza_margherita" to R.drawable.pizza_margherita,
    "pizza_pepperoni"  to R.drawable.pizza_pepperoni,
    "drink_mineral"    to R.drawable.mineral_water,
    "sauce_bbq"        to R.drawable.sauce_bbq
)

private fun Int.toPriceLabel(locale: Locale = Locale.getDefault()): String {
    val amount = this / 100.0
    return NumberFormat.getCurrencyInstance(locale).format(amount)
}

fun Product.toUi(locale: Locale = Locale.getDefault()): ProductUi {
    val resId = localImageRes[id] ?: R.drawable.ic_placeholder
    return ProductUi(
        id = id,
        name = name,
        description = description?.trim().orEmpty(),
        priceCents = priceCents,
        priceLabel = priceCents.toPriceLabel(locale),
        category = when (category) {
            Category.Pizza        -> CategoryUi.Pizza
            Category.Drinks       -> CategoryUi.Drinks
            Category.Sauces       -> CategoryUi.Sauces
            Category.IceCream     -> CategoryUi.IceCream
        },
        imageResId = resId
    )
}


/*
 // LATER (when switching to URLs):
 // 1) set url.
 // 2) Swap Image(painterResource(...)) -> AsyncImage(model = url).
*/