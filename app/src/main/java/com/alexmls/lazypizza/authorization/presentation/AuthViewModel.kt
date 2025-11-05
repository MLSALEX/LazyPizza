package com.alexmls.lazypizza.authorization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.WrongCodeException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _events = Channel<AuthEvent>(Channel.BUFFERED)
    val events: Flow<AuthEvent> = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthUiState()
        )

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.PhoneChanged ->
                _state.update { it.copy(phone = action.value, error = null) }

            AuthAction.ContinueClicked ->
                onContinueClicked()

            is AuthAction.CodeChanged ->
                _state.update { it.copy(code = action.value.take(6), error = null) }

            AuthAction.ConfirmClicked ->
                onConfirmClicked()

            AuthAction.ContinueAsGuestClicked ->
                viewModelScope.launch {
                    _events.send(AuthEvent.NavigateBackGuest)
                }

            AuthAction.ResendClicked -> {}
        }
    }
    private fun onContinueClicked() {
        val phone = state.value.phone.trim()
        if (phone.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.startPhoneVerification(phone)
                _state.update {
                    it.copy(
                        isLoading = false,
                        step = AuthUiState.Step.Code
                    )
                }
            } catch (e: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    private fun onConfirmClicked() {
        val code = state.value.code.trim()
        if (code.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.verifyCode(code)          //  FakeAuthRepository
                _state.update { it.copy(isLoading = false) }
                _events.send(AuthEvent.NavigateBackSignedIn)
            } catch (e: WrongCodeException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Incorrect code. Please try again."
                    )
                }
            } catch (e: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Something went wrong"
                    )
                }
            }
        }
    }

}