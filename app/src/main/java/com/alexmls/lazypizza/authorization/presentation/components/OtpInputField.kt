package com.alexmls.lazypizza.authorization.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    shouldShowCursor: Boolean = false,
    shouldCursorBlink: Boolean = false,
    isError: Boolean = false,
    onOtpModified: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpLength) {
            throw IllegalArgumentException("OTP should be $otpLength digits")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(
            text = otpText,
            selection = TextRange(otpText.length)
        ),
        onValueChange = { value ->
            val newText = value.text.take(otpLength)
            onOtpModified(newText, newText.length == otpLength)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpLength) { index ->
                    CharacterContainer(
                        index = index,
                        text = otpText,
                        shouldShowCursor = shouldShowCursor,
                        shouldCursorBlink = shouldCursorBlink,
                        isError = isError,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}

@Composable
internal fun CharacterContainer(
    index: Int,
    text: String,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val hasChar = index < text.length
    val isFocused = text.length == index
    val isFocusedEmpty = isFocused
    val showPlaceholder = !hasChar && !isFocused

    val character = when {
        hasChar -> text[index].toString()
        showPlaceholder -> "0"
        else -> ""
    }

    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    LaunchedEffect(isFocused, shouldShowCursor, shouldCursorBlink) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800)
                cursorVisible.value = !cursorVisible.value
            }
        } else {
            cursorVisible.value = shouldShowCursor
        }
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large
            )
            .border(
                width = 1.dp,
                color = if (isError)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character,
            style = MaterialTheme.typography.titleMedium,
            color = when {
                hasChar -> MaterialTheme.colorScheme.onSurface
                showPlaceholder -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.onSurface
            },
            textAlign = TextAlign.Center
        )

        AnimatedVisibility(visible = isFocusedEmpty && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}