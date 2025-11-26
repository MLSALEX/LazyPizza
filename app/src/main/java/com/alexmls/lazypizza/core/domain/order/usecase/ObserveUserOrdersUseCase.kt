package com.alexmls.lazypizza.core.domain.order.usecase

import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.requireCurrentUserId
import com.alexmls.lazypizza.core.domain.order.Order
import com.alexmls.lazypizza.core.domain.order.OrderRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserOrdersUseCase(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        val userId = authRepository.requireCurrentUserId()
        return orderRepository.observeOrdersForUser(userId)
    }
}