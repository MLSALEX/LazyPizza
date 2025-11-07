package com.alexmls.lazypizza.authorization.presentation.helpers

internal fun String.toPhoneInput(): String = buildString {
    this@toPhoneInput.forEachIndexed { index, ch ->
        when {
            ch == '+' && index == 0 -> append(ch)
            ch.isDigit() -> append(ch)
        }
    }
}

internal fun String.isPhoneComplete(): Boolean {
    if (!startsWith("+373")) return false
    val digits = count(Char::isDigit)
    return digits == 11
}