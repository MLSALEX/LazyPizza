package com.alexmls.lazypizza.cart.presentation.screens.cart

import com.alexmls.lazypizza.cart.domain.model.CartLineId

sealed interface CartAction {
    data object ClickBackToMenu : CartAction
    data class Inc(val id: CartLineId) : CartAction
    data class Dec(val id: CartLineId) : CartAction
    data class Remove(val id: CartLineId) : CartAction
}