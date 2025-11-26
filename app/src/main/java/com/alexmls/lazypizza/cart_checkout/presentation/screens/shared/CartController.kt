package com.alexmls.lazypizza.cart_checkout.presentation.screens.shared

import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import kotlinx.coroutines.flow.StateFlow

internal interface CartController {
    val state: StateFlow<CartState>

    suspend fun inc(id: String)
    suspend fun dec(id: String)
    suspend fun remove(id: String)
    suspend fun addAddon(addon: AddonUi)

    suspend fun clear()
}