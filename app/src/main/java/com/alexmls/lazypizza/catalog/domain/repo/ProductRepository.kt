package com.alexmls.lazypizza.catalog.domain.repo


import com.alexmls.lazypizza.catalog.domain.model.Product
import kotlinx.coroutines.flow.Flow


interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
}