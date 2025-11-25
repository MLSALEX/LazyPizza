package com.alexmls.lazypizza.cart_checkout.presentation.screens.shared

import com.alexmls.lazypizza.cart_checkout.domain.model.CartLineId
import com.alexmls.lazypizza.cart_checkout.domain.repo.CartRepository
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart_checkout.presentation.mapper.toUi
import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class CartControllerImpl(
    private val repo: CartRepository,
    private val observeAddons: ObserveRecommendedAddonsUseCase,
    scope: CoroutineScope
) : CartController {

    private val shuffleSeed = System.nanoTime()

    private val _state = MutableStateFlow(CartState())
    override val state: StateFlow<CartState> = _state.asStateFlow()

    init {
        combine(
            repo.observeCart()
                .map { lines ->
                    val ui = lines.map { it.toUi() }   // CartLine.toUi(): OrderItemUi
                    ui to ui.sumOf { it.totalCents }
                },
            observeAddons()
                .map { it.shuffled(kotlin.random.Random(shuffleSeed)) }
                .map { list -> list.map { it.toUi() } }
        ) { (items, total), addons ->
            CartState(
                items = items,
                totalCents = total,
                addons = addons
            )
        }
            .onEach { _state.value = it }
            .launchIn(scope)
    }

    override suspend fun inc(id: String) {
        val item = _state.value.items.firstOrNull { it.id == id } ?: return
        repo.setQty(
            CartLineId(id),
            item.qty + 1
        )
    }

    override suspend fun dec(id: String) {
        val item = _state.value.items.firstOrNull { it.id == id } ?: return
        if (item.qty > 1) {
            repo.setQty(
                CartLineId(id),
                item.qty - 1
            )
        }
    }

    override suspend fun remove(id: String) {
        repo.remove(CartLineId(id))
    }

    override suspend fun addAddon(addon: AddonUi) {
        repo.add(
            AddToCartPayload(
                productId = addon.id,
                productName = addon.name,
                imageUrl = addon.imageUrl,
                basePriceCents = addon.priceCents,
                toppings = emptyList(),
                quantity = 1
            )
        )
    }
    override suspend fun clear() {
        repo.clear()
    }
}