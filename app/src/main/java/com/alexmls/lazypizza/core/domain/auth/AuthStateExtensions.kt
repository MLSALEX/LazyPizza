package com.alexmls.lazypizza.core.domain.auth

class NotAuthenticatedException : IllegalStateException("User is not authenticated")

fun AuthStateProvider.requireCurrentUserId(): String {
    return when (val state = authState.value) {
        is AuthState.Authenticated -> state.userId
        AuthState.Anonymous -> throw NotAuthenticatedException()
    }
}