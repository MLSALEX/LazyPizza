package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.pillShape
import com.alexmls.lazypizza.core.designsystem.theme.typography

enum class PickupTimeMode {
    Earliest,
    Scheduled
}

@Composable
fun PickupTimeSection(
    selectedMode: PickupTimeMode,
    earliestTime: String,
    onModeChange: (PickupTimeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val onModeChangeState by rememberUpdatedState(newValue = onModeChange)

    val onEarliestClick = remember(onModeChangeState) {
        { onModeChangeState(PickupTimeMode.Earliest) }
    }
    val onScheduleClick = remember(onModeChangeState) {
        { onModeChangeState(PickupTimeMode.Scheduled) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.pickup_time),
            style = typography().labelMedium,
            color = color().secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PickupTimeOption(
                title = stringResource(R.string.earliest_available_time),
                selected = selectedMode == PickupTimeMode.Earliest,
                onClick = onEarliestClick
            )

            PickupTimeOption(
                title = stringResource(R.string.schedule_time),
                selected = selectedMode == PickupTimeMode.Scheduled,
                onClick = onScheduleClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.earliest_pickup_time),
                style = typography().labelMedium,
                color = color().secondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = earliestTime,
                style = typography().labelMedium,
                color = color().onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PickupTimeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = pillShape,
        color = color().surface,
        border = BorderStroke(
            width = 1.dp,
            color = color().outline
        )
    ) {
        Row(
            modifier = Modifier
                .padding( 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                modifier = Modifier
                    .scale(0.7f)
            )
            Spacer(modifier = Modifier.width(4.dp))


            Text(
                text = title,
                style = typography().bodyMediumMedium,
                color = if (selected) {
                    color().onPrimaryContainer
                } else {
                    color().secondary
                }
            )
        }
    }
}