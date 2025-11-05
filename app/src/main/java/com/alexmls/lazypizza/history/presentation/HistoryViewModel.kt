package com.alexmls.lazypizza.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _events = Channel<HistoryEvent>(Channel.BUFFERED)
    val events: Flow<HistoryEvent> = _events.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState()
        )

    init {
        viewModelScope.launch {
            authRepository.authState.collect { auth ->
                _state.update {
                    it.copy(
                        isAuthorized = auth is AuthState.Authenticated
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
        }
    }

}