package com.alexmls.lazypizza.checkout.presentation

import com.alexmls.lazypizza.checkout.presentation.components.PickupTimeMode

sealed interface CheckupAction {
    data class ChangePickupMode(val mode: PickupTimeMode) : CheckupAction
}