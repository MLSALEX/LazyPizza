package com.alexmls.lazypizza.feature.catalog.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class ProductUi(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Int,
    val priceLabel: String,
    val category: CategoryUi,
    @DrawableRes val imageResId: Int
)

enum class CategoryUi { Pizza, Drinks, Sauces, IceCream }