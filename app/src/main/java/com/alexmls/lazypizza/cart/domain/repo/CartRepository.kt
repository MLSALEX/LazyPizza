package com.alexmls.lazypizza.cart.domain.repo

import com.alexmls.lazypizza.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

internal interface CartRepository {
    suspend fun add(item: CartItem)
    fun observeCount(): Flow<Int>
}