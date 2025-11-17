package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.cart_checkout.domain.repo.CartRepository
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartController
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartControllerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckupViewModel internal constructor(
    repo: CartRepository,
    observeAddons: ObserveRecommendedAddonsUseCase
) : ViewModel() {

    private val controller: CartController = CartControllerImpl(
        repo = repo,
        observeAddons = observeAddons,
        scope = viewModelScope
    )
    private val local = MutableStateFlow(
        CheckupState(
            cart = CartState()
        )
    )

    val state: StateFlow<CheckupState> =
        combine(
            controller.state,
            local
        ) { cartState, checkup ->
            checkup.copy(cart = cartState)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CheckupState()
            )

    fun onAction(action: CheckupAction) {
        when (action) {
            is CheckupAction.ChangePickupMode ->
                local.update { it.copy(pickupMode = action.mode) }

            CheckupAction.ToggleOrderDetails ->
                local.update { it.copy(isOrderDetailsExpanded = !it.isOrderDetailsExpanded) }

            is CheckupAction.IncItem ->
                launch { controller.inc(action.id) }

            is CheckupAction.DecItem ->
                launch { controller.dec(action.id) }

            is CheckupAction.RemoveItem ->
                launch { controller.remove(action.id) }
        }
    }

    private inline fun launch(crossinline block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

}