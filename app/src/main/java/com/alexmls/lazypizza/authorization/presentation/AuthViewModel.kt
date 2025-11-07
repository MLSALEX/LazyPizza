package com.alexmls.lazypizza.authorization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.authorization.presentation.helpers.isPhoneComplete
import com.alexmls.lazypizza.authorization.presentation.helpers.toPhoneInput
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.WrongCodeException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    private var resendJob: Job? = null

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthUiState()
        )

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.PhoneChanged -> {
                val cleaned = action.value.toPhoneInput()
                _state.update { it.copy(phone = cleaned, error = null) }
            }


            AuthAction.ContinueClicked -> onContinueClicked()

            is AuthAction.CodeChanged ->
                _state.update { it.copy(code = action.value.take(6), error = null) }

            AuthAction.ConfirmClicked -> onConfirmClicked()

            AuthAction.ContinueAsGuestClicked ->
                viewModelScope.launch {
                    _events.send(AuthEvent.NavigateBackGuest)
                }

            AuthAction.ResendClicked ->  onResendClicked()
        }
    }
    private fun onContinueClicked() {
        requestVerificationCode()
    }

    private fun onResendClicked() {
        requestVerificationCode()
    }

    private fun requestVerificationCode() {
        val phone = state.value.phone

        if (!phone.isPhoneComplete()) {
            _state.update { it.copy(error = AuthError.InvalidPhone) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.startPhoneVerification(phone)
                _state.update {
                    it.copy(
                        isLoading = false,
                        step = AuthUiState.Step.Code,
                        code = "",
                        error = null
                    )
                }
                startResendTimer()
            } catch (e: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = AuthError.Unknown
                    )
                }
            }
        }
    }
    private fun startResendTimer() {
        resendJob?.cancel()

        resendJob = viewModelScope.launch {
            for (sec in RESEND_TIMEOUT_SECONDS downTo 1) {
                _state.update { it.copy(secondsToResend = sec) }
                delay(1_000L)
            }
            _state.update { it.copy(secondsToResend = null) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        resendJob?.cancel()
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
                        error = AuthError.WrongCode
                    )
                }
            } catch (e: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = AuthError.Unknown
                    )
                }
            }
        }
    }
    companion object {
        private const val RESEND_TIMEOUT_SECONDS = 60
    }
}