package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.LocalLayoutType
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.pillShape
import com.alexmls.lazypizza.core.designsystem.theme.typography
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class PickupTimeMode {
    Earliest,
    Scheduled
}

@Composable
fun PickupTimeSection(
    selectedMode: PickupTimeMode,
    earliestTime: String,
    scheduledDate: LocalDate?,
    scheduledTime: LocalTime?,
    onModeChange: (PickupTimeMode) -> Unit,
    modifier: Modifier = Modifier,
    layout: LayoutType = LocalLayoutType.current
) {
    val onModeChangeState by rememberUpdatedState(onModeChange)

    val onEarliestClick = remember(onModeChangeState) {
        { onModeChangeState(PickupTimeMode.Earliest) }
    }
    val onScheduledModeClick = remember(onModeChangeState) {
        { onModeChangeState(PickupTimeMode.Scheduled) }
    }
    val pickupTimeLabel = remember(
        selectedMode,
        earliestTime,
        scheduledDate,
        scheduledTime
    ) {
        when (selectedMode) {
            PickupTimeMode.Earliest -> earliestTime

            PickupTimeMode.Scheduled -> {
                if (scheduledDate != null && scheduledTime != null) {
                    formatPickupLabel(
                        date = scheduledDate,
                        time = scheduledTime
                    )
                } else {
                    earliestTime
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.pickup_time),
            style = typography().labelMedium,
            color = color().secondary
        )

        TwoColumnLayout(
            layout = layout,
            modifier = Modifier.fillMaxWidth(),
            horizontalSpacing = 8.dp,
            left = {
                PickupTimeOption(
                    title = stringResource(R.string.earliest_available_time),
                    selected = selectedMode == PickupTimeMode.Earliest,
                    onClick = onEarliestClick
                )
            },
            right = {
                PickupTimeOption(
                    title = stringResource(R.string.schedule_time),
                    selected = selectedMode == PickupTimeMode.Scheduled,
                    onClick = onScheduledModeClick
                )
            }
        )

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
                text = pickupTimeLabel,
                style = typography().labelMedium,
                color = color().onPrimaryContainer
            )
        }
    }
}
private fun formatPickupLabel(
    date: LocalDate,
    time: LocalTime
): String {
    val today = LocalDate.now()

    val timePart = time.format(DateTimeFormatter.ofPattern("HH:mm"))

    return if (date == today) {
        timePart
    } else {
        val datePart = date.format(
            DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
        )
        "$datePart, $timePart"
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
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