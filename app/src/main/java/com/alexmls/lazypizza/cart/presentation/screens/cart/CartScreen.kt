package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.EmptyCartState
import com.alexmls.lazypizza.core.designsystem.components.NavBar
import com.alexmls.lazypizza.core.designsystem.components.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartRoot(
    onNavigateToMenu: () -> Unit,
    viewModel: CartViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onNavigateToMenuUpToDate by rememberUpdatedState(onNavigateToMenu)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CartEvent.NavigateToMenu -> onNavigateToMenuUpToDate()
            }
        }
    }
    CartScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit,
) {
    val backToMenu by rememberUpdatedState { onAction(CartAction.ClickBackToMenu) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        NavBar(
            config = NavBarConfig.TitleCenter(stringResource(R.string.cart)),
            onClick = {} ,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.isEmpty) {
            EmptyCartState(
                onBackToMenu = backToMenu,
                modifier = Modifier.fillMaxSize()
            )
        } else {

        }
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CartScreen(
            state = CartState(),
            onAction = {}
        )
    }
}