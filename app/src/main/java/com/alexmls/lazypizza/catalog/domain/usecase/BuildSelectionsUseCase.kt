package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.Topping


class BuildSelectionsUseCase {
    operator fun invoke(
        qty: Map<String, Int>,
        toppings: List<Topping>
    ): List<ToppingSelection> {
        val maxById = toppings.associate { it.id to it.maxUnits }
        return qty.mapNotNull { (id, u) ->
            val max = maxById[id] ?: Int.MAX_VALUE
            val clamped = u.coerceIn(0, max)
            if (clamped > 0) ToppingSelection(id, clamped) else null
        }
    }
}