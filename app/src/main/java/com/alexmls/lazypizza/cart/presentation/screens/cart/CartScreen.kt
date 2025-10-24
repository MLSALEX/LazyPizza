package com.alexmls.lazypizza.cart.presentation.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import com.alexmls.lazypizza.cart.domain.model.CartLineId
import com.alexmls.lazypizza.cart.presentation.model.AddonUi
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.CartItemCard
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.EmptyCartState
import com.alexmls.lazypizza.cart.presentation.screens.cart.components.RecommendedAddonsRow
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.Adaptive
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.LocalLayoutType
import com.alexmls.lazypizza.core.designsystem.components.ButtonOverlay
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton
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
    layout: LayoutType = LocalLayoutType.current
) {
    val backToMenu by rememberUpdatedState { onAction(CartAction.ClickBackToMenu) }
    val formattedTotal = remember(state.totalCents) {
        UsdFormat.format(state.totalCents)
    }
    val totalText = stringResource(R.string.checkout_for, formattedTotal)
    val addonUpToDate = rememberUpdatedState<(AddonUi) -> Unit> { a ->
        onAction(CartAction.AddRecommended(a))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp)
    ) {
        NavBar(
            config = NavBarConfig.TitleCenter(stringResource(R.string.cart)),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.isEmpty) {
            EmptyCartState(
                onBackToMenu = backToMenu,
                modifier = Modifier.fillMaxSize()
            )
            return@Column
        }
        Adaptive(
            layout = layout,
            mobile = {
                CartMobile(
                    state = state,
                    totalText = totalText,
                    onAction = onAction,
                    onAddon = addonUpToDate.value
                )
            },
            wide = {
                CartWide(
                    state = state,
                    totalText = totalText,
                    onAction = onAction,
                    onAddAddon = addonUpToDate.value
                )
            }
        )
    }
}

@Composable
private fun CartMobile(
    state: CartState,
    totalText: String,
    onAction: (CartAction) -> Unit,
    onAddon: (AddonUi) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ){
                CartItemsList(
                    items = state.items,
                    onInc = { id -> onAction(CartAction.Inc(id)) },
                    onDec = { id -> onAction(CartAction.Dec(id)) },
                    onRemove = { id -> onAction(CartAction.Remove(id)) },
                    modifier = Modifier
                )
            }

            RecommendedAddonsRow(
                items = state.addons,
                onAddClick = onAddon,
                modifier = Modifier.padding(bottom = 70.dp)
            )
        }
        ButtonOverlay(
            text = totalText,
            onClick = { onAction(CartAction.Checkout) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(end = 16.dp),
            scrimHeight = 72.dp,
        )
    }
}
@Composable
private fun CartWide(
    state: CartState,
    totalText: String,
    onAction: (CartAction) -> Unit,
    onAddAddon: (AddonUi) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CartItemsList(
            items = state.items,
            onInc = { id -> onAction(CartAction.Inc(id)) },
            onDec = { id -> onAction(CartAction.Dec(id)) },
            onRemove = { id -> onAction(CartAction.Remove(id)) },
            modifier = Modifier
                .width(350.dp)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    bottomStart = 16.dp
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp)) {
                    RecommendedAddonsRow(
                        items = state.addons,
                        onAddClick = onAddAddon,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    LpPrimaryButton(
                        text = totalText,
                        onClick = { onAction(CartAction.Checkout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(end = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemsList(
    items: List<CartItemUi>,
    onInc: (CartLineId) -> Unit,
    onDec: (CartLineId) -> Unit,
    onRemove: (CartLineId) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 60.dp, top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.id.value }
        ) { item ->
            val inc = remember(item.id) { { onInc(item.id)} }
            val dec = remember(item.id) { { onDec(item.id)  } }
            val remove = remember(item.id) { { onRemove(item.id) } }

            CartItemCard(
                item = item,
                onInc = inc,
                onDec = dec,
                onRemove = remove
            )
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