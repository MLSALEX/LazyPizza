package com.alexmls.lazypizza.catalog.data.remote

data class RemoteTopping(
    val id: String = "",
    val name: String = "",
    val price_cents: Int = 0,
    val image_path: String? = null,   // e.g. "toppings/bacon.png"
    val image_url: String? = null,    // absolute URL wins over path
    val is_active: Boolean = true,
    val max_units: Int? = null
)