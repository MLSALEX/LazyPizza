package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.Topping

class ApplyToppingQtyChangeUseCase {

    operator fun invoke(
        currentQty: Map<String, Int>,
        id: String,
        transform: (Int) -> Int,
        toppings: List<Topping>
    ): Map<String, Int> {
        val old = currentQty[id] ?: 0
        val newRequested = transform(old)
        val max = toppings.firstOrNull { it.id == id }?.maxUnits ?: Int.MAX_VALUE
        val clamped = newRequested.coerceIn(0, max)

        return if (clamped == 0) currentQty - id else currentQty + (id to clamped)
    }
}