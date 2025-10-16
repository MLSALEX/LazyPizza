package com.alexmls.lazypizza.app.navigation.utils

import androidx.navigation.NavHostController
import com.alexmls.lazypizza.app.navigation.NavDestination
import com.alexmls.lazypizza.app.navigation.NavTab

fun navigateToTab(navController: NavHostController, tab: NavTab) {
    navController.navigate(
        when (tab) {
            NavTab.Menu -> NavDestination.Menu
            NavTab.Cart -> NavDestination.Cart
            NavTab.History -> NavDestination.History
        }
    ) {
        popUpTo(navController.graph.startDestinationId) { inclusive = false }
        launchSingleTop = true
        restoreState = false
    }
}
