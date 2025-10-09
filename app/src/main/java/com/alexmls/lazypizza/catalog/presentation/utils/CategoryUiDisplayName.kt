package com.alexmls.lazypizza.catalog.presentation.utils

import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi

fun CategoryUi.displayName(): String = when (this) {
    CategoryUi.Pizza  -> "PIZZA"
    CategoryUi.Drinks -> "DRINKS"
    CategoryUi.Sauces -> "SAUCES"
    CategoryUi.IceCream -> "ICE CREAM"
}

val CATEGORY_ORDER = listOf(
    CategoryUi.Pizza, CategoryUi.Drinks, CategoryUi.Sauces, CategoryUi.IceCream
)