package com.alexmls.lazypizza.core.domain.auth

import kotlinx.coroutines.flow.StateFlow

sealed interface AuthState {
    data object Anonymous : AuthState

    data class Authenticated(
        val userId: String,
        val phone: String?
    ) : AuthState
}

interface AuthStateProvider {
    val authState: StateFlow<AuthState>
}

interface AuthRepository: AuthStateProvider {
    suspend fun startPhoneVerification(phone: String)
    suspend fun verifyCode(code: String)
    suspend fun logout()
}

class WrongCodeException : Exception("Wrong verification code")