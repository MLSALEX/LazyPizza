package com.alexmls.lazypizza.cart_checkout.data.local.mappers

import com.alexmls.lazypizza.cart_checkout.data.local.GuestCartLineEntity
import com.alexmls.lazypizza.cart_checkout.domain.model.CartLine
import com.alexmls.lazypizza.cart_checkout.domain.model.CartLineId
import com.alexmls.lazypizza.cart_checkout.domain.model.CartTopping
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload

// Guest entity -> domain
internal fun GuestCartLineEntity.toDomain(): CartLine =
    CartLine(
        id = CartLineId(id),
        productId = productId,
        name = productName,
        imageUrl = imageUrl.orEmpty(),
        qty = qty,
        unitPriceCents = unitPriceCents,
        toppings = toppings
            .split("+")
            .filter { it.isNotBlank() }
            .map { pair ->
                val (tid, u) = pair.split("×")
                CartTopping(name = tid, units = u.toInt())
            }
    )

// AddToCartPayload -> Guest entity
internal fun toGuestEntity(id: CartLineId, p: AddToCartPayload, qty: Int): GuestCartLineEntity =
    GuestCartLineEntity(
        id = id.value,
        productId = p.productId,
        productName = p.productName,
        imageUrl = p.imageUrl,
        unitPriceCents = p.basePriceCents + p.toppings.sumOf { it.unitPriceCents * it.units },
        qty = qty,
        toppings = p.toppings
            .sortedBy { it.id }
            .joinToString("+") { "${it.id}×${it.units}" }
    )