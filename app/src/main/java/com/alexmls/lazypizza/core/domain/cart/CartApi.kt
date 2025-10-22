package com.alexmls.lazypizza.core.domain.cart

import kotlinx.coroutines.flow.Flow

data class ToppingEntry(
    val id: String,
    val name: String,
    val unitPriceCents: Int,
    val units: Int
)

data class AddToCartPayload(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val basePriceCents: Int,
    val toppings: List<ToppingEntry>,
    val quantity: Int = 1
)

/** Write-side of Cart */
interface CartWriteApi {
    suspend fun addToCart(payload: AddToCartPayload)
}

/** Read-side of Cart (for badges, etc.) */
interface CartReadApi {
    fun observeCount(): Flow<Int>
}