package com.alexmls.lazypizza.authorization.presentation

sealed interface AuthError {
    data object InvalidPhone : AuthError
    data object WrongCode : AuthError
    data object Unknown : AuthError
}