package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.cart_checkout.domain.model.TimeValidationResult
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeMode
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.time_formatter.TimeInputState
import java.time.LocalDate
import java.time.LocalTime

@Immutable
data class CheckoutState(
    val cart: CartState = CartState(),
    val pickupMode: PickupTimeMode = PickupTimeMode.Earliest,
    val earliestPickupTime: String = "",
    val isOrderDetailsExpanded: Boolean = false,

    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val timeValidation: TimeValidationResult? = null,
    val isDateDialogVisible: Boolean = false,
    val isTimeDialogVisible: Boolean = false,

    val timeInput: TimeInputState = TimeInputState.from(
        LocalTime.now().withSecond(0).withNano(0)
    ),
    val isPickupTimeConfirmed: Boolean = false,
)