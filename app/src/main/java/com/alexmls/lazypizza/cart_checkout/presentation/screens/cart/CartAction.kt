package com.alexmls.lazypizza.cart_checkout.presentation.screens.cart

import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi

sealed interface CartAction {
    data object ClickBackToMenu : CartAction
    data class Inc(val id: String) : CartAction
    data class Dec(val id: String) : CartAction
    data class Remove(val id: String) : CartAction
    data class AddRecommended(val addon: AddonUi) : CartAction
    data object Checkout : CartAction
}