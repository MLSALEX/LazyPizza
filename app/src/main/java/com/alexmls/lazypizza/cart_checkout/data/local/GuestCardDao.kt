package com.alexmls.lazypizza.cart_checkout.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GuestCartDao {
    @Query("SELECT * FROM guest_cart_lines")
    fun observe(): Flow<List<GuestCartLineEntity>>

    @Query("SELECT * FROM guest_cart_lines")
    suspend fun snapshot(): List<GuestCartLineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: GuestCartLineEntity)

    @Query("UPDATE guest_cart_lines SET qty=:qty WHERE id=:id")
    suspend fun setQty(id: String, qty: Int)

    @Query("DELETE FROM guest_cart_lines WHERE id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM guest_cart_lines")
    suspend fun clearAll()
}