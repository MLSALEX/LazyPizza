package com.alexmls.lazypizza.core.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.alexmls.lazypizza.R

fun openDialer(context: Context, rawPhone: String) {
    // Keep only digits and leading '+'
    val normalized = buildString {
        rawPhone.trim().forEachIndexed { i, ch ->
            if (ch.isDigit() || (i == 0 && ch == '+')) append(ch)
        }
    }
    val intent = Intent(
        Intent.ACTION_DIAL,
        "tel:$normalized".toUri()
    )
    // If you might call this from Application context, add NEW_TASK
    if (context !is android.app.Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.no_dialer_found),
            Toast.LENGTH_SHORT
        ).show()
    }
}