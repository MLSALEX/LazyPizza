package com.alexmls.lazypizza.catalog.domain.usecase

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
        val product = productRepo.getById(productId)
            ?: error("Product not found: $productId")

        val ids = selections.map { it.id }
        val toppingsById = toppingRepo.getByIds(ids).associateBy { it.id }

        val entries = selections.mapNotNull { sel ->
            val t = toppingsById[sel.id] ?: return@mapNotNull null
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