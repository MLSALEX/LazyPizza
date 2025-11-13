package com.alexmls.lazypizza.core.domain.cart

import com.alexmls.lazypizza.cart.domain.model.CartLine

interface GuestSnapshotPort {
    suspend fun snapshotGuest(): List<CartLine>
    suspend fun clearGuest()
}
interface UserSessionPort {
    fun replaceAll(lines: List<CartLine>)
    suspend fun clearUser()
}