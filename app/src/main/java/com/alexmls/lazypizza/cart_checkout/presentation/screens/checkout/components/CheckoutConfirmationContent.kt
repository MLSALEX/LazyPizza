package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.labelMediumMedium
import com.alexmls.lazypizza.core.designsystem.theme.titleLargeMedium
import com.alexmls.lazypizza.core.designsystem.theme.typography

@Composable
fun CheckoutConfirmationContent(
    orderNumber: String,
    pickupTime: String,
    onBackToMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.order_confirmation_title),
                style = typography().titleLargeMedium,
                color = color().onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.order_confirmation_subtitle),
                style = typography().bodyMedium,
                color = color().secondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            ConfirmationInfoCard(
                orderNumber = orderNumber,
                pickupTime = pickupTime,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(30.dp))
            TextButton(
                onClick = onBackToMenuClick
            ) {
                Text(
                    text = stringResource(R.string.back_to_menu),
                    style = typography().titleSmall,
                    color = color().primary
                )
            }
        }
    }
}

@Composable
private fun ConfirmationInfoCard(
    orderNumber: String,
    pickupTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = color().outline,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.order_number_label),
                    style = typography().labelMediumMedium,
                    color = color().secondary
                )
                Text(
                    text = stringResource(R.string.order_number_value, orderNumber),
                    style = typography().labelMedium,
                    color = color().onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.pickup_time_label),
                    style = typography().labelMediumMedium,
                    color = color().secondary
                )
                Text(
                    text = pickupTime.uppercase(),
                    style = typography().labelMedium,
                    color = color().onPrimaryContainer
                )
            }
        }
    }
}