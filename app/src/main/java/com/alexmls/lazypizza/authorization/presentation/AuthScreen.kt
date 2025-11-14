package com.alexmls.lazypizza.authorization.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.authorization.presentation.components.AuthErrorText
import com.alexmls.lazypizza.authorization.presentation.components.LpTextFieldPhone
import com.alexmls.lazypizza.authorization.presentation.components.OtpInputField
import com.alexmls.lazypizza.authorization.presentation.helpers.formatAsMmSs
import com.alexmls.lazypizza.authorization.presentation.helpers.isPhoneComplete
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoot(
    onFinishedSignedIn: () -> Unit,
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val act by rememberUpdatedState(viewModel::onAction)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AuthEvent.NavigateBackSignedIn -> onFinishedSignedIn()
                AuthEvent.ContinueWithoutSignIn -> onContinueAsGuest()
            }
        }
    }

    AuthScreen(
        state = state,
        onAction = act,
        modifier = modifier
    )
}

@Composable
fun AuthScreen(
    state: AuthUiState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val act by rememberUpdatedState(onAction)
    val title = stringResource(R.string.welcome_to_lazypizza)

    val subtitle = when (state.step) {
        AuthUiState.Step.Phone -> stringResource(R.string.enter_your_phone_number)
        AuthUiState.Step.Code  -> stringResource(R.string.auth_enter_code_title)
    }

    val isPhoneStep = state.step == AuthUiState.Step.Phone

    val primaryTextRes = if (isPhoneStep) {
        R.string.auth_continue
    } else {
        R.string.confirm
    }
    val primaryText = stringResource(primaryTextRes)

    val isPrimaryEnabled = if (isPhoneStep) {
        state.phone.isPhoneComplete() && !state.isLoading
    } else {
        state.code.length == 6 && !state.isLoading
    }

    val onPrimaryClick = remember(isPhoneStep, act) {
        {
            if (isPhoneStep) {
                act(AuthAction.ContinueClicked)
            } else {
                act(AuthAction.ConfirmClicked)
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                title = title,
                subtitle = subtitle
            )
            Spacer(Modifier.height(20.dp))

            LpTextFieldPhone(
                value = state.phone,
                onValueChange = if (isPhoneStep) {
                    { act(AuthAction.PhoneChanged(it)) }
                } else {
                    {}
                },
                isError = state.error == AuthError.InvalidPhone,
                placeholder = "+373 000 00 000",
                modifier = Modifier.fillMaxWidth(),
                enabled = isPhoneStep,
                readOnly = !isPhoneStep
            )
            if (isPhoneStep) {
                Spacer(Modifier.height(8.dp))
                AuthErrorText(
                    error = state.error,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Spacer(Modifier.height(12.dp))
                CodeSection(
                    code = state.code,
                    error = state.error,
                    onCodeChanged = { act(AuthAction.CodeChanged(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(16.dp))
            LpPrimaryButton(
                text = primaryText,
                onClick = onPrimaryClick,
                enabled = isPrimaryEnabled,
                modifier = Modifier.fillMaxWidth(),
                height = 40.dp
            )

            Spacer(Modifier.height(4.dp))

            ContinueAsGuestButton(
                onClick = { act(AuthAction.ContinueAsGuestClicked) }
            )
            if (!isPhoneStep) {
                Spacer(Modifier.height(8.dp))
                ResendSection(
                    secondsToResend = state.secondsToResend,
                    onResend = { act(AuthAction.ResendClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.weight(2f))
    }
}

@Composable
private fun CodeSection(
    code: String,
    error: AuthError?,
    onCodeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWrongCode = error == AuthError.WrongCode
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OtpInputField(
            modifier = Modifier.fillMaxWidth(),
            otpText = code,
            otpLength = 6,
            shouldShowCursor = true,
            shouldCursorBlink = true,
            isError = isWrongCode,
            onOtpModified = { newCode, _ ->
                onCodeChanged(newCode)
            }
        )

        AuthErrorText(
            error = error,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ResendSection(
    secondsToResend: Int?,
    onResend: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (secondsToResend != null) {
        Text(
            text = stringResource(
                R.string.auth_resend_timer,
                secondsToResend.formatAsMmSs()
            ),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    } else {
        TextButton(
            onClick = onResend,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.auth_resend_code),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ContinueAsGuestButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.continue_without_signing_in),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LazyPizzaTheme {
        AuthScreen(
            state = AuthUiState(),
            onAction = {}
        )
    }
}