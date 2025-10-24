package com.alexmls.lazypizza.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.core.designsystem.theme.titleLargeMedium

@Composable
fun EmptyState(
    title: String,
    subtitle: String?,
    modifier: Modifier = Modifier,
    topPadding: Dp = 120.dp,
    action: (@Composable () -> Unit)? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLargeMedium,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = topPadding)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = titleTextStyle,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = subtitleTextStyle,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (action != null) {
                Spacer(Modifier.height(20.dp))
                action()
            }
        }
    }
}