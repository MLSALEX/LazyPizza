@file:OptIn(ExperimentalCoroutinesApi::class)

package com.alexmls.lazypizza.cart.data.repo

import com.alexmls.lazypizza.cart.domain.model.CartLine
import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.alexmls.lazypizza.core.domain.auth.AuthStateProvider
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class SwitchingCartRepository(
    private val auth: AuthStateProvider,
    private val guestRepo: GuestCartRepository,
    private val userRepo: UserSessionCartRepository,
) : CartRepository {

    private suspend fun currentAuth(): AuthState = auth.authState.first()

    override fun observeCart(): Flow<List<CartLine>> =
        auth.authState.flatMapLatest { st ->
            if (st is AuthState.Anonymous) guestRepo.observeCart() else userRepo.observeCart()
        }

    override fun observeCount(): Flow<Int> =
        observeCart().map { it.sumOf(CartLine::qty) }.distinctUntilChanged()

    override suspend fun add(payload: AddToCartPayload) {
        when (currentAuth()) {
            is AuthState.Anonymous -> guestRepo.add(payload)
            is AuthState.Authenticated -> userRepo.add(payload)
        }
    }

    override suspend fun setQty(id: CartLineId, qty: Int) {
        when (currentAuth()) {
            is AuthState.Anonymous -> guestRepo.setQty(id, qty)
            is AuthState.Authenticated -> userRepo.setQty(id, qty)
        }
    }

    override suspend fun remove(id: CartLineId) {
        when (currentAuth()) {
            is AuthState.Anonymous -> guestRepo.remove(id)
            is AuthState.Authenticated -> userRepo.remove(id)
        }
    }
}