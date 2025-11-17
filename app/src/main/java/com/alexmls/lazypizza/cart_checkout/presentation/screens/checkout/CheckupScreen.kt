package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.OrderDetailsSection
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeSection
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckupRoot(
    viewModel: CheckupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CheckupScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CheckupScreen(
    state: CheckupState,
    onAction: (CheckupAction) -> Unit,
) {
    Column( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        PickupTimeSection(
            selectedMode = state.pickupMode,
            earliestTime = state.earliestPickupTime,
            onModeChange = { mode ->
                onAction(CheckupAction.ChangePickupMode(mode))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        HorizontalDivider()
        OrderDetailsSection(
            items = state.cart.items,
            expanded = state.isOrderDetailsExpanded,
            onToggleExpanded = { onAction(CheckupAction.ToggleOrderDetails) },
            onIncItem = { id -> onAction(CheckupAction.IncItem(id)) },
            onDecItem = { id -> onAction(CheckupAction.DecItem(id)) },
            onRemoveItem = { id -> onAction(CheckupAction.RemoveItem(id)) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider()
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CheckupScreen(
            state = CheckupState(
                isOrderDetailsExpanded = true
            ),
            onAction = {}
        )
    }
}