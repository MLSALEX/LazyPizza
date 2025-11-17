package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeMode

sealed interface CheckupAction {
    data class ChangePickupMode(val mode: PickupTimeMode) : CheckupAction

    data object ToggleOrderDetails : CheckupAction

    data class IncItem(val id: String) : CheckupAction
    data class DecItem(val id: String) : CheckupAction
    data class RemoveItem(val id: String) : CheckupAction
}