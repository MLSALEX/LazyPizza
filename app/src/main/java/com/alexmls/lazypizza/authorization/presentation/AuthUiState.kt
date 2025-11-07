package com.alexmls.lazypizza.authorization.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class AuthUiState(
    val phone: String = "",
    val code: String = "",
    val step: Step = Step.Phone,
    val isLoading: Boolean = false,
    val error: AuthError? = null,
    val secondsToResend: Int? = null,
) {
    enum class Step { Phone, Code }
}