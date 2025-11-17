package com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart_checkout.presentation.components.OrderItemCard
import com.alexmls.lazypizza.cart_checkout.presentation.model.OrderItemUi
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.typography

@Composable
fun OrderDetailsSection(
    items: List<OrderItemUi>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onIncItem: (String) -> Unit,
    onDecItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val onToggleUp by rememberUpdatedState(onToggleExpanded)
    val onIncUp by rememberUpdatedState(onIncItem)
    val onDecUp by rememberUpdatedState(onDecItem)
    val onRemoveUp by rememberUpdatedState(onRemoveItem)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OrderDetailsHeader(
            expanded = expanded,
            onClick = { onToggleUp() },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items.forEach { item ->
                    val itemId = item.id

                    val inc = remember(itemId, onIncUp) { { onIncUp(itemId) } }
                    val dec = remember(itemId, onDecUp) { { onDecUp(itemId) } }
                    val remove = remember(itemId, onRemoveUp) { { onRemoveUp(itemId) } }

                    OrderItemCard(
                        item = item,
                        onInc = inc,
                        onDec = dec,
                        onRemove = remove,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderDetailsHeader(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onClickUp by rememberUpdatedState(onClick)

    Row(
        modifier = modifier
            .clickable { onClickUp() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.order_details),
            style = typography().labelMedium,
            color = color().secondary
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            shape = MaterialTheme.shapes.small,

            border = BorderStroke(1.dp, color().outlineVariant),
            modifier = Modifier.size(22.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    tint = color().secondary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}