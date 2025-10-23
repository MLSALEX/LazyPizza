package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.model.CartLine
import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.domain.model.CartTopping
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.ToppingEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class CartRepositoryImpl : CartRepository {

    private val _lines = MutableStateFlow<Map<CartLineId, CartLine>>(emptyMap())

    override suspend fun add(payload: AddToCartPayload) {
        val id = buildId(payload.productId, payload.toppings)
        val unitPrice = payload.basePriceCents +
                payload.toppings.sumOf { it.unitPriceCents * it.units }

        val extras = payload.toppings.map { CartTopping(it.name, it.units) }

        _lines.update { map ->
            val exist = map[id]
            val newQty = (exist?.qty ?: 0) + payload.quantity.coerceAtLeast(1)
            val newLine = exist?.copy(qty = newQty) ?: CartLine(
                id = id,
                productId = payload.productId,
                name = payload.productName,
                imageUrl = payload.imageUrl,
                qty = newQty,
                unitPriceCents = unitPrice,
                toppings = extras
            )
            map + (id to newLine)
        }
    }

    override fun observeCart(): Flow<List<CartLine>> =
        _lines.map { it.values.sortedBy { ln -> ln.name } }

    override fun observeCount(): Flow<Int> =
        _lines.map { it.values.sumOf { ln -> ln.qty } }

    override suspend fun setQty(id: CartLineId, qty: Int) {
        _lines.update { m ->
            when {
                qty <= 0      -> m - id
                m[id] != null -> m + (id to m.getValue(id).copy(qty = qty))
                else          -> m
            }
        }
    }

    override suspend fun remove(id: CartLineId) {
        _lines.update { it - id }
    }

    private fun buildId(productId: String, toppings: List<ToppingEntry>): CartLineId {
        val k = toppings.sortedBy { it.id }
            .joinToString("+") { entry -> "${entry.id}×${entry.units}" }
        return CartLineId("$productId|$k")
    }
}