package com.alexmls.lazypizza.authorization.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.authorization.presentation.AuthError

@Composable
fun AuthErrorText(
    error: AuthError?,
    modifier: Modifier = Modifier
) {
    if (error == null) return

    val message = when (error) {
        AuthError.InvalidPhone ->
            stringResource(R.string.auth_error_invalid_phone)

        AuthError.WrongCode ->
            stringResource(R.string.auth_error_wrong_code)

        AuthError.Unknown ->
            stringResource(R.string.auth_error_unknown)
    }
    Text(
        text = message,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}
