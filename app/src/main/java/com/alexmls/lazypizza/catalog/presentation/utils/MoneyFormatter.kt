package com.alexmls.lazypizza.catalog.presentation.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class UsdMoneyFormatter(
    private val locale: Locale = Locale.US
) : MoneyFormatter {

    private val df: DecimalFormat = (NumberFormat.getCurrencyInstance(locale) as DecimalFormat).apply {
        currency = Currency.getInstance("USD")
        val s = decimalFormatSymbols.also { it.currencySymbol = "$" }
        decimalFormatSymbols = s
        applyPattern("¤#,##0.00")
    }

    override fun format(cents: Int): String = df.format(cents / 100.0)
}

fun interface MoneyFormatter {
    /** cents -> formatted string, e.g. "8.99 $" */
    fun format(cents: Int): String
}