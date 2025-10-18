package com.alexmls.lazypizza.catalog.domain.repo

import com.alexmls.lazypizza.catalog.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun add(item: CartItem)
    fun observeCount(): Flow<Int>
}