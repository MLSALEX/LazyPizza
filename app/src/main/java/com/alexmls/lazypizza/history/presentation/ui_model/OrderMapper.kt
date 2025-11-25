package com.alexmls.lazypizza.history.presentation.ui_model

import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.domain.order.Order
import com.alexmls.lazypizza.core.domain.order.OrderItem
import com.alexmls.lazypizza.core.domain.order.OrderStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val orderDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d, HH:mm")

fun Order.toCardUiModel(): OrderCardUiModel {
    val dateTime = Instant.ofEpochMilli(createdAtMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(orderDateFormatter)

    val itemLines: List<String> = items.map { it.toItemLine() }

    val totalText = UsdFormat.format(totalAmountCents.toInt())

    val statusUi = when (status) {
        OrderStatus.IN_PROGRESS -> OrderStatusUi.InProgress
        OrderStatus.COMPLETED   -> OrderStatusUi.Completed
    }
    return OrderCardUiModel(
        orderNumber = "Order #$orderNumber",
        dateTimeText = dateTime,
        items = itemLines,
        totalAmountText = totalText,
        status = statusUi
    )
}

private fun OrderItem.toItemLine(): String = buildString {
    append("$quantity × $name")
    if (addons.isNotEmpty()) {
        append(" (+${addons.size} add-ons)")
    }
}