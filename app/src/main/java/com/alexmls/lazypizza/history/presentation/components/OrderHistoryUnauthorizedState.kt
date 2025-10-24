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
fun OrderHistoryUnauthorizedState(
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onClickUpToDate by rememberUpdatedState(onSignIn)

    EmptyState(
        title = stringResource(R.string.not_signed_in),
        subtitle = stringResource(R.string.please_sign_in_to_view_your_order_history),
        modifier = Modifier.fillMaxSize(),
        action = {
            LpPrimaryButton(
                text = stringResource(R.string.sign_in),
                onClick = onClickUpToDate,
                modifier = Modifier.wrapContentWidth(),
                height = 44.dp,
                textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
            )
        }
    )
}
