package com.alexmls.lazypizza.core.data.order

data class RemoteOrderDto(
    val userId: String = "",
    val orderNumber: String = "",
    val pickupTimeMillis: Long = 0L,
    val items: List<RemoteOrderItemDto> = emptyList(),
    val totalAmountCents: Long = 0L,
    val status: String = "In Progress",
    val createdAtMillis: Long = 0L
)

data class RemoteOrderItemDto(
    val productId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val priceCents: Long = 0L,
    val addons: List<RemoteAddonDto> = emptyList()
)

data class RemoteAddonDto(
    val id: String = "",
    val name: String = "",
    val priceCents: Long = 0L
)