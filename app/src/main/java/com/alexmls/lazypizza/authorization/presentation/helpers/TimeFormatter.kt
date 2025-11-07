package com.alexmls.lazypizza.authorization.presentation.helpers

internal fun Int.formatAsMmSs(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}