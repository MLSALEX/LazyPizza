package com.alexmls.lazypizza.history.presentation

import com.alexmls.lazypizza.history.presentation.ui_model.OrderCardUiModel

data class HistoryState(
    val isAuthorized: Boolean = false,
    val isLoading: Boolean = false,
    val orders: List<OrderCardUiModel> = emptyList()
)