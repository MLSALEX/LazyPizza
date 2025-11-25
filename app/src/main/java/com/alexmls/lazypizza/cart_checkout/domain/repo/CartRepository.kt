package com.alexmls.lazypizza.cart_checkout.domain.repo

import com.alexmls.lazypizza.cart_checkout.domain.model.CartLine
import com.alexmls.lazypizza.cart_checkout.domain.model.CartLineId
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import kotlinx.coroutines.flow.Flow

internal interface CartRepository {
    suspend fun add(payload: AddToCartPayload)
    fun observeCount(): Flow<Int>
    fun observeCart(): Flow<List<CartLine>>
    suspend fun setQty(id: CartLineId, qty: Int)
    suspend fun remove(id: CartLineId)
    suspend fun clear()
}