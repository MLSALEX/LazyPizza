package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.data.remote.FirestoreProductDataSource
import com.alexmls.lazypizza.catalog.data.remote.toDomainOrNull

import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val remote: FirestoreProductDataSource
) : ProductRepository {
    override fun observeProducts(): Flow<List<Product>> {
        return remote.observeProducts().map { remoteList ->
            remoteList.mapNotNull { it.toDomainOrNull() }}
    }
}