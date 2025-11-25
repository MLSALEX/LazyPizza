package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

sealed interface CheckoutEvent {
    data class ShowError(val message: String) : CheckoutEvent
}