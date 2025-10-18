package com.alexmls.lazypizza.catalog.presentation.screens.cart

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CartEvent {
    data object NavigateToMenu : CartEvent
}