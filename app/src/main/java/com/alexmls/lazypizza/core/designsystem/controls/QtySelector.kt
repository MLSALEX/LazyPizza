package com.alexmls.lazypizza.core.designsystem.controls

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.theme.Remove

@Composable
fun QtySelector(
    value: Int,
    onInc: () -> Unit,
    onDec: () -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..999,
    width: Dp = 96.dp,
) {
    val inc by rememberUpdatedState(onInc)
    val dec by rememberUpdatedState(onDec)

    val height = 24.dp
    val buttonSize = 24.dp
    val iconSize = 14.dp
    val numberStyle = MaterialTheme.typography.titleMedium

    val canDec = value >= range.first
    val canInc = value < range.last

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        IconButton(
            onClick = { if (canDec) dec() },
            enabled = canDec,
            modifier = Modifier.size(buttonSize)
        ) {
            Icon(
                Icons.Filled.Remove,
                stringResource(R.string.decrease_item_quantity),
                Modifier.size(iconSize))
        }
        Text(
            text = value.toString(),
            style = numberStyle,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        )
        IconButton(
            onClick = { if (canInc) inc() },
            enabled = canInc,
            modifier = Modifier.size(buttonSize)
        ) {
            Icon(
                Icons.Default.Add,
                stringResource(R.string.increase_item_quantity),
                Modifier.size(iconSize))
        }
    }
}