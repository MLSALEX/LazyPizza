package com.alexmls.lazypizza.catalog.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.controls.QtySelector
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme

@Immutable
data class ToppingCallbacks(
    val addOne: () -> Unit,
    val inc: () -> Unit,
    val decOrRemove: () -> Unit
)

private val cardWidth  = 121.dp
private val cardHeight = 142.dp
private val imageSize  = 56.dp
private val vPadding   = 10.dp

@Composable
fun ToppingCard(
    item: ToppingUi,
    qty: Int,
    callbacks: ToppingCallbacks,
    modifier: Modifier = Modifier,
    style: LpCardStyle = rememberLpCardStyle()
) {
    val cb by rememberUpdatedState(callbacks)
    val price = remember(item.priceCents) { UsdFormat.format(item.priceCents) }

    val isActive = qty > 0

    Card(
        onClick = {
            if (!isActive) cb.addOne()
        },
        shape = style.shape,
        colors = CardDefaults.cardColors(
            containerColor = style.container,
            contentColor = contentColorFor(style.container)
        ),
        border = if (isActive) style.borderActive else style.borderDefault,
        modifier = modifier
            .requiredWidth(cardWidth)
            .requiredHeight(cardHeight)
            .shadow(
                elevation = 16.dp,
                shape = style.shape,
                spotColor = style.shadowSpot,
                ambientColor = style.shadowAmbient
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(vPadding)
        ) {
            ProductImage(
                url = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.size(imageSize),
                bgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                bgShape = CircleShape,
                inset = 4.dp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary
                )

                Box(Modifier
                    .padding(top = 4.dp)
                    .animateContentSize()
                ) {
                    if (!isActive) {
                        Text(
                            text = price,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        QtySelector(
                            value = qty,
                            onInc = cb.inc,
                            onDec = cb.decOrRemove,
                            range = 1..3,
                            width = 98.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Topping • qty = 0",
    widthDp = 160,
    heightDp = 160,
    showBackground = true,
    backgroundColor = 0xFFF0F3F6
)
@Composable
private fun Topping_Default() {
    LazyPizzaTheme {
        ToppingCard(
            item = ToppingUi(
                id = "t_bacon",
                name = "Bacon",
                priceCents = 100,
                imageUrl = "https://pl-coding.com/wp-content/uploads/lazypizza/topping/bacon.png",
                maxUnits = 3
                ),
            qty = 0,
            callbacks = ToppingCallbacks(
                addOne = {},
                inc = {},
                decOrRemove = {}),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(
    name = "Topping • qty = 3",
    widthDp = 160,
    heightDp = 160,
    showBackground = true,
    backgroundColor = 0xFFF0F3F6
)
@Composable
private fun Topping_Qty3() {
    LazyPizzaTheme {
        ToppingCard(
            item = ToppingUi(
                "t_cheese",
                "Bacon",
                100,
                "https://pl-coding.com/wp-content/uploads/lazypizza/topping/cheese.png",
                maxUnits = 3,
                ),
            qty = 3,
            callbacks = ToppingCallbacks(
                addOne = {},
                inc = {},
                decOrRemove = {}
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}
