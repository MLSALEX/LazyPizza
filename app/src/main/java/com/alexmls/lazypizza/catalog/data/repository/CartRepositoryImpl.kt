package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.domain.model.CartItem
import com.alexmls.lazypizza.catalog.domain.repo.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CartRepositoryImpl : CartRepository {
    private val _count = MutableStateFlow(0)
    override suspend fun add(item: CartItem) {
        _count.update { it + item.quantity.coerceAtLeast(1) }
    }
    override fun observeCount(): Flow<Int> = _count
}