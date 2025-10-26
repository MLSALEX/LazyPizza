package com.alexmls.lazypizza.catalog.presentation.screens.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.domain.usecase.AddProductToCartUseCase
import com.alexmls.lazypizza.catalog.domain.usecase.ApplyToppingQtyChangeUseCase
import com.alexmls.lazypizza.catalog.domain.usecase.BuildSelectionsUseCase
import com.alexmls.lazypizza.catalog.domain.usecase.CalculateTotalUseCase
import com.alexmls.lazypizza.catalog.domain.usecase.ObserveProductDetailsUseCase
import com.alexmls.lazypizza.catalog.presentation.mapper.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProductDetailsScreenViewModel(
    private val observeDetails: ObserveProductDetailsUseCase,
    private val applyQtyChange: ApplyToppingQtyChangeUseCase,
    private val calcTotal: CalculateTotalUseCase,
    private val buildSelections: BuildSelectionsUseCase,
    private val addProductToCart: AddProductToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _events = Channel<ProductDetailsEvent>(capacity = 1)
    val events = _events.receiveAsFlow()

    private val productId: String = checkNotNull(savedStateHandle.get<String>("productId"))

    private val _state = MutableStateFlow(ProductDetailsScreenState(isLoading = true))
    val state: StateFlow<ProductDetailsScreenState> = _state

    private var domainProduct: Product? = null
    private var domainToppings: List<Topping> = emptyList()

    init {
        viewModelScope.launch {
            observeDetails(productId).collectLatest { (p, tops) ->
                domainProduct = p
                domainToppings = tops
                val uiToppings = tops.map { it.toUi() }
                val next = _state.value.copy(
                    productId = p.id,
                    title = p.name,
                    description = p.description.orEmpty(),
                    imageUrl = p.imageUrl,
                    basePriceCents = p.priceCents,
                    toppings = uiToppings,
                    isLoading = false
                )
                recalcAndSet(next)
            }
        }
    }

    fun onAction(action: ProductDetailsScreenAction) {
        when (action) {
            ProductDetailsScreenAction.ClickBack -> Unit
            ProductDetailsScreenAction.ClickAddToCart -> onAddToCartClick()
            is ProductDetailsScreenAction.AddOne      -> modify(action.id) { 1 }
            is ProductDetailsScreenAction.Inc         -> modify(action.id) { it + 1 }
            is ProductDetailsScreenAction.DecOrRemove -> modify(action.id) { it - 1 }
        }
    }

    private fun modify(id: String, f: (Int) -> Int) {
        val current = _state.value
        val nextQty = applyQtyChange(current.qty, id, f, domainToppings)
        recalcAndSet(current.copy(qty = nextQty))
    }

    private fun recalcAndSet(next: ProductDetailsScreenState) {
        val base = next.basePriceCents
        val total = calcTotal(base, domainToppings, next.qty)
        _state.value = next.copy(totalCents = total)
    }

    private fun onAddToCartClick() {
        val s = state.value
        if (s.productId.isBlank()) return
        viewModelScope.launch {
            val selections = buildSelections(s.qty, domainToppings)
            addProductToCart(
                productId = s.productId,
                selections = selections,
                quantity = 1
            )
            _events.send(ProductDetailsEvent.AddedToCart)
        }
    }
}
