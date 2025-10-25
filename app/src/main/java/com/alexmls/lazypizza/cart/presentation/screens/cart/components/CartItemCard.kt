package com.alexmls.lazypizza.cart.presentation.screens.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.ProductImage
import com.alexmls.lazypizza.core.designsystem.components.TrashButton
import com.alexmls.lazypizza.core.designsystem.controls.QtySelector
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium

@Composable
fun CartItemCard(
    item: CartItemUi,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    style: LpCardStyle = rememberLpCardStyle(),
) {
    val onIncUp by rememberUpdatedState(onInc)
    val onDecUp by rememberUpdatedState(onDec)
    val onRemoveUp by rememberUpdatedState(onRemove)

    val unit = remember(item.unitPriceCents) { UsdFormat.format(item.unitPriceCents) }
    val total = remember(item.totalCents) { UsdFormat.format(item.totalCents) }
    val miniLabel = remember(item.qty, unit) { "${item.qty} × $unit" }

    Card(
        shape = style.shape,
        colors = CardDefaults.cardColors(
            containerColor = style.container,
            contentColor = contentColorFor(style.container)
        ),
        border = style.borderDefault,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = style.shape,
                spotColor = style.shadowSpot,
                ambientColor = style.shadowAmbient
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .requiredHeightIn(min = 112.dp)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(
                    url = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(104.dp)
                        .padding(6.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMediumMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )

                    TrashButton(onClick = onRemoveUp)
                }

                if (item.extrasLines.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    item.extrasLines.forEach { line ->
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    QtySelector(
                        value = item.qty,
                        onInc = onIncUp,
                        onDec = onDecUp,
                        range = 1..999,
                        allowDecAtMin = false
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = total,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = miniLabel,      // "2 × $8.99"
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}