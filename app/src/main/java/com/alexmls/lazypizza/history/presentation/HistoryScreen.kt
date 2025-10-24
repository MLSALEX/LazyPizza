package com.alexmls.lazypizza.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.components.NavBar
import com.alexmls.lazypizza.core.designsystem.components.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.history.presentation.components.OrderHistoryUnauthorizedState

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        NavBar(
            config = NavBarConfig.TitleCenter(stringResource(R.string.order_history)),
            modifier = Modifier.fillMaxWidth()
        )

        if (!state.isAuthorized) {
            OrderHistoryUnauthorizedState(
                onSignIn = onSignIn,
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
        HistoryScreen(
            state = HistoryState(),
            onAction = {}
        )
    }
}