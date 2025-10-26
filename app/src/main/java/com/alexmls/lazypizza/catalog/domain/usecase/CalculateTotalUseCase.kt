package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.Topping

class CalculateTotalUseCase {
    operator fun invoke(
        basePriceCents: Int,
        toppings: List<Topping>,
        qty: Map<String, Int>
    ): Int {
        val priceById = toppings.associate { it.id to it.priceCents }
        val extras = qty.entries.sumOf { (id, units) -> (priceById[id] ?: 0) * units }
        return basePriceCents + extras
    }
}