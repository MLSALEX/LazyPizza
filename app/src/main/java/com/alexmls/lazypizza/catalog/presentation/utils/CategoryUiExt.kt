package com.alexmls.lazypizza.catalog.presentation.utils

import androidx.annotation.StringRes
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi

@get:StringRes
val CategoryUi.titleRes: Int
    get() = when (this) {
        CategoryUi.Pizza    -> R.string.cat_pizza
        CategoryUi.Drinks   -> R.string.cat_drinks
        CategoryUi.Sauces   -> R.string.cat_sauces
        CategoryUi.IceCream -> R.string.cat_ice_cream
    }