package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.helper.formatPickerHeadline
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.helper.toLocalDate
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.typography
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    isDateSelectable: (LocalDate) -> Boolean,
    modifier: Modifier = Modifier
) {
    val zoneId = remember { ZoneId.systemDefault() }

    val selectableDates = remember(isDateSelectable, zoneId) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = utcTimeMillis.toLocalDate(zoneId)
                return isDateSelectable(date)
            }
        }
    }

    val pickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DatePickerDialogContent(
            pickerState = pickerState,
            zoneId = zoneId,
            onDismissRequest = onDismissRequest,
            onDateSelected = onDateSelected,
            modifier = modifier
                .width(360.dp)
                .wrapContentHeight()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialogContent(
    pickerState: DatePickerState,
    zoneId: ZoneId,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDate = pickerState.selectedDateMillis?.toLocalDate(zoneId)
    val headlineText = selectedDate?.let(::formatPickerHeadline).orEmpty()
    val isOkEnabled = selectedDate != null

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color().surface
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_date),
                style = typography().labelMedium,
                color = color().secondary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(4.dp))

            if (headlineText.isNotEmpty()) {
                Text(
                    text = headlineText,
                    style = typography().titleLarge,
                    color = color().onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()

            DatePicker(
                state = pickerState,
                modifier = Modifier.fillMaxWidth(),
                showModeToggle = false,
                title = null,
                headline = null,
                colors = DatePickerDefaults.colors(
                    containerColor = color().surface,
                    headlineContentColor = color().onPrimaryContainer,
                    titleContentColor = color().secondary,
                    weekdayContentColor = color().secondary,
                    selectedDayContainerColor = color().primary,
                    selectedDayContentColor = color().onPrimary,
                    todayDateBorderColor = color().primary,
                    todayContentColor = color().onPrimaryContainer,
                    disabledDayContentColor = color().onSurfaceVariant.copy(alpha = 0.3f)
                )
            )

            LpDialogConfirmBar(
                isConfirmEnabled = isOkEnabled,
                onDismiss = onDismissRequest,
                onConfirm = {
                    val date = selectedDate ?: return@LpDialogConfirmBar
                    onDateSelected(date)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(locale = "en")
@Composable
private fun PickupDatePickerDialogPreview() {
    LazyPizzaTheme {
        val zoneId = remember { ZoneId.systemDefault() }

        val selectableDates = remember(zoneId) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = utcTimeMillis.toLocalDate(zoneId)
                    return date >= LocalDate.now()
                }
            }
        }

        val initialSelectedDateMillis = remember(zoneId) {
            LocalDate.now()
                .plusDays(2)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()
        }

        val pickerState = rememberDatePickerState(
            selectableDates = selectableDates,
            initialSelectedDateMillis = initialSelectedDateMillis
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DatePickerDialogContent(
                pickerState = pickerState,
                zoneId = zoneId,
                onDismissRequest = {},
                onDateSelected = {}
            )
        }
    }
}