package com.alexmls.lazypizza.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.alexmls.lazypizza.history.presentation.preview.HistoryDemoData
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedDemoData = false

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

                    if (isAuthorized && !hasLoadedDemoData) {
                        // For demo: load static orders once
                        loadDemoOrders()
                        hasLoadedDemoData = true
                    }

                    if (!isAuthorized) {
                        // Clear orders when user logs out
                        _state.update {
                            it.copy(
                                isLoading = false,
                                orders = emptyList()
                            )
                        }
                        hasLoadedDemoData = false
                    }
                }
        }
    }
    private fun loadDemoOrders() {
        _state.update {
            it.copy(
                isLoading = false,
                orders = HistoryDemoData.orders
            )
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