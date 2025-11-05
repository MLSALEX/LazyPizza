package com.alexmls.lazypizza.authorization.presentation

data class AuthUiState(
    val phone: String = "",
    val code: String = "",
    val step: Step = Step.Phone,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    enum class Step { Phone, Code }
}