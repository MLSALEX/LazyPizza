package com.alexmls.lazypizza.catalog.presentation.screens.product_details

sealed interface ProductDetailsEvent {
    data object AddedToCart : ProductDetailsEvent
}