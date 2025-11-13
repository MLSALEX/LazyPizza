package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.data.local.GuestCartDao
import com.alexmls.lazypizza.cart.data.local.GuestCartLineEntity
import com.alexmls.lazypizza.cart.data.local.mappers.toDomain
import com.alexmls.lazypizza.cart.data.local.mappers.toGuestEntity
import com.alexmls.lazypizza.cart.domain.buildId
import com.alexmls.lazypizza.cart.domain.model.CartLine
import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.GuestSnapshotPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class GuestCartRepository(
    private val dao: GuestCartDao,
) : CartRepository, GuestSnapshotPort {

    override fun observeCart(): Flow<List<CartLine>> =
        dao.observe().map { it.map(GuestCartLineEntity::toDomain) }

    override fun observeCount(): Flow<Int> =
        observeCart().map { it.sumOf(CartLine::qty) }.distinctUntilChanged()

    override suspend fun add(payload: AddToCartPayload) {
        val id = buildId(payload.productId, payload.toppings)
        val existing = dao.snapshot().firstOrNull { it.id == id.value }
        val newQty = (existing?.qty ?: 0) + payload.quantity.coerceAtLeast(1)
        dao.upsert(toGuestEntity(id, payload, newQty))
    }

    override suspend fun setQty(id: CartLineId, qty: Int) {
        if (qty <= 0) dao.delete(id.value) else dao.setQty(id.value, qty)
    }

    override suspend fun remove(id: CartLineId) {
        dao.delete(id.value)
    }

    override suspend fun clearGuest() = dao.clearAll()
    override suspend fun snapshotGuest(): List<CartLine> =
        dao.snapshot().map { it.toDomain() }
}