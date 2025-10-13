package com.alexmls.lazypizza.catalog.presentation.utils

import com.alexmls.lazypizza.app.navigation.NavigationRoute

fun String.toProductDetailsRoute(): NavigationRoute.ProductDetails {
    require(isNotBlank()) { "Product id can't be blank." }
    return NavigationRoute.ProductDetails(productId = this)
}