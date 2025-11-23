package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeMode
import java.time.LocalDate

sealed interface CheckoutAction {
    data class ChangePickupMode(val mode: PickupTimeMode) : CheckoutAction

    data object ToggleOrderDetails : CheckoutAction

    data class IncItem(val id: String) : CheckoutAction
    data class DecItem(val id: String) : CheckoutAction
    data class RemoveItem(val id: String) : CheckoutAction
    data class AddRecommended(val addon: AddonUi) : CheckoutAction

    data object DismissDatePicker : CheckoutAction
    data class DateSelected(val date: LocalDate) : CheckoutAction

    data class TimeHourChanged(val value: String) : CheckoutAction
    data class TimeMinuteChanged(val value: String) : CheckoutAction
    data object TimeOkClicked : CheckoutAction
    data object TimeCancelClicked : CheckoutAction
}