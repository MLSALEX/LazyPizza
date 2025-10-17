package com.alexmls.lazypizza.app.navigation.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavHostController
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.app.navigation.NavDestination
import com.alexmls.lazypizza.app.navigation.NavTab


fun navigateToTab(navController: NavHostController, tab: NavTab) {
    val dest = when (tab) {
            NavTab.Menu -> NavDestination.Menu
            NavTab.Cart -> NavDestination.Cart
            NavTab.History -> NavDestination.History
        }
    navController.navigate(dest) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
        restoreState = false
    }
}

@StringRes
fun NavTab.labelRes(): Int = when (this) {
    NavTab.Menu    -> R.string.tab_menu
    NavTab.Cart    -> R.string.tab_cart
    NavTab.History -> R.string.tab_history
}

@DrawableRes
fun NavTab.iconRes(): Int = when (this) {
    NavTab.Menu    -> R.drawable.menu
    NavTab.Cart    -> R.drawable.cart
    NavTab.History -> R.drawable.history
}