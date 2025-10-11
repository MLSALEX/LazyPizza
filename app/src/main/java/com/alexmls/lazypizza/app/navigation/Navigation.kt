package com.alexmls.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeRoot
import kotlinx.serialization.Serializable

sealed interface NavigationRoute  {
    @Serializable
    data object Home : NavigationRoute

    @Serializable
    data class ProductDetails(val id: String): NavigationRoute
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
                onNavigateToDetails = { productId ->
                    navController.navigate(NavigationRoute.ProductDetails(productId))
                }
            )
        }
        composable<NavigationRoute.ProductDetails> { entry ->
            val args = entry.toRoute<NavigationRoute.ProductDetails>()
//            ProductDetailsRoot(
//                productId = args.id,
//                onBack = { navController.navigateUp() }
//            )
        }
    }
}