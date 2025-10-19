package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.model.CartItem
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartRequest
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import kotlinx.coroutines.flow.Flow

internal class CartApiImpl(
    private val repo: CartRepository
) : CartWriteApi, CartReadApi {

    override suspend fun addToCart(req: AddToCartRequest) {
        val item = CartItem(req.productId, req.toppings, req.quantity)
        repo.add(item)
    }
    override fun observeCount(): Flow<Int> = repo.observeCount()
}