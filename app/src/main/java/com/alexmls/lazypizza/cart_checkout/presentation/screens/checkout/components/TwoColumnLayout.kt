package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.LocalLayoutType

@Composable
fun TwoColumnLayout(
    layout: LayoutType = LocalLayoutType.current,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 24.dp,
    rowVerticalAlignment: Alignment.Vertical = Alignment.Top,
    left: @Composable ColumnScope.() -> Unit,
    right: @Composable ColumnScope.() -> Unit
) {
    when (layout) {
        LayoutType.Mobile -> {
            Column(modifier = modifier) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    content = left
                )
                Spacer(Modifier.height(horizontalSpacing))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    content = right
                )
            }
        }

        LayoutType.Wide -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                verticalAlignment = rowVerticalAlignment
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    content = left
                )
                Column(
                    modifier = Modifier.weight(1f),
                    content = right
                )
            }
        }
    }
}
