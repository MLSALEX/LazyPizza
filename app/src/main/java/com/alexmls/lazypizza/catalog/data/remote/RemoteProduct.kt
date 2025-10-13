package com.alexmls.lazypizza.catalog.data.remote

data class RemoteProduct(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price_cents: Int = 0,
    val category: String = "",
    val image_path: String? = null,
    val image_url: String? = null,
    val is_active: Boolean = true
)