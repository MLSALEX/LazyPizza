package com.alexmls.lazypizza.catalog.presentation.screens.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import com.alexmls.lazypizza.catalog.presentation.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProductDetailsScreenViewModel(
    private val productRepo: ProductRepository,
    private val toppingRepo: ToppingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle.get<String>("productId"))

    private val _state = MutableStateFlow(ProductDetailsScreenState(isLoading = true))
    val state: StateFlow<ProductDetailsScreenState> = _state

    init {
        viewModelScope.launch {
            combine(
                productRepo.observeProducts().map { list -> list.firstOrNull { it.id == productId } },
                toppingRepo.observeToppings()
            ) { product, toppings -> product to toppings }
                .collectLatest { (p, tops) ->
                    val uiToppings = tops.map { it.toUi() }
                    recalcAndSet(
                        _state.value.copy(
                            productId = p?.id.orEmpty(),
                            title = p?.name.orEmpty(),
                            description = p?.description.orEmpty(),
                            imageUrl = p?.imageUrl.orEmpty(),
                            basePriceCents = p?.priceCents ?: 0,
                            toppings = uiToppings,
                            isLoading = false
                        )
                    )
                }
        }
    }

    fun onAction(action: ProductDetailsScreenAction) {
        when (action) {
            ProductDetailsScreenAction.ClickBack -> Unit // handled by host
            ProductDetailsScreenAction.ClickAddToCart -> Unit // send event in real app
            is ProductDetailsScreenAction.AddOne -> modify(action.id) { 1 }
            is ProductDetailsScreenAction.Inc -> modify(action.id) { it + 1 }
            is ProductDetailsScreenAction.DecOrRemove -> modify(action.id) { n -> (n - 1).coerceAtLeast(0) }
        }
    }
    private fun computeTotal(state: ProductDetailsScreenState): Int {
        val priceById = state.toppings.associate { it.id to it.priceCents }
        val extras = state.qty.entries.sumOf { (id, q) -> (priceById[id] ?: 0) * q }
        return state.basePriceCents + extras
    }

    private fun recalcAndSet(next: ProductDetailsScreenState) {
        _state.value = next.copy(totalCents = computeTotal(next))
    }
    private fun modify(id: String, f: (Int) -> Int) {
        val current = _state.value
        val old = current.qty[id] ?: 0
        val newQty = f(old)
        val nextQty = if (newQty == 0) current.qty - id else current.qty + (id to newQty)
        val next = current.copy(qty = nextQty)
        recalcAndSet(next)
    }
}
