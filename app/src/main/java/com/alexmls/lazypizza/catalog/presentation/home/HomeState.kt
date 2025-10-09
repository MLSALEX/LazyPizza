package com.alexmls.lazypizza.catalog.presentation.home

import androidx.compose.runtime.Immutable
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi

@Immutable
data class HomeState(
    val title: String = "LazyPizza",
    val phone: String = "+1 (555) 321-7890",
    val isLoading: Boolean = false,
    val search: String = "",
    val selected: CategoryUi? = null, // null = All
    val categories: List<CategoryUi> = CategoryUi.entries,
    val items: List<ProductUi> = emptyList(),
    val qty: Map<String, Int> = emptyMap(),
) {
    fun qtyOf(id: String) = qty[id] ?: 0
}