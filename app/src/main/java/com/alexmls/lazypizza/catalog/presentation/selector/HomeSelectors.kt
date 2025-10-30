package com.alexmls.lazypizza.catalog.presentation.selector

import com.alexmls.lazypizza.catalog.presentation.model.CategorySectionUi
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi
import com.alexmls.lazypizza.catalog.presentation.model.SectionItemUi
import com.alexmls.lazypizza.catalog.presentation.utils.CATEGORY_ORDER

fun filterItems(items: List<ProductUi>, query: String): List<ProductUi> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return items
    return items.asSequence().filter { it.name.lowercase().contains(q) }.toList()
}

fun buildSections(
    filtered: List<ProductUi>,
    qty: Map<String, Int>
): List<CategorySectionUi> =
    CATEGORY_ORDER.mapNotNull { cat ->
        val list = filtered
            .filter { it.category == cat }
            .map { p -> SectionItemUi(product = p, qty = qty[p.id] ?: 0) }
        if (list.isEmpty()) null else CategorySectionUi(cat, list)
    }

// for LazyColumn (header + items)
fun buildSectionStartIndex(
    sections: List<CategorySectionUi>
): Map<CategoryUi, Int> {
    var index = 0
    return buildMap {
        sections.forEach { section ->
            put(section.category, index)
            index += 1 + section.items.size
        }
    }
}