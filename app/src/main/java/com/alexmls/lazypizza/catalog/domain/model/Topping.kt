package com.alexmls.lazypizza.catalog.domain.model

data class Topping(
    val id: String,
    val name: String,
    val priceCents: Int,
    val imageUrl: String,
    val maxUnits: Int = 3             // business rule: 1..3 recommended
)
