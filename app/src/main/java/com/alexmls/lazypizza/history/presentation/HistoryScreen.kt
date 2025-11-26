package com.alexmls.lazypizza.history.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.Adaptive
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.LocalLayoutType
import com.alexmls.lazypizza.core.designsystem.components.top_bars.NavBar
import com.alexmls.lazypizza.core.designsystem.components.top_bars.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.history.presentation.components.OrderCard
import com.alexmls.lazypizza.history.presentation.components.OrderHistoryEmptyState
import com.alexmls.lazypizza.history.presentation.components.OrderHistoryUnauthorizedState
import com.alexmls.lazypizza.history.presentation.preview.HistoryDemoData
import com.alexmls.lazypizza.history.presentation.ui_model.OrderCardUiModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    onNavigateToAuth: () -> Unit,
    onNavigateToMenu: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                HistoryEvent.NavigateToAuth -> onNavigateToAuth()
                HistoryEvent.NavigateToMenu -> onNavigateToMenu()
            }
        }
    }

    HistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
    layout: LayoutType = LocalLayoutType.current
) {
    val onSignIn by rememberUpdatedState { onAction(HistoryAction.ClickSignIn) }
    val onGoToMenu by rememberUpdatedState { onAction(HistoryAction.ClickGoToMenu) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        NavBar(
            config = NavBarConfig.TitleCenter(stringResource(R.string.order_history)),
            modifier = Modifier.fillMaxWidth()
        )

        when {
            !state.isAuthorized -> {
                OrderHistoryUnauthorizedState(
                    onSignIn = onSignIn,
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.orders.isEmpty() -> {
                OrderHistoryEmptyState(
                    onGoToMenu = onGoToMenu,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Adaptive(
                    layout = layout,
                    mobile = {
                        HistoryListMobile(
                            orders = state.orders,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    wide = {
                        HistoryGridWide(
                            orders = state.orders,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )
            }
        }
    }
}
@Composable
private fun HistoryListMobile(
    orders: List<OrderCardUiModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = orders,
            key = { it.orderNumber }
        ) { order ->
            OrderCard(
                order = order,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryGridWide(
    orders: List<OrderCardUiModel>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(
            items = orders,
            key = { it.orderNumber }
        ) { order ->
            OrderCard(
                order = order,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "History – Filled", showBackground = true)
@Composable
private fun HistoryFilledPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryDemoData.filledState(),
            onAction = {}
        )
    }
}

@Preview(name = "History – Empty", showBackground = true)
@Composable
private fun HistoryEmptyPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isAuthorized = true,
                isLoading = false,
                orders = emptyList()
            ),
            onAction = {}
        )
    }
}

@Preview(name = "History – Unauthorized", showBackground = true)
@Composable
private fun HistoryUnauthorizedPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isAuthorized = false
            ),
            onAction = {}
        )
    }
}

@Preview(
    name = "History – Filled – Wide",
    showBackground = true,
    widthDp = 1024,
    heightDp = 600)
@Composable
private fun HistoryFilledWidePreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryDemoData.filledState(),
            onAction = {},
            layout = LayoutType.Wide
        )
    }
}