package com.alexmls.lazypizza.catalog.presentation.utils

import com.alexmls.lazypizza.catalog.presentation.model.CategorySectionUi
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import kotlin.collections.forEach

// Assumes each section contributes: 1 header item + N product items (grid counts items, not rows)
fun buildSectionStartIndex(sections: List<CategorySectionUi>): Map<CategoryUi, Int> {
    var index = 0
    val map = mutableMapOf<CategoryUi, Int>()
    sections.forEach { section ->
        map[section.category] = index
        index += 1 + section.items.size
    }
    return map
}