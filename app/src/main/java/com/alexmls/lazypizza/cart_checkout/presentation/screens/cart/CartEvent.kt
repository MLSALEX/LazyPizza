package com.alexmls.lazypizza.cart_checkout.presentation.screens.cart

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CartEvent {
    data object NavigateToMenu : CartEvent
    data object NavigateToCheckup : CartEvent
}