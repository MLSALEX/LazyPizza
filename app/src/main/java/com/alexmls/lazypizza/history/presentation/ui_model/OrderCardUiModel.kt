package com.alexmls.lazypizza.history.presentation.ui_model

import androidx.compose.runtime.Immutable

@Immutable
enum class OrderStatusUi {
    Completed,
    InProgress
}

@Immutable
data class OrderCardUiModel(
    val orderNumber: String,
    val dateTimeText: String,
    val items: List<String>,
    val totalAmountText: String,
    val status: OrderStatusUi
)