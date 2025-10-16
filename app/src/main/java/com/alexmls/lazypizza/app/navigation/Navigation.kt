package com.alexmls.lazypizza.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeRoot
import com.alexmls.lazypizza.catalog.presentation.screens.product_details.ProductDetailsRoot
import com.alexmls.lazypizza.app.navigation.utils.toProductDetailsRoute
import kotlinx.serialization.Serializable

sealed interface NavDestination  {
    @Serializable
    data object Menu : NavDestination

    @Serializable
    data object Cart : NavDestination

    @Serializable
    data object History : NavDestination

    @Serializable
    data class ProductDetails(
        val productId: String
    ): NavDestination
}

@Immutable
data class NavigationBarState(
    val activeTab: NavTab,
    val cartCount: Int
)

@Immutable
sealed interface NavTab {
    data object Menu : NavTab
    data object Cart : NavTab
    data object History : NavTab
}

@Composable
fun Navigation(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.Menu,
        modifier = Modifier.padding(padding)
    ) {
        composable<NavDestination.Menu>{
            HomeRoot(
                onNavigateToDetails = { productId: String ->
                    navController.navigate(productId.toProductDetailsRoute())
                }
            )
        }
//        composable<NavDestination.Cart>    { CartRoot() }
//        composable<NavDestination.History> { HistoryRoot() }
        composable<NavDestination.ProductDetails> { entry ->
            ProductDetailsRoot(
                onBack = { navController.navigateUp() },
                onAddToCart = { total, selected -> /* ... */ }
            )
        }
    }
}