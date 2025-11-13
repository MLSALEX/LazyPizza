package com.alexmls.lazypizza.cart.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guest_cart_lines")
data class GuestCartLineEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val productName: String,
    val imageUrl: String?,
    val unitPriceCents: Int,
    val qty: Int,
    val toppings: String
)
