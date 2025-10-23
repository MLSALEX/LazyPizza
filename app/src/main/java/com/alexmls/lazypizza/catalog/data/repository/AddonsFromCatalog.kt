package com.alexmls.lazypizza.catalog.data.repository

import com.alexmls.lazypizza.catalog.domain.model.Category
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.core.domain.catalog.Addon
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddonsFromCatalog(
    private val productRepo: ProductRepository
) : CatalogReadApi {

    private val addonCategories = setOf(Category.Sauces, Category.Drinks)

    override fun observeAddons(): Flow<List<Addon>> =
        productRepo.observeProducts()
            .map { products ->
                products.asSequence()
                    .filter { it.category in addonCategories }
                    .map { p ->
                        Addon(
                            id = p.id,
                            name = p.name,
                            imageUrl = p.imageUrl,
                            priceCents = p.priceCents
                        )
                    }
                    .toList()
            }
}