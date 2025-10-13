package com.alexmls.lazypizza.catalog.presentation.preview

import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi

object PreviewToppings {
    private const val DEFAULT_MAX_UNITS = 3
    val all: List<ToppingUi> = listOf(
        ToppingUi(
            id = "top_bacon",
            name = "Bacon",
            priceCents = 100,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_extra_cheese",
            name = "Extra Cheese",
            priceCents = 100,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_corn",
            name = "Corn",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_tomato",
            name = "Tomato",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_olives",
            name = "Olives",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_pepperoni",
            name = "Pepperoni",
            priceCents = 100,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_mushrooms",
            name = "Mushrooms",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_basil",
            name = "Basil",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_pineapple",
            name = "Pineapple",
            priceCents = 100,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_onion",
            name = "Onion",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_chili_peppers",
            name = "Chili Peppers",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        ),
        ToppingUi(
            id = "top_spinach",
            name = "Spinach",
            priceCents = 50,
            imageUrl = "",
            maxUnits = DEFAULT_MAX_UNITS
        )
    )
}
