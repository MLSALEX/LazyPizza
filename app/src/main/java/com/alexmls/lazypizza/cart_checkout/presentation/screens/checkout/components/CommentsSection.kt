package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.typography

@Composable
fun CommentsSection(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.comments),
            style = typography().labelMedium,
            color = color().secondary
        )
        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = typography().bodyLarge.copy(
                color = color().onPrimaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 92.dp)
                .background(
                    color = color().surfaceVariant,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(16.dp),
            decorationBox = { inner ->
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_comment),
                        style = typography().bodyLarge,
                        color = color().secondary
                    )
                }
                inner()
            }
        )
    }
}