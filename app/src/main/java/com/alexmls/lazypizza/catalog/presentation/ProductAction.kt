package com.alexmls.lazypizza.catalog.presentation

sealed interface ProductAction {
    data class OpenDetails(val id: String) : ProductAction
    data class Add(val id: String)        : ProductAction
    data class Inc(val id: String)        : ProductAction
    data class Dec(val id: String)        : ProductAction
    data class Remove(val id: String)     : ProductAction
}