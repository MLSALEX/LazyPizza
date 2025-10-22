package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.CartItemCard
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.EmptyCartState
import com.alexmls.lazypizza.core.designsystem.components.ButtonOverlay
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
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
    val formattedTotal = remember(state.totalCents) {
        UsdFormat.format(state.totalCents)
    }
    val totalText = stringResource(R.string.checkout_for, formattedTotal)

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
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.items,
                        key = { it.id.value }
                    ) { item ->
                        val inc = remember(item.id) { { onAction(CartAction.Inc(item.id)) } }
                        val dec = remember(item.id) { { onAction(CartAction.Dec(item.id)) } }
                        val remove = remember(item.id) { { onAction(CartAction.Remove(item.id)) } }

                        CartItemCard(
                            item = item,
                            onInc = inc,
                            onDec = dec,
                            onRemove = remove
                        )
                    }
                }

                ButtonOverlay(
                    text = totalText,
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    scrimHeight = 72.dp,
                )
            }
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