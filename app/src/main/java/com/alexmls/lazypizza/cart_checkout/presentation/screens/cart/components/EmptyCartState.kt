package com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.components

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
fun EmptyCartState(
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onClickUpToDate by rememberUpdatedState(onBackToMenu)

    EmptyState(
        title = stringResource(R.string.cart_empty_title),
        subtitle = stringResource(R.string.cart_empty_subtitle),
        modifier = modifier,
        action = {
            LpPrimaryButton(
                text = stringResource(R.string.back_to_menu),
                onClick = onClickUpToDate,
                modifier = Modifier.wrapContentWidth(),
                height = 44.dp,
                textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
            )
        }
    )
}
