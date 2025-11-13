package com.alexmls.lazypizza.app.session

import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.alexmls.lazypizza.core.domain.cart.GuestSnapshotPort
import com.alexmls.lazypizza.core.domain.cart.UserSessionPort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CartSessionOrchestrator(
    private val auth: AuthRepository,
    private val guestPort: GuestSnapshotPort,
    private val userPort: UserSessionPort,
    private val scope: CoroutineScope
) {
    fun start() {
        scope.launch {
            val initial = auth.authState.value
            if (initial is AuthState.Authenticated) {
                guestPort.clearGuest()
            }

            var prev: AuthState? = null

            auth.authState.collect { cur ->
                if (prev == cur) return@collect

                when {
                    prev is AuthState.Anonymous && cur is AuthState.Authenticated -> {
                        val lines = guestPort.snapshotGuest()
                        userPort.replaceAll(lines)
                        guestPort.clearGuest()
                    }

                    prev is AuthState.Authenticated && cur is AuthState.Anonymous -> {
                        userPort.clearUser()
                        guestPort.clearGuest()
                    }
                }
                prev = cur
            }
        }
    }
}
