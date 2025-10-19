package com.alexmls.lazypizza.cart.presentation.screens.cart

sealed interface CartAction {
    data object ClickBackToMenu : CartAction
}