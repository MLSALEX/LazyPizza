package com.alexmls.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeRoot
import com.alexmls.lazypizza.catalog.presentation.screens.product_details.ProductDetailsRoot
import com.alexmls.lazypizza.catalog.presentation.utils.toProductDetailsRoute
import kotlinx.serialization.Serializable

sealed interface NavigationRoute  {
    @Serializable
    data object Home : NavigationRoute

    @Serializable
    data class ProductDetails(
        val productId: String
    ): NavigationRoute
}

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Home
    ) {
        composable<NavigationRoute.Home>{
            HomeRoot(
                onNavigateToDetails = { productId: String ->
                    navController.navigate(productId.toProductDetailsRoute())
                }
            )
        }
        composable<NavigationRoute.ProductDetails> { entry ->
            ProductDetailsRoot(
                onBack = { navController.navigateUp() },
                onAddToCart = { total, selected -> /* ... */ }
            )
        }
    }
}