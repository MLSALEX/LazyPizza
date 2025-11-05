package com.alexmls.lazypizza.authentication.data

import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.alexmls.lazypizza.core.domain.auth.WrongCodeException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthRepository : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Anonymous)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var pendingPhone: String? = null

    override suspend fun startPhoneVerification(phone: String) {
        require(phone.isNotBlank()) { "phone must not be blank" }
        pendingPhone = phone
    }

    override suspend fun verifyCode(code: String) {
        if (code != TEST_CODE) {
            throw WrongCodeException()
        }

        val phone = pendingPhone

        _authState.value = AuthState.Authenticated(
            userId = "fake-user-${phone.orEmpty()}",
            phone = phone
        )
    }

    override suspend fun logout() {
        pendingPhone = null
        _authState.value = AuthState.Anonymous
    }

    companion object {
        const val TEST_CODE = "789456"
    }
}