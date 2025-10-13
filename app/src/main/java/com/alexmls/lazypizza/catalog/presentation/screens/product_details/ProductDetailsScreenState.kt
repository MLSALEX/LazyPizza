package com.alexmls.lazypizza.catalog.presentation.screens.product_details

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi

@Immutable
data class ProductDetailsScreenState(
    val productId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val basePriceCents: Int = 0,
    val toppings: List<ToppingUi> = emptyList(),
    val qty: Map<String, Int> = emptyMap(), // toppingId -> qty
    val totalCents: Int = 0,
    val isLoading: Boolean = false
){
    fun qtyOf(id: String) = qty[id] ?: 0
}