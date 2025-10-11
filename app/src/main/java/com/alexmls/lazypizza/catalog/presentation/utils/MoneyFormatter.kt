package com.alexmls.lazypizza.catalog.presentation.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object UsdFormat {
    private fun DecimalFormat.applyUsd() = apply {
        currency = Currency.getInstance("USD")
        decimalFormatSymbols = decimalFormatSymbols.also { it.currencySymbol = "$" }
    }

    private val df0: DecimalFormat =
        (NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat)
            .applyUsd()
            .apply {
                minimumFractionDigits = 0
                maximumFractionDigits = 0
            }

    private val df2: DecimalFormat =
        (NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat)
            .applyUsd()
            .apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }

    /** cents -> "$1" or "$8.99" */
    fun format(cents: Int): String {
        val amount = cents / 100.0
        return if (cents % 100 == 0) df0.format(amount) else df2.format(amount)
    }
}