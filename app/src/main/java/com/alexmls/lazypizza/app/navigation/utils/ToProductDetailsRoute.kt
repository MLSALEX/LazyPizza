package com.alexmls.lazypizza.app.navigation.utils

import com.alexmls.lazypizza.app.navigation.NavDestination

fun String.toProductDetailsRoute(): NavDestination.ProductDetails {
    require(isNotBlank()) { "Product id can't be blank." }
    return NavDestination.ProductDetails(productId = this)
}