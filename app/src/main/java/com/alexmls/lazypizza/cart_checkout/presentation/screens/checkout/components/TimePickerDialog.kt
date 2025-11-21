package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart_checkout.domain.model.TimeValidationResult
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.time_formatter.TimeInputState
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.titleXLargeMedium
import com.alexmls.lazypizza.core.designsystem.theme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: TimeInputState,
    validationResult: TimeValidationResult?,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismiss by rememberUpdatedState(onDismissRequest)
    val confirm by rememberUpdatedState(onConfirmClick)
    val hourChange by rememberUpdatedState(onHourChange)
    val minuteChange by rememberUpdatedState(onMinuteChange)

    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        TimePickerDialogContent(
            hourText = state.hour,
            minuteText = state.minute,
            validationResult = validationResult,
            isConfirmEnabled = state.parsedTime != null,
            onHourChange = hourChange,
            onMinuteChange = minuteChange,
            onDismissRequest = dismiss,
            onConfirmClick = confirm,
            modifier = modifier
        )
    }
}
@Composable
private fun TimePickerDialogContent(
    hourText: String,
    minuteText: String,
    validationResult: TimeValidationResult?,
    isConfirmEnabled: Boolean,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hourFocusRequester = remember { FocusRequester() }
    val minuteFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        hourFocusRequester.requestFocus()
    }

    Surface(
        modifier = modifier
            .width(264.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        color = color().surface
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.select_time),
                style = typography().labelMedium,
                color = color().secondary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TimeNumberField(
                    value = hourText,
                    onValueChange = onHourChange,
                    modifier = Modifier.weight(1f),
                    focusRequester = hourFocusRequester,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        minuteFocusRequester.requestFocus()
                    }
                )

                Text(
                    text = ":",
                    style = typography().titleXLargeMedium,
                    color = color().onPrimaryContainer,
                    modifier = Modifier.padding(8.dp)
                )

                TimeNumberField(
                    value = minuteText,
                    onValueChange = onMinuteChange,
                    modifier = Modifier.weight(1f),
                    focusRequester = minuteFocusRequester,
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        if (isConfirmEnabled) onConfirmClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeNumberLabel(
                    text = stringResource(R.string.hour_label)
                )

                Spacer(Modifier.width(8.dp))

                TimeNumberLabel(
                    text = stringResource(R.string.minute_label)
                )
            }

            when (validationResult) {
                TimeValidationResult.OutsideWorkingHours -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.pickup_outside_working_hours),
                        style = typography().bodySmall,
                        color = color().primary,
                    )
                }

                TimeValidationResult.TooEarlyFromNow -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.pickup_too_early),
                        style = typography().bodySmall,
                        color = color().primary,
                    )
                }

                TimeValidationResult.Ok, null -> Unit
            }

            Spacer(Modifier.height(16.dp))

            LpDialogConfirmBar(
                isConfirmEnabled = isConfirmEnabled,
                onDismiss = onDismissRequest,
                onConfirm = onConfirmClick
            )
        }
    }
}

@Composable
private fun TimeNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: (() -> Unit)? = null
) {
    val noHandleColors = remember {
        TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.Transparent
        )
    }
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = if (isFocused) color().primary else color().outline
    val containerColor = if (isFocused) color().surface else color().surfaceVariant

    val keyboardActions = remember(imeAction, onImeAction) {
        if (onImeAction == null) {
            KeyboardActions.Default
        } else {
            when (imeAction) {
                ImeAction.Next -> KeyboardActions(onNext = { onImeAction() })
                ImeAction.Done -> KeyboardActions(onDone = { onImeAction() })
                else -> KeyboardActions.Default
            }
        }
    }
    CompositionLocalProvider(LocalTextSelectionColors provides noHandleColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = typography().titleXLargeMedium.copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            cursorBrush = SolidColor(color().secondary),
            modifier = modifier
                .width(104.dp)
                .height(72.dp)
                .then(
                    if (focusRequester != null) Modifier.focusRequester(focusRequester)
                    else Modifier
                )
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = containerColor,
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "00",
                                style = typography().titleXLargeMedium,
                                color = color().secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
private fun TimeNumberLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = typography().bodySmall,
        color = color().secondary,
        textAlign = TextAlign.Start,
        modifier = modifier.width(104.dp)
    )
}

@Preview()
@Composable
private fun TimePickerDialogPreview() {
    LazyPizzaTheme {
        TimePickerDialogContent(
            hourText = "23",
            minuteText = "00",
            validationResult = TimeValidationResult.OutsideWorkingHours,
            isConfirmEnabled = true,
            onHourChange = {},
            onMinuteChange = {},
            onDismissRequest = {},
            onConfirmClick = {}
        )
    }
}