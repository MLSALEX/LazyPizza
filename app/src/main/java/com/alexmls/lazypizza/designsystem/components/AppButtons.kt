package com.alexmls.lazypizza.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.designsystem.theme.BrandColors
import com.alexmls.lazypizza.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.designsystem.theme.pillShape


@Composable
fun LpPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 40.dp,
) {

    val containerBrush: Brush = BrandColors.btGradient

    val bgModifier = if (enabled) {
        Modifier.background(containerBrush)
    } else {
        Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = modifier
            .clip(pillShape)
            .height(height)
            .then(bgModifier)
    ) {
       Text(
           text = text,
           style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun LpSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 40.dp,
) {
    val borderColor = if (enabled) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = modifier
            .height(height)
            .defaultMinSize(minWidth = 56.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview_LpButtons_Small() {
    LazyPizzaTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Primary ", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // normal
                    LpPrimaryButton(text = "Label", onClick = {})
                    LpPrimaryButton(text = "Label", onClick = {}, enabled = false)
                }

                Spacer(Modifier.height(12.dp))

                Text("Secondary ", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    LpSecondaryButton(text = "Label", onClick = {})
                    LpSecondaryButton(text = "Label", onClick = {}, enabled = false)
                }
            }
        }
    }
}


