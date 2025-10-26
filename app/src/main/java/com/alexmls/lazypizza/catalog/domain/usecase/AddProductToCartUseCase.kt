package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.MissingProductException
import com.alexmls.lazypizza.catalog.domain.MissingToppingsException
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import com.alexmls.lazypizza.core.domain.cart.ToppingEntry

data class ToppingSelection(val id: String, val units: Int)

class AddProductToCartUseCase(
    private val productRepo: ProductRepository,
    private val toppingRepo: ToppingRepository,
    private val cart: CartWriteApi
) {
    suspend operator fun invoke(
        productId: String,
        selections: List<ToppingSelection>,
        quantity: Int = 1
    ) {
        require(productId.isNotBlank()) { "productId must not be blank" }
        require(quantity > 0) { "quantity must be > 0" }

        // Merge duplicates and drop non-positive units
        val merged = selections
            .groupBy { it.id }
            .map { (id, list) -> ToppingSelection(id, list.sumOf { it.units }) }
            .filter { it.units > 0 }

        val product = productRepo.getById(productId)
            ?: throw MissingProductException(productId)

        val ids = merged.map { it.id }.distinct().sorted()
        val toppings = toppingRepo.getByIds(ids)
        val foundById = toppings.associateBy { it.id }

        val missing = ids.filterNot(foundById::containsKey)
        if (missing.isNotEmpty()) throw MissingToppingsException(missing)

        val entries = merged
            .sortedBy { it.id }
            .map { sel ->
                val t = foundById.getValue(sel.id)
                ToppingEntry(
                    id = t.id,
                    name = t.name,
                    unitPriceCents = t.priceCents,
                    units = sel.units
                )
            }

        val payload = AddToCartPayload(
            productId = product.id,
            productName = product.name,
            imageUrl = product.imageUrl,
            basePriceCents = product.priceCents,
            toppings = entries,
            quantity = quantity
        )

        cart.addToCart(payload)
    }
}