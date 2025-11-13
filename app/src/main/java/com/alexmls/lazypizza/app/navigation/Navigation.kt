package com.alexmls.lazypizza.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexmls.lazypizza.app.navigation.utils.navigateToTab
import com.alexmls.lazypizza.app.navigation.utils.toProductDetailsRoute
import com.alexmls.lazypizza.authorization.presentation.AuthRoot
import com.alexmls.lazypizza.cart.presentation.screens.cart.CartRoot
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeRoot
import com.alexmls.lazypizza.catalog.presentation.screens.product_details.ProductDetailsRoot
import com.alexmls.lazypizza.history.presentation.HistoryRoot
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

    @Serializable
    data object Auth : NavDestination
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
    padding: PaddingValues,
    modifier: Modifier
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
                },
                onNavigateToAuth = {
                    navController.navigate(NavDestination.Auth)
                }
            )
        }
        composable<NavDestination.Cart>    {
            CartRoot(
                onNavigateToMenu = { navigateToTab(navController, NavTab.Menu) }
            )
        }
        composable<NavDestination.History> {
            HistoryRoot(
                onNavigateToAuth = {
                    navController.navigate(NavDestination.Auth)
                },
                onNavigateToMenu = {
                    navigateToTab(navController, NavTab.Menu)
                }
            )
        }
        composable<NavDestination.ProductDetails> { entry ->
            ProductDetailsRoot(
                onBack = { navController.navigateUp() },
                onAddedToCart = { navigateToTab(navController, NavTab.Menu) }
            )
        }
        composable<NavDestination.Auth> {
            AuthRoot(
                onFinishedSignedIn = {
                    navController.popBackStack()
                },
                onFinishedGuest = {
                    navController.popBackStack()
                }
            )
        }
    }
}