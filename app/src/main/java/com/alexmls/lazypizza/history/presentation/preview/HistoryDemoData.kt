package com.alexmls.lazypizza.history.presentation.preview

import com.alexmls.lazypizza.history.presentation.HistoryState
import com.alexmls.lazypizza.history.presentation.ui_model.OrderCardUiModel
import com.alexmls.lazypizza.history.presentation.ui_model.OrderStatusUi

object HistoryDemoData {

    val orders: List<OrderCardUiModel> = listOf(
        OrderCardUiModel(
            orderNumber = "#12347",
            dateTimeText = "September 25, 12:15",
            items = listOf("1 x Margherita"),
            totalAmountText = "$8.99",
            status = OrderStatusUi.InProgress
        ),
        OrderCardUiModel(
            orderNumber = "#12346",
            dateTimeText = "September 25, 12:15",
            items = listOf(
                "1 x Margherita",
                "2 x Pepsi",
                "2 x Cookies Ice Cream"
            ),
            totalAmountText = "$25.45",
            status = OrderStatusUi.Completed
        ),
        OrderCardUiModel(
            orderNumber = "#12345",
            dateTimeText = "September 25, 12:15",
            items = listOf("1 x Margherita"),
            totalAmountText = "$11.78",
            status = OrderStatusUi.Completed
        )
    )

    // Helper for "filled" history state
    fun filledState(): HistoryState = HistoryState(
        isAuthorized = true,
        isLoading = false,
        orders = orders
    )
}