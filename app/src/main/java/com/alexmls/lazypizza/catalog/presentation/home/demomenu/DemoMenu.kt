package com.alexmls.lazypizza.catalog.presentation.home.demomenu

import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi

object DemoMenu {
    val all: List<ProductUi> = listOf(
        // Pizza
        ProductUi("pizza_margherita", "Margherita",
            "Tomato sauce, mozzarella, fresh basil, olive oil", 899, CategoryUi.Pizza, R.drawable.pizza_margherita),
        ProductUi("pizza_pepperoni", "Pepperoni",
            "Tomato sauce, mozzarella, pepperoni", 999, CategoryUi.Pizza, R.drawable.pizza_pepperoni),
        // Drinks
        ProductUi("drink_mineral", "Mineral Water", "", 149, CategoryUi.Drinks, R.drawable.mineral_water),
        // Sauces
        ProductUi("sauce_bbq", "BBQ Sauce", "", 79, CategoryUi.Sauces, R.drawable.sauce_bbq),
    )
}