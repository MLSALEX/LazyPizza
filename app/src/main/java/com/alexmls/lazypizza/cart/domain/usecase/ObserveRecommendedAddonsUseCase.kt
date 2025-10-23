package com.alexmls.lazypizza.cart.domain.usecase

import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.catalog.Addon
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

internal class ObserveRecommendedAddonsUseCase(
    private val catalog: CatalogReadApi,
    private val cart: CartRepository
) {
    operator fun invoke(): Flow<List<Addon>> =
        combine(
            catalog.observeAddons(),
            cart.observeCart()
        ) { addons, cartLines ->
            val idsInCart = cartLines.map { it.productId }.toSet()
            addons.filterNot { it.id in idsInCart }
        }.distinctUntilChanged()
}