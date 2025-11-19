package com.alexmls.lazypizza.cart_checkout.domain.model

sealed interface TimeValidationResult {
    data object Ok : TimeValidationResult
    data object OutsideWorkingHours : TimeValidationResult
    data object TooEarlyFromNow : TimeValidationResult
}