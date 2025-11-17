package com.alexmls.lazypizza.checkout.presentation

import com.alexmls.lazypizza.checkout.presentation.components.PickupTimeMode
import com.alexmls.lazypizza.core.ui_model.OrderItemUi

data class CheckupState(
    val pickupMode: PickupTimeMode = PickupTimeMode.Earliest,
    val earliestPickupTime: String = "12:15",
    val isOrderDetailsExpanded: Boolean = false,
    val items: List<OrderItemUi> = emptyList()
)