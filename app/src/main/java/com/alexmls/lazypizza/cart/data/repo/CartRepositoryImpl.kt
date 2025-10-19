package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.model.CartItem
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class CartRepositoryImpl : CartRepository {
    private val _count = MutableStateFlow(0)
    override suspend fun add(item: CartItem) {
        _count.update { it + item.quantity.coerceAtLeast(1) }
    }
    override fun observeCount(): Flow<Int> = _count
}