package com.alexmls.lazypizza.core.domain.cart

import kotlinx.coroutines.flow.Flow

data class AddToCartRequest(
    val productId: String,
    val toppings: Map<String, Int>,
    val quantity: Int
)

/** Write-side of Cart */
interface CartWriteApi {
    suspend fun addToCart(req: AddToCartRequest)
}

/** Read-side of Cart (for badges, etc.) */
interface CartReadApi {
    fun observeCount(): Flow<Int>
}