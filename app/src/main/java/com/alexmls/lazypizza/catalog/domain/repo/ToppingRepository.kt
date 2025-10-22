package com.alexmls.lazypizza.catalog.domain.repo

import com.alexmls.lazypizza.catalog.domain.model.Topping
import kotlinx.coroutines.flow.Flow

interface ToppingRepository {
    fun observeToppings(): Flow<List<Topping>>
    suspend fun getByIds(ids: Collection<String>): List<Topping>
}