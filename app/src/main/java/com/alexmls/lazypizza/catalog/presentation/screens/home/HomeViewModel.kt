package com.alexmls.lazypizza.catalog.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.presentation.mapper.toUi
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel (
    private val productRepository: ProductRepository,
    private val cartWrite: CartWriteApi,
    private val cartRead: CartReadApi
): ViewModel() {

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                productRepository.observeProducts().map { it.map { d -> d.toUi() } },
                cartRead.observeQuantities()
            ) { itemsUi, qtyMap ->
                _state.value.copy(isLoading = false, items = itemsUi, qty = qtyMap)
            }.collect { next -> _state.value = next }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.ClickPhone -> sendEvent(HomeEvent.Dial(action.number))
            is HomeAction.SearchChanged -> _state.update { it.copy(search = action.query) }
            is HomeAction.CategorySelected -> _state.update { it.copy(selected = action.category) }

            is HomeAction.OpenDetails ->  sendEvent(HomeEvent.NavigateToDetails(action.id))

            is HomeAction.Add    -> onAddClick(action.id)
            is HomeAction.Inc    -> onSetQty(action.id, (state.value.qty[action.id] ?: 0) + 1)
            is HomeAction.Dec    -> onSetQty(action.id, ((state.value.qty[action.id] ?: 0) - 1).coerceAtLeast(0))
            is HomeAction.Remove -> onSetQty(action.id, 0)
        }
    }
    private fun onAddClick(productId: String) {
        val item = state.value.items.firstOrNull { it.id == productId } ?: return
        viewModelScope.launch {
            cartWrite.addToCart(
                AddToCartPayload(
                    productId = item.id,
                    productName = item.name,
                    imageUrl = item.imageUrl,
                    basePriceCents = item.priceCents,
                    toppings = emptyList(),
                    quantity = 1
                )
            )
            sendEvent(HomeEvent.HapticTick)
        }
    }
    private fun onSetQty(productId: String, qty: Int) {
        viewModelScope.launch {
            cartWrite.setQuantity(productId, qty) // 0 = delete
            if (qty > 0) sendEvent(HomeEvent.HapticTick)
        }
    }

    private fun sendEvent(e: HomeEvent) = viewModelScope.launch {
        _events.send(e)
    }
}