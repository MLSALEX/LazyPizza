package com.alexmls.lazypizza.catalog.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.presentation.mapper.toUi
import com.alexmls.lazypizza.catalog.presentation.selector.buildSectionStartIndex
import com.alexmls.lazypizza.catalog.presentation.selector.buildSections
import com.alexmls.lazypizza.catalog.presentation.selector.filterItems
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
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
        val searchFlow = _state
            .map { it.search }
            .distinctUntilChanged()
            .debounce(150)

        val itemsFlow = productRepository.observeProducts()
            .map { it.map { d -> d.toUi() } }

        val qtyFlow = cartRead.observeQuantities()

        viewModelScope.launch {
            combine(itemsFlow, qtyFlow, searchFlow) { itemsUi, qtyMap, query ->
                // compute derived fields once per change
                val filtered = filterItems(itemsUi, query)
                val sections = buildSections(filtered, qtyMap)
                val sectionStart = buildSectionStartIndex(sections)
                _state.value.copy(
                    isLoading = false,
                    items = itemsUi,
                    qty = qtyMap,
                    sections = sections,
                    sectionStart = sectionStart,
                    isEmptyAfterFilter = filtered.isEmpty()
                )
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
        }
    }
    private fun onSetQty(productId: String, qty: Int) {
        viewModelScope.launch {
            cartWrite.setQuantity(productId, qty) // 0 = delete
        }
    }

    private fun sendEvent(e: HomeEvent) = viewModelScope.launch {
        _events.send(e)
    }
}