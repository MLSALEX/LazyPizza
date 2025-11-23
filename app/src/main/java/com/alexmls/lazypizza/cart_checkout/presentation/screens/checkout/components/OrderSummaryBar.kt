package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.labelLargeSemiBold
import com.alexmls.lazypizza.core.designsystem.theme.typography

@Composable
fun OrderSummaryBar(
    totalFormatted: String,
    onPlaceOrder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.order_total),
                style = typography().labelLarge,
                color = color().secondary
            )
            Text(
                text = totalFormatted,
                style = MaterialTheme.typography.labelLargeSemiBold,
                color = color().onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LpPrimaryButton(
            text = stringResource(R.string.place_order),
            onClick = onPlaceOrder,
            modifier = Modifier.fillMaxWidth()
        )
    }
}