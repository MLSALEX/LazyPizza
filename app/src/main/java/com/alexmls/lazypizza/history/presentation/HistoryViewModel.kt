package com.alexmls.lazypizza.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.alexmls.lazypizza.core.domain.order.usecase.ObserveUserOrdersUseCase
import com.alexmls.lazypizza.history.presentation.ui_model.toCardUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val authRepository: AuthRepository,
    private val observeUserOrders: ObserveUserOrdersUseCase
) : ViewModel() {

    private var ordersJob: Job? = null

    private val _events = Channel<HistoryEvent>(Channel.BUFFERED)
    val events: Flow<HistoryEvent> = _events.receiveAsFlow()

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState()
        )

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState
                .collect { auth ->
                    val isAuthorized = auth is AuthState.Authenticated

                    _state.update { it.copy(isAuthorized = isAuthorized) }

                    if (isAuthorized) {
                        startObservingOrders()
                    } else {
                        // останавливаем подписку и чистим список
                        ordersJob?.cancel()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                orders = emptyList()
                            )
                        }
                    }
                }
        }
    }
    private fun startObservingOrders() {
        ordersJob?.cancel()

        ordersJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            observeUserOrders()
                .collect { orders ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            orders = orders.map { it.toCardUiModel() }
                        )
                    }
                }
        }
    }
    fun onAction(action: HistoryAction) {
        when (action) {
            HistoryAction.ClickSignIn -> {
                viewModelScope.launch {
                    _events.send(HistoryEvent.NavigateToAuth)
                }
            }
            HistoryAction.ClickGoToMenu -> {
                viewModelScope.launch {
                    _events.send(HistoryEvent.NavigateToMenu)
                }
            }
        }
    }

}