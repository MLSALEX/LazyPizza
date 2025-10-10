package com.alexmls.lazypizza.catalog.presentation.preview

import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi

object PreviewProducts {
    val basic4: List<ProductUi> = listOf(
        // Pizza
        ProductUi(
            "pizza_margherita",
            "Margherita",
            "Tomato sauce, mozzarella, fresh basil, olive oil",
            899,
            CategoryUi.Pizza,
            imageUrl = "https://pl-coding.com/wp-content/uploads/lazypizza/pizza/margherita.png"
        ),
        ProductUi(
            "pizza_pepperoni",
            "Pepperoni",
            "Tomato sauce, mozzarella, pepperoni",
            999,
            CategoryUi.Pizza,
            imageUrl = "https://pl-coding.com/wp-content/uploads/lazypizza/pizza/pepperoni.png"
        ),
        // Drinks
        ProductUi(
            "drink_mineral",
            "Mineral Water",
            "",
            149,
            CategoryUi.Drinks,
            "https://pl-coding.com/wp-content/uploads/lazypizza/drink/mineral-water.png"
        ),
        // Sauces
        ProductUi(
            "sauce_bbq",
            "BBQ Sauce",
            "",
            79,
            CategoryUi.Sauces,
            "https://pl-coding.com/wp-content/uploads/lazypizza/sauce/bbq.png"
        ),
    )
}