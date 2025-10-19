package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CartEvent {
    data object NavigateToMenu : CartEvent
}