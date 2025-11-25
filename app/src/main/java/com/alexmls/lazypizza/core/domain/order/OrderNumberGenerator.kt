package com.alexmls.lazypizza.core.domain.order

fun generateOrderNumber(
    nowMillis: Long = System.currentTimeMillis()
): String {
    val lastFiveDigits = (nowMillis % 100_000).toInt()
    return lastFiveDigits.toString().padStart(5, '0')
}