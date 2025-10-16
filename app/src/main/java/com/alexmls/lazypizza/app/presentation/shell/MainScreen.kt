package com.alexmls.lazypizza.app.presentation.shell

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alexmls.lazypizza.app.navigation.NavDestination
import com.alexmls.lazypizza.app.navigation.NavTab
import com.alexmls.lazypizza.app.navigation.Navigation
import com.alexmls.lazypizza.app.navigation.NavigationBarState
import com.alexmls.lazypizza.app.navigation.utils.navigateToTab
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.rememberLayoutType
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoot(
    vm: MainViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val barState by vm.navBarState.collectAsStateWithLifecycle()

    MainScreen(
        navController = navController,
        state = barState,
        onSelectTab = vm::onTabSelected
    )
}

@Composable
fun MainScreen(
    navController: NavHostController,
    state: NavigationBarState,
    onSelectTab: (NavTab) -> Unit,
    onAction: (MainAction) -> Unit = {}
) {
    val layout = rememberLayoutType()
    val backStack by navController.currentBackStackEntryAsState()
    val showBar = backStack?.destination?.hasRoute<NavDestination.ProductDetails>() != true

    val onTabClick: (NavTab) -> Unit = remember(state.activeTab) {
        { tab ->
            if (state.activeTab != tab) {
                onSelectTab(tab)
                navigateToTab(navController, tab)
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (layout == LayoutType.Mobile && showBar)
                BottomBar(
                    state = state,
                    onSelectTab = onTabClick
                )
        },
        contentWindowInsets = WindowInsets()
    ) { padding ->
        Row(Modifier.fillMaxSize()) {
            if (layout == LayoutType.Wide && showBar)
                NavigationRail(
                    state = state,
                    onSelectTab = onTabClick
                )

            Navigation(navController, padding)
        }
    }
}
