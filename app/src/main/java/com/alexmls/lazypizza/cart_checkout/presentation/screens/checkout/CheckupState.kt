package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeMode

data class CheckupState(
    val cart: CartState = CartState(),
    val pickupMode: PickupTimeMode = PickupTimeMode.Earliest,
    val earliestPickupTime: String = "12:15",
    val isOrderDetailsExpanded: Boolean = false,
)