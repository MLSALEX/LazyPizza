package com.alexmls.lazypizza.cart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [GuestCartLineEntity::class],
    exportSchema = true
)
abstract class CartDatabase : RoomDatabase() {
    abstract fun guestCartDao(): GuestCartDao

    companion object {
        const val NAME = "cart.db"
    }
}