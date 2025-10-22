package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.data.remote.FirestoreToppingDataSource
import com.alexmls.lazypizza.catalog.data.remote.toDomain
import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ToppingRepositoryImpl(
    private val remote: FirestoreToppingDataSource,
    appScope: CoroutineScope
) : ToppingRepository {

    private val cache: StateFlow<List<Topping>> =
        remote.observeToppings()
            .map { list -> list.mapNotNull { it.toDomain() } }
            .catch { emit(emptyList()) }
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    override fun observeToppings(): Flow<List<Topping>> = cache

    override suspend fun getByIds(ids: Collection<String>): List<Topping> {
        val set = ids.toSet()
        return cache.first().filter { it.id in set }
    }
}