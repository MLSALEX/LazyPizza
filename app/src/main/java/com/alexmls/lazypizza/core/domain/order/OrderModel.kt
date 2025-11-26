package com.alexmls.lazypizza.core.domain.order

data class Order(
    val id: String,
    val userId: String,
    val orderNumber: String,     // "12347"
    val pickupTimeMillis: Long,
    val items: List<OrderItem>,
    val totalAmountCents: Long,
    val status: OrderStatus,
    val createdAtMillis: Long
)

data class OrderItem(
    val productId: String,
    val name: String,
    val quantity: Int,
    val priceCents: Long,
    val addons: List<AddonItem>
)

data class AddonItem(
    val id: String,
    val name: String,
    val priceCents: Long
)

enum class OrderStatus {
    IN_PROGRESS,
    COMPLETED
}