package com.alexmls.lazypizza.authorization.presentation.helpers

private const val MAX_PHONE_DIGITS = 11

internal fun String.toPhoneInput(): String = buildString {
    var digitsCount = 0

    this@toPhoneInput.forEachIndexed { index, ch ->
        when {
            ch == '+' && index == 0 -> append(ch)
            ch.isDigit() && digitsCount < MAX_PHONE_DIGITS-> {
                append(ch)
                digitsCount++
            }
        }
    }
}

internal fun String.isPhoneComplete(): Boolean {
    if (!startsWith("+373")) return false
    val digits = count(Char::isDigit)
    return digits == 11
}