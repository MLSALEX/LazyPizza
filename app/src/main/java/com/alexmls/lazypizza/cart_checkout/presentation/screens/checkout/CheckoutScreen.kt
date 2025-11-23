package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart_checkout.presentation.components.RecommendedAddonsRow
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.CommentsSection
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.DatePickerDialog
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.OrderDetailsSection
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.OrderSummaryBar
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeSection
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.TimePickerDialog
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.components.top_bars.CheckoutTopBar
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.color
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun CheckoutRoot(
    onBackClick: () -> Unit,
    viewModel: CheckoutViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CheckoutScreen(
        state = state,
        onAction = viewModel::onAction,
        isDateSelectable = viewModel::isDateSelectable,
        onBackClick = onBackClick
    )
}

@Composable
fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    isDateSelectable: (LocalDate) -> Boolean,
    onBackClick: () -> Unit,
) {
    if (state.isDateDialogVisible) {
        DatePickerDialog(
            onDismissRequest = {
                onAction(CheckoutAction.DismissDatePicker)
            },
            onDateSelected = { date ->
                onAction(CheckoutAction.DateSelected(date))
            },
            isDateSelectable = isDateSelectable
        )
    }
    if (state.isTimeDialogVisible) {
        TimePickerDialog(
            state = state.timeInput,
            validationResult = state.timeValidation,
            onHourChange = { value ->
                onAction(CheckoutAction.TimeHourChanged(value))
            },
            onMinuteChange = { value ->
                onAction(CheckoutAction.TimeMinuteChanged(value))
            },
            onDismissRequest = {
                onAction(CheckoutAction.TimeCancelClicked)
            },
            onConfirmClick = {
                onAction(CheckoutAction.TimeOkClicked)
            }
        )
    }
    val sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
            .shadow(
                elevation = 8.dp,
                shape = sheetShape,
                clip = false
            )
            .clip(sheetShape),
        color = color().surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            CheckoutTopBar(
                title = stringResource(R.string.order_checkout),
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                PickupTimeSection(
                    selectedMode = state.pickupMode,
                    earliestTime = state.earliestPickupTime,
                    scheduledDate = state.selectedDate,
                    scheduledTime = state.selectedTime,
                    onModeChange = { mode ->
                        onAction(CheckoutAction.ChangePickupMode(mode))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                HorizontalDivider()
                OrderDetailsSection(
                    items = state.cart.items,
                    expanded = state.isOrderDetailsExpanded,
                    onToggleExpanded = { onAction(CheckoutAction.ToggleOrderDetails) },
                    onIncItem = { id -> onAction(CheckoutAction.IncItem(id)) },
                    onDecItem = { id -> onAction(CheckoutAction.DecItem(id)) },
                    onRemoveItem = { id -> onAction(CheckoutAction.RemoveItem(id)) },
                )
                HorizontalDivider()
                RecommendedAddonsRow(
                    title = stringResource(R.string.recommended_to_add_to_your_order),
                    items = state.cart.addons,
                    onAddClick = { addon ->
                        onAction(CheckoutAction.AddRecommended(addon))
                    }
                )
                HorizontalDivider()
                CommentsSection(
                    text = state.comment,
                    onTextChange = { onAction(CheckoutAction.CommentChanged(it)) }
                )
            }
            val totalFormatted = remember(state.cart.totalCents) {
                UsdFormat.format(state.cart.totalCents)
            }
            OrderSummaryBar(
                totalFormatted =  totalFormatted,
                onPlaceOrder = { onAction(CheckoutAction.SubmitOrder) }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CheckoutScreen(
            state = CheckoutState(
                isOrderDetailsExpanded = true
            ),
            onAction = {},
            isDateSelectable = { _ -> true},
            onBackClick = {}
        )
    }
}