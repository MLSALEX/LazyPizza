package com.alexmls.lazypizza.cart.presentation.screens.cart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton
import com.alexmls.lazypizza.core.designsystem.theme.titleLargeMedium

@Composable
fun EmptyCartState(
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topGap = 120.dp
    val onClickUpToDate by rememberUpdatedState(onBackToMenu)
    val btnText = stringResource(R.string.back_to_menu)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = topGap)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.cart_empty_title),
                style = MaterialTheme.typography.titleLargeMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.cart_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(20.dp))

            LpPrimaryButton(
                text = btnText,
                onClick = onClickUpToDate,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 0.dp),
                height = 44.dp,
                textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
            )
        }
    }
}
