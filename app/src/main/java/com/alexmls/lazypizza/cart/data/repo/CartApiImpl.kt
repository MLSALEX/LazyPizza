package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class CartApiImpl(
    private val repo: CartRepository
) : CartWriteApi, CartReadApi {

    override suspend fun addToCart(payload: AddToCartPayload) {
        repo.add(payload)
    }
    override suspend fun setQuantity(productId: String, quantity: Int) {
        val id = CartLineId("$productId|")
        repo.setQty(id, quantity)
    }
    override suspend fun removeProduct(productId: String) {
        val id = CartLineId("$productId|")
        repo.remove(id)
    }
    override fun observeCount(): Flow<Int> = repo.observeCount()
    override fun observeQuantities(): Flow<Map<String, Int>> =
        repo.observeCart()
            .map { lines ->
                lines
                    .groupBy { it.productId }
                    .mapValues { (_, list) -> list.sumOf { it.qty } }
            }
            .distinctUntilChanged()
}