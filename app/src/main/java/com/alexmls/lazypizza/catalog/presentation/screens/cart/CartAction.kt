package com.alexmls.lazypizza.catalog.presentation.screens.cart

sealed interface CartAction {
    data object ClickBackToMenu : CartAction
}