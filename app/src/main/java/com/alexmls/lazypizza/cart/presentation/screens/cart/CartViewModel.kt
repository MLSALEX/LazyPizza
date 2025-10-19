package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
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
        repo.observeCount()
            .map { count -> CartState(itemCount = count) }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartState()
            )

    private val _events = Channel<CartEvent>(capacity = Channel.BUFFERED)
    val events: Flow<CartEvent> = _events.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.ClickBackToMenu -> {
                viewModelScope.launch { _events.send(CartEvent.NavigateToMenu) }
            }
        }
    }
}