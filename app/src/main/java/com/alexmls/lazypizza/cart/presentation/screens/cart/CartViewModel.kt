package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel internal constructor(
    private val repo: CartRepository
) : ViewModel() {

    val state: StateFlow<CartState> =
        repo.observeCart()
            .map { lines ->
                val ui = lines.map { line ->
                    CartItemUi(
                        id = line.id,
                        name = line.name,
                        imageUrl = line.imageUrl,
                        qty = line.qty,
                        unitPriceCents = line.unitPriceCents,
                        extrasLines = line.toppings.map { t ->
                            if (t.units == 1) "1 × ${t.name}" else "${t.units} × ${t.name}"
                        }
                    )
                }
                CartState(
                    items = ui,
                    totalCents = ui.sumOf { it.totalCents }
                )
            }
            .distinctUntilChanged()
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
            is CartAction.Inc -> inc(action.id)
            is CartAction.Dec -> dec(action.id)
            is CartAction.Remove -> remove(action.id)
        }
    }
    private fun navigateToMenu() = viewModelScope.launch {
        _events.send(CartEvent.NavigateToMenu)
    }

    private fun inc(id: CartLineId) = viewModelScope.launch {
        lineById(id)?.let { repo.setQty(id, it.qty + 1) }
    }

    private fun dec(id: CartLineId) = viewModelScope.launch {
        lineById(id)?.let { if (it.qty > 1) repo.setQty(id, it.qty - 1) }
    }

    private fun remove(id: CartLineId) = viewModelScope.launch {
        repo.remove(id)
    }

    private fun lineById(id: CartLineId): CartItemUi? =
        state.value.items.firstOrNull { it.id == id }
}