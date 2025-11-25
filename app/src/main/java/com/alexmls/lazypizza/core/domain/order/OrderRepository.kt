package com.alexmls.lazypizza.core.domain.order

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(order: Order): Order

    fun observeOrdersForUser(userId: String): Flow<List<Order>>
}