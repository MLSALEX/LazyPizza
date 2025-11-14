package com.alexmls.lazypizza.authorization.presentation.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexmls.lazypizza.core.designsystem.theme.pillShape

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