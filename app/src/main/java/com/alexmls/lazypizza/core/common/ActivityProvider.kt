package com.alexmls.lazypizza.core.common

import android.app.Activity

/**
 * Simple contract to get currently resumed Activity.
 */
interface ActivityProvider {
    val currentActivity: Activity?
}