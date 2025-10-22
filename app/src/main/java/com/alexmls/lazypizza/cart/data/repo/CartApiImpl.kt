package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import kotlinx.coroutines.flow.Flow

internal class CartApiImpl(
    private val repo: CartRepository
) : CartWriteApi, CartReadApi {

    override suspend fun addToCart(payload: AddToCartPayload) {
        repo.add(payload)
    }
    override fun observeCount(): Flow<Int> = repo.observeCount()
}