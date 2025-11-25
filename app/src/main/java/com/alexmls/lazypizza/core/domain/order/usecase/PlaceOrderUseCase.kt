package com.alexmls.lazypizza.core.domain.order.usecase

import com.alexmls.lazypizza.core.domain.auth.AuthStateProvider
import com.alexmls.lazypizza.core.domain.auth.requireCurrentUserId
import com.alexmls.lazypizza.core.domain.order.Order
import com.alexmls.lazypizza.core.domain.order.OrderItem
import com.alexmls.lazypizza.core.domain.order.OrderRepository
import com.alexmls.lazypizza.core.domain.order.OrderStatus
import com.alexmls.lazypizza.core.domain.order.generateOrderNumber

class PlaceOrderUseCase(
    private val orderRepository: OrderRepository,
    private val authStateProvider: AuthStateProvider,
    private val timeProvider: () -> Long = { System.currentTimeMillis() }
) {

    suspend operator fun invoke(
        pickupTimeMillis: Long,
        items: List<OrderItem>,
        totalAmountCents: Long
    ): Order {
        val userId = authStateProvider.requireCurrentUserId()
        val now = timeProvider()

        val order = Order(
            id = "",
            userId = userId,
            orderNumber = generateOrderNumber(now),
            pickupTimeMillis = pickupTimeMillis,
            items = items,
            totalAmountCents = totalAmountCents,
            status = OrderStatus.IN_PROGRESS,
            createdAtMillis = now
        )

        return orderRepository.placeOrder(order)
    }
}