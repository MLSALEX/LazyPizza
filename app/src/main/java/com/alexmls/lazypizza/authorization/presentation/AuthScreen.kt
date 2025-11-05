package com.alexmls.lazypizza.authorization.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.authorization.data.FakeAuthRepository
import com.alexmls.lazypizza.core.designsystem.components.LpPrimaryButton
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.pillShape
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoot(
    onFinishedSignedIn: () -> Unit,
    onFinishedGuest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val act by rememberUpdatedState(viewModel::onAction)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AuthEvent.NavigateBackSignedIn -> onFinishedSignedIn()
                AuthEvent.NavigateBackGuest -> onFinishedGuest()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(Modifier.weight(1f))

        AuthContent(
            state = state,
            onAction = act,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(2f))
    }
}

@Composable
private fun AuthContent(
    state: AuthUiState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val act by rememberUpdatedState(onAction)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthHeader(step = state.step)

        when (state.step) {
            AuthUiState.Step.Phone -> AuthPhoneStep(
                phone = state.phone,
                isLoading = state.isLoading,
                error = state.error,
                onPhoneChanged = { act(AuthAction.PhoneChanged(it)) },
                onContinue = { act(AuthAction.ContinueClicked) },
                onContinueAsGuest = { act(AuthAction.ContinueAsGuestClicked) }
            )

            AuthUiState.Step.Code -> AuthCodeStep(
                phone = state.phone,
                code = state.code,
                isLoading = state.isLoading,
                error = state.error,
                onCodeChanged = { act(AuthAction.CodeChanged(it)) },
                onConfirm = { act(AuthAction.ConfirmClicked) },
                onContinueAsGuest = { act(AuthAction.ContinueAsGuestClicked) },
                onResend = { act(AuthAction.ResendClicked) }
            )
        }
    }
}

@Composable
private fun AuthPhoneStep(
    phone: String,
    isLoading: Boolean,
    error: String?,
    onPhoneChanged: (String) -> Unit,
    onContinue: () -> Unit,
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasError = error != null
    val isContinueEnabled = phone.isNotBlank() && !isLoading

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LpTextFieldPhone(
            value = phone,
            onValueChange = onPhoneChanged,
            isError = hasError,
            placeholder = "+1 000 000 0000",
            modifier = Modifier.fillMaxWidth()
        )

        AuthErrorText(error)

        LpPrimaryButton(
            text = stringResource(R.string.auth_continue),
            onClick = onContinue,
            enabled = isContinueEnabled,
            modifier = Modifier.fillMaxWidth(),
            height = 40.dp
        )

        TextButton(
            onClick = onContinueAsGuest
        ) {
            Text(
                text = stringResource(R.string.continue_without_signing_in),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun LpTextFieldPhone(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(placeholder) },
        singleLine = true,
        isError = isError,
        shape = pillShape,
        enabled = enabled,
        readOnly = readOnly
    )
}

@Composable
private fun AuthCodeStep(
    phone: String,
    code: String,
    isLoading: Boolean,
    error: String?,
    onCodeChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasError = error != null
    val isConfirmEnabled = code.isNotBlank() && !isLoading

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LpTextFieldPhone(
            value = phone,
            onValueChange = {},
            isError = false,
            placeholder = "",
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            readOnly = true
        )

        OutlinedTextField(
            value = code,
            onValueChange = onCodeChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(FakeAuthRepository.TEST_CODE) },
            singleLine = true,
            isError = hasError,
            shape = pillShape
        )

        AuthErrorText(error)

        LpPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = onConfirm,
            enabled = isConfirmEnabled,
            modifier = Modifier.fillMaxWidth(),
            height = 40.dp
        )

        TextButton(onClick = onContinueAsGuest) {
            Text(
                text = stringResource(R.string.continue_without_signing_in),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
        }

        TextButton(onClick = onResend) {
            Text(
                text = stringResource(R.string.auth_resend_code),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun AuthErrorText(
    error: String?,
    modifier: Modifier = Modifier
) {
    if (error == null) return

    Text(
        text = error,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AuthHeader(
    step: AuthUiState.Step,
    modifier: Modifier = Modifier
) {
    val subtitle = when (step) {
        AuthUiState.Step.Phone -> stringResource(R.string.enter_your_phone_number)
        AuthUiState.Step.Code  -> stringResource(R.string.auth_enter_code_title)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_to_lazypizza),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
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