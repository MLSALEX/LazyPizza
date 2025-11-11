package com.alexmls.lazypizza.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.alexmls.lazypizza.core.designsystem.components.NavBar
import com.alexmls.lazypizza.core.designsystem.components.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.history.presentation.components.OrderCard
import com.alexmls.lazypizza.history.presentation.components.OrderHistoryEmptyState
import com.alexmls.lazypizza.history.presentation.components.OrderHistoryUnauthorizedState
import com.alexmls.lazypizza.history.presentation.ui_model.OrderCardUiModel
import com.alexmls.lazypizza.history.presentation.ui_model.OrderStatusUi
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.orders,
                        key = { it.orderNumber }
                    ) { order ->
                        OrderCard(
                            order = order,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "History – Filled", showBackground = true)
@Composable
private fun HistoryFilledPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(
                isAuthorized = true,
                isLoading = false,
                orders = listOf(
                    OrderCardUiModel(
                        orderNumber = "#12347",
                        dateTimeText = "September 25, 12:15",
                        items = listOf("1 x Margherita"),
                        totalAmountText = "$8.99",
                        status = OrderStatusUi.InProgress
                    ),
                    OrderCardUiModel(
                        orderNumber = "#12346",
                        dateTimeText = "September 25, 12:15",
                        items = listOf(
                            "1 x Margherita",
                            "2 x Pepsi",
                            "2 x Cookies Ice Cream"
                        ),
                        totalAmountText = "$25.45",
                        status = OrderStatusUi.Completed
                    ),
                    OrderCardUiModel(
                        orderNumber = "#12345",
                        dateTimeText = "September 25, 12:15",
                        items = listOf("1 x Margherita"),
                        totalAmountText = "$11.78",
                        status = OrderStatusUi.Completed
                    )
                )
            ),
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