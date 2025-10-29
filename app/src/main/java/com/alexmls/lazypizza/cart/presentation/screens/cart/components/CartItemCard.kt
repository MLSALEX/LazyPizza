package com.alexmls.lazypizza.cart.presentation.screens.cart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.cart.presentation.model.CartItemUi
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.PriceWithQty
import com.alexmls.lazypizza.core.designsystem.components.ProductImagePanel
import com.alexmls.lazypizza.core.designsystem.components.ProductTitle
import com.alexmls.lazypizza.core.designsystem.components.TrashButton

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

    val colors = CardDefaults.cardColors(
        containerColor = style.container,
        contentColor = contentColorFor(style.container)
    )
    val base = modifier.shadow(
        elevation = 16.dp,
        shape = style.shape,
        spotColor = style.shadowSpot,
        ambientColor = style.shadowAmbient
    )

    Card(
        shape = style.shape,
        colors = colors,
        border = style.borderDefault,
        modifier = base
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .requiredHeightIn(min = 112.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePanel(
                url = item.imageUrl,
                contentDescription = item.name,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxHeight(),
                width = 112.dp,
                imageSize = 104.dp,
                inset = 6.dp
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProductTitle(
                        text = item.name,
                        modifier = Modifier.weight(1f)
                    )
                    TrashButton(onClick = onRemoveUp)
                }

                if (item.extrasLines.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    item.extrasLines.forEach { line ->
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.weight(1f))

                PriceWithQty(
                    qty = item.qty,
                    unitCents = item.unitPriceCents,
                    onInc = onIncUp,
                    onDec = onDecUp
                )
            }
        }
    }
}