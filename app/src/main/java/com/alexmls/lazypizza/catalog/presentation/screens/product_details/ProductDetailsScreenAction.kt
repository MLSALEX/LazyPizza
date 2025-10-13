package com.alexmls.lazypizza.catalog.presentation.screens.product_details

sealed interface ProductDetailsScreenAction {
    data object ClickBack : ProductDetailsScreenAction
    data object ClickAddToCart : ProductDetailsScreenAction
    data class AddOne(val id: String) : ProductDetailsScreenAction
    data class Inc(val id: String) : ProductDetailsScreenAction
    data class DecOrRemove(val id: String) : ProductDetailsScreenAction
}