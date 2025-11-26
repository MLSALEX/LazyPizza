package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.cart_checkout.domain.model.TimeValidationResult
import com.alexmls.lazypizza.cart_checkout.domain.repo.CartRepository
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ValidatePickupDateTimeUseCase
import com.alexmls.lazypizza.cart_checkout.presentation.mapper.toDomain
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartState
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components.PickupTimeMode
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.time_formatter.TimeInputFormatter
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartController
import com.alexmls.lazypizza.cart_checkout.presentation.screens.shared.CartControllerImpl
import com.alexmls.lazypizza.core.domain.auth.NotAuthenticatedException
import com.alexmls.lazypizza.core.domain.order.OrderItem
import com.alexmls.lazypizza.core.domain.order.usecase.PlaceOrderUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CheckoutViewModel internal constructor(
    private val repo: CartRepository,
    private val observeAddons: ObserveRecommendedAddonsUseCase,
    private val validatePickupDateTime: ValidatePickupDateTimeUseCase,
    private val placeOrder: PlaceOrderUseCase,
) : ViewModel() {

    private val controller: CartController = CartControllerImpl(
        repo = repo,
        observeAddons = observeAddons,
        scope = viewModelScope
    )
    private val local = MutableStateFlow(
        CheckoutState(
            cart = CartState(),
            earliestPickupTime = computeEarliestPickupTime()
        )
    )
    private val _events = Channel<CheckoutEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    val state: StateFlow<CheckoutState> =
        combine(
            controller.state,
            local
        ) { cartState, checkup ->
            checkup.copy(cart = cartState)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CheckoutState()
            )

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.ChangePickupMode -> handleChangePickupMode(action)

            CheckoutAction.ToggleOrderDetails ->
                local.update { it.copy(isOrderDetailsExpanded = !it.isOrderDetailsExpanded) }

            is CheckoutAction.IncItem ->
                launch { controller.inc(action.id) }
            is CheckoutAction.DecItem ->
                launch { controller.dec(action.id) }
            is CheckoutAction.RemoveItem ->
                launch { controller.remove(action.id) }
            is CheckoutAction.AddRecommended ->
                launch { controller.addAddon(action.addon) }

            CheckoutAction.DismissDatePicker -> handleDateCancelClicked()
            is CheckoutAction.DateSelected -> handleDateSelected(action)

            is CheckoutAction.TimeHourChanged   -> handleTimeHourChanged(action)
            is CheckoutAction.TimeMinuteChanged -> handleTimeMinuteChanged(action)
            CheckoutAction.TimeOkClicked        -> handleTimeOkClicked()
            CheckoutAction.TimeCancelClicked    -> handleTimeCancelClicked()

            is CheckoutAction.CommentChanged ->
                local.update { it.copy(comment = action.text) }

            CheckoutAction.SubmitOrder -> handleSubmitOrder()
        }
    }

    private fun handleSubmitOrder() {
        val current = state.value

        if (current.cart.isEmpty) {
            return
        }

        val pickupTimeMillis = computePickupTimeMillis(current) ?: return

        val orderItems: List<OrderItem> = current.cart.items.map { uiItem ->
            uiItem.toDomain()
        }

        val totalAmountCents: Long = current.cart.totalCents.toLong()

        viewModelScope.launch {
            try {
                val order = placeOrder(
                    pickupTimeMillis = pickupTimeMillis,
                    items = orderItems,
                    totalAmountCents = totalAmountCents
                )

                controller.clear()

                local.update { localState ->
                    localState.copy(
                        isOrderConfirmed = true,
                        orderNumber = order.orderNumber,
                        confirmationPickupTime = computeConfirmationPickupTime(localState),
                        isDateDialogVisible = false,
                        isTimeDialogVisible = false
                    )
                }
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "Failed to place order", e)
                val message = when (e) {
                    is NotAuthenticatedException -> "Please sign in to place your order"
                    else -> "Something went wrong. Please try again"
                }
                _events.send(
                    CheckoutEvent.ShowError(message)
                )
            }
        }
    }

    private fun handleDateSelected(action: CheckoutAction.DateSelected) {
        local.update { state ->
            state.copy(
                draftDate = action.date,
                timeValidation = null,
                isDateDialogVisible = false,
                isTimeDialogVisible = true
            )
        }
    }

    private fun handleChangePickupMode(action: CheckoutAction.ChangePickupMode) {
        local.update { state ->
            val newMode = action.mode
            when (newMode) {
                PickupTimeMode.Earliest -> state.copy(
                    pickupMode = PickupTimeMode.Earliest,
                    isDateDialogVisible = false,
                    isTimeDialogVisible = false,
                    selectedDate = null,
                    selectedTime = null,
                    timeValidation = null,
                    isPickupTimeConfirmed = false,
                    earliestPickupTime = computeEarliestPickupTime()
                )

                PickupTimeMode.Scheduled -> state.copy(
                    pickupMode = PickupTimeMode.Scheduled,
                    isDateDialogVisible = true
                )
            }
        }
    }
    private fun handleTimeHourChanged(action: CheckoutAction.TimeHourChanged) {
        local.update { state ->
            state.copy(
                timeInput = TimeInputFormatter.onHourChanged(
                    state.timeInput,
                    action.value
                ),
                timeValidation = null
            )
        }
    }
    private fun handleTimeMinuteChanged(action: CheckoutAction.TimeMinuteChanged) {
        local.update { state ->
            state.copy(
                timeInput = TimeInputFormatter.onMinuteChanged(
                    state.timeInput,
                    action.value
                ),
                timeValidation = null
            )
        }
    }

    private fun handleTimeOkClicked() {
        local.update { state ->
            val date = state.draftDate ?: state.selectedDate ?: return@update state
            val time = state.timeInput.parsedTime ?: return@update state

            val validation = validatePickupDateTime.validate(date, time)

            when (validation) {
                TimeValidationResult.Ok -> state.copy(
                    selectedDate = date,
                    selectedTime = time,
                    draftDate = null,
                    timeValidation = validation,
                    isTimeDialogVisible = false,
                    pickupMode = PickupTimeMode.Scheduled,
                    isPickupTimeConfirmed = true
                )

                TimeValidationResult.OutsideWorkingHours,
                TimeValidationResult.TooEarlyFromNow -> state.copy(
                    timeValidation = validation
                )

                null -> state
            }
        }
    }

    private fun handleTimeCancelClicked() {
        local.update { state ->
            if (!state.isPickupTimeConfirmed) {
                resetToEarliest(state)
            } else {
                state.copy(
                    isTimeDialogVisible = false,
                    draftDate = null
                )
            }
        }
    }

    private fun resetToEarliest(state: CheckoutState): CheckoutState =
        state.copy(
            pickupMode = PickupTimeMode.Earliest,
            isDateDialogVisible = false,
            isTimeDialogVisible = false,
            selectedDate = null,
            selectedTime = null,
            timeValidation = null,
            isPickupTimeConfirmed = false,
            earliestPickupTime = computeEarliestPickupTime()
        )

    private fun handleDateCancelClicked() {
        local.update { state ->
            if (!state.isPickupTimeConfirmed) {
                resetToEarliest(state)
            } else {
                state.copy(
                    isDateDialogVisible = false,
                    draftDate = null
                )
            }
        }
    }

    fun isDateSelectable(date: LocalDate): Boolean =
        validatePickupDateTime.isDateSelectable(date)

    private inline fun launch(crossinline block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    private fun computeEarliestPickupTime(): String {
        val earliest = validatePickupDateTime.earliestAvailableDateTime()
        val time = earliest.toLocalTime()
        return time.format(TIME_ONLY_FORMATTER)
    }

    private fun computePickupTimeMillis(state: CheckoutState): Long? {
        val dateTime = resolvePickupDateTime(state) ?: return null

        return dateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun computeConfirmationPickupTime(state: CheckoutState): String {
        val dateTime = resolvePickupDateTime(state)

        return if (dateTime != null) {
            dateTime.format(CONFIRMATION_FORMATTER)
        } else {
            state.earliestPickupTime
        }
    }

    private fun resolvePickupDateTime(state: CheckoutState): LocalDateTime? {
        return when (state.pickupMode) {
            PickupTimeMode.Earliest -> {
                validatePickupDateTime.earliestAvailableDateTime()
            }

            PickupTimeMode.Scheduled -> {
                val date = state.selectedDate
                val time = state.selectedTime
                if (date == null || time == null) return null

                LocalDateTime.of(date, time)
            }
        }
    }
    companion object {
        private val TIME_ONLY_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm")

        private val CONFIRMATION_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("MMMM d, HH:mm")
    }
}