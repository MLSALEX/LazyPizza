package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.data.remote.FirestoreToppingDataSource
import com.alexmls.lazypizza.catalog.data.remote.toDomain
import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToppingRepositoryImpl(
    private val remote: FirestoreToppingDataSource
) : ToppingRepository {
    override fun observeToppings(): Flow<List<Topping>> =
        remote.observeToppings().map { list ->
            list.mapNotNull { it.toDomain() }
        }
}