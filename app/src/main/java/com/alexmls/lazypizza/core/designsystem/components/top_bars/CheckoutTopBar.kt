package com.alexmls.lazypizza.core.designsystem.components.top_bars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.typography

@Composable
fun CheckoutTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = typography().bodyMediumMedium,
            color = color().onPrimaryContainer
        )

        CheckoutBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun CheckoutBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onClickState by rememberUpdatedState(onClick)

    Box(
        modifier = modifier
            .size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        FilledIconButton(
            onClick = onClickState,
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = color().secondaryContainer,
                contentColor = color().secondary
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = color().secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}