package com.alexmls.lazypizza.core.domain.catalog

import kotlinx.coroutines.flow.Flow

data class Addon(
    val id: String,
    val name: String,
    val imageUrl: String,
    val priceCents: Int
)

interface CatalogReadApi {
    /** sauces + drinks */
    fun observeAddons(): Flow<List<Addon>>
}