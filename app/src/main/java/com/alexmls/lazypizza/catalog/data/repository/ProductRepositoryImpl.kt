package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.data.remote.FirestoreProductDataSource
import com.alexmls.lazypizza.catalog.data.remote.toDomainOrNull
import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProductRepositoryImpl(
    private val remote: FirestoreProductDataSource,
    appScope: CoroutineScope
) : ProductRepository {
    private val cache: StateFlow<List<Product>> =
        remote.observeProducts()
            .map { list -> list.mapNotNull { it.toDomainOrNull() } }
            .catch { emit(emptyList()) }
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    override fun observeProducts(): Flow<List<Product>> = cache

    override suspend fun getById(id: String): Product? =
        cache.first().firstOrNull { it.id == id }
}