package com.alexmls.lazypizza.cart_checkout.presentation.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.cart_checkout.domain.repo.CartRepository
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartController
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartControllerImpl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel internal constructor(
    private val repo: CartRepository,
    private val observeAddons: ObserveRecommendedAddonsUseCase
) : ViewModel() {

    private val controller: CartController = CartControllerImpl(
        repo = repo,
        observeAddons = observeAddons,
        scope = viewModelScope
    )

    val state: StateFlow<CartState> =
        controller.state
            .map { ui ->
                CartState(
                    items = ui.items,
                    totalCents = ui.totalCents,
                    addons = ui.addons
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CartState()
            )

    private val _events = Channel<CartEvent>(capacity = Channel.BUFFERED)
    val events: Flow<CartEvent> = _events.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.ClickBackToMenu -> navigateToMenu()
            is CartAction.Inc -> launch { controller.inc(action.id) }
            is CartAction.Dec -> launch { controller.dec(action.id) }
            is CartAction.Remove -> launch { controller.remove(action.id) }
            is CartAction.AddRecommended -> launch { controller.addAddon(action.addon) }
            CartAction.Checkout -> navigateToCheckup()
        }
    }
    private fun navigateToMenu() = viewModelScope.launch {
        _events.send(CartEvent.NavigateToMenu)
    }

    private inline fun launch(crossinline block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
    private fun navigateToCheckup() = viewModelScope.launch {
        _events.send(CartEvent.NavigateToCheckup)
    }
}