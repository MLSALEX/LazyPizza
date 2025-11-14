package com.alexmls.lazypizza.authorization.presentation

sealed interface AuthEvent {
    data object NavigateBackSignedIn : AuthEvent
    data object ContinueWithoutSignIn : AuthEvent
}