package com.alexmls.lazypizza.catalog.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String,
    val priceCents: Int,        // 899 == $8.99
    val category: Category
)

enum class Category { Pizza, Drinks, Sauces, IceCream}