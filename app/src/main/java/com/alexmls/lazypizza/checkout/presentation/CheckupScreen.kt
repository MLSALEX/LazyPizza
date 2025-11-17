package com.alexmls.lazypizza.checkout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.checkout.presentation.components.PickupTimeSection
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
    Column(){
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
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CheckupScreen(
            state = CheckupState(),
            onAction = {}
        )
    }
}