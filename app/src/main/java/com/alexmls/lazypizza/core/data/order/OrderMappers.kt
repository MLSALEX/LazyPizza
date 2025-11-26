package com.alexmls.lazypizza.core.data.order

import com.alexmls.lazypizza.core.domain.order.AddonItem
import com.alexmls.lazypizza.core.domain.order.Order
import com.alexmls.lazypizza.core.domain.order.OrderItem
import com.alexmls.lazypizza.core.domain.order.OrderStatus

fun Order.toRemote(): RemoteOrderDto =
    RemoteOrderDto(
        userId = userId,
        orderNumber = orderNumber,
        pickupTimeMillis = pickupTimeMillis,
        items = items.map { it.toRemote() },
        totalAmountCents = totalAmountCents,
        status = status.name,
        createdAtMillis = createdAtMillis
    )

fun OrderItem.toRemote(): RemoteOrderItemDto =
    RemoteOrderItemDto(
        productId = productId,
        name = name,
        quantity = quantity,
        priceCents = priceCents,
        addons = addons.map { it.toRemote() }
    )

fun AddonItem.toRemote(): RemoteAddonDto =
    RemoteAddonDto(
        id = id,
        name = name,
        priceCents = priceCents
    )

fun RemoteOrderDto.toDomain(id: String): Order =
    Order(
        id = id,
        userId = userId,
        orderNumber = orderNumber,
        pickupTimeMillis = pickupTimeMillis,
        items = items.map { it.toDomain() },
        totalAmountCents = totalAmountCents,
        status = runCatching { OrderStatus.valueOf(status) }.getOrElse { OrderStatus.IN_PROGRESS },
        createdAtMillis = createdAtMillis
    )

fun RemoteOrderItemDto.toDomain(): OrderItem =
    OrderItem(
        productId = productId,
        name = name,
        quantity = quantity,
        priceCents = priceCents,
        addons = addons.map { it.toDomain() }
    )

fun RemoteAddonDto.toDomain(): AddonItem =
    AddonItem(
        id = id,
        name = name,
        priceCents = priceCents
    )