package com.alexmls.lazypizza.authorization.presentation

sealed interface AuthAction {
    data class PhoneChanged(val value: String) : AuthAction
    data object ContinueClicked : AuthAction

    data class CodeChanged(val value: String) : AuthAction
    data object ConfirmClicked : AuthAction

    data object ContinueAsGuestClicked : AuthAction
    data object ResendClicked : AuthAction
}