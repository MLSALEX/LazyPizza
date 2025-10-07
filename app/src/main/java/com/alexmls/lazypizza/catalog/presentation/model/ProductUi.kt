package com.alexmls.lazypizza.catalog.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class ProductUi(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Int,
    val category: CategoryUi,
    @DrawableRes val imageResId: Int
)

enum class CategoryUi { Pizza, Drinks, Sauces, IceCream }