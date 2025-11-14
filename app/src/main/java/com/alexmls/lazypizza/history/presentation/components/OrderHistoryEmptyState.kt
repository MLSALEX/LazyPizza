package com.alexmls.lazypizza.history.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.components.EmptyState
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton

@Composable
fun OrderHistoryEmptyState(
    onGoToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onClickGoToMenu by rememberUpdatedState(onGoToMenu)

    EmptyState(
        title = stringResource(R.string.no_orders_yet),
        subtitle = stringResource(R.string.orders_will_appear_after_first_purchase),
        modifier = modifier.fillMaxSize(),
        action = {
            LpPrimaryButton(
                text = stringResource(R.string.go_to_menu),
                onClick = onClickGoToMenu,
                modifier = Modifier.wrapContentWidth(),
                height = 44.dp,
                textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
            )
        }
    )
}