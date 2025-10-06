package com.alexmls.lazypizza.feature.catalog.presentation.components

import androidx.annotation.IntRange
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.designsystem.components.LpSecondaryButton
import com.alexmls.lazypizza.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.designsystem.theme.Remove
import com.alexmls.lazypizza.designsystem.theme.Trash
import com.alexmls.lazypizza.designsystem.theme.bodyMediumMedium
import com.alexmls.lazypizza.designsystem.theme.titleLargeSemiBold
import com.alexmls.lazypizza.feature.catalog.presentation.ProductAction
import com.alexmls.lazypizza.feature.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.feature.catalog.presentation.model.ProductUi
import com.alexmls.lazypizza.feature.catalog.presentation.utils.UsdMoneyFormatter
import java.util.Locale

@Immutable
data class ProductCardStyle(
    val shape: Shape,
    val containerColor: Color,
    val border: BorderStroke,
    val shadowSpot: Color,
    val shadowAmbient: Color,
)

@Composable
fun rememberProductCardStyle(): ProductCardStyle {
    val cs = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.medium
    val borderColor = Color.White.copy(alpha = 0.9f)
    return ProductCardStyle(
        shape = shape,
        containerColor = cs.surface,
        border = BorderStroke(1.dp, borderColor),
        shadowSpot = cs.onPrimaryContainer.copy(alpha = 0.06f),
        shadowAmbient = cs.onPrimaryContainer
    )
}

enum class ProductCardVariant { WithAddButton, NoAddButton }

@Composable
fun ProductCard(
    item: ProductUi,
    qty: Int,
    formatMoney: (Int) -> String,
    onAction: (ProductAction) -> Unit,
    variant: ProductCardVariant,
    modifier: Modifier = Modifier,
    style: ProductCardStyle = rememberProductCardStyle(),
) {
    val act by rememberUpdatedState(onAction)
    val hasQty = qty > 0

    val onOpen  = remember(item.id, act) { { act(ProductAction.OpenDetails(item.id)) } }
    val onAdd   = remember(item.id, act) { { act(ProductAction.Add(item.id)) } }
    val onInc   = remember(item.id, act) { { act(ProductAction.Inc(item.id)) } }
    val onDec   = remember(item.id, act) { { act(ProductAction.Dec(item.id)) } }
    val onRemove= remember(item.id, act) { { act(ProductAction.Remove(item.id)) } }

    Card(
        onClick = onOpen,
        shape = style.shape,
        colors = CardDefaults.cardColors(
            containerColor = style.containerColor,
            contentColor = contentColorFor(style.containerColor)
        ),
        border = style.border,
        modifier = modifier.shadow(
            elevation = 16.dp,
            shape = style.shape,
            spotColor = style.shadowSpot,
            ambientColor = style.shadowAmbient
        )
    ) {
        ProductCardContent(
            item = item,
            showDescription = (qty == 0),
            topRight = if (hasQty) { { TrashButton(onClick = onRemove) } } else null,
            bottomContent = {
                BottomContent(
                    qty = qty,
                    priceCents = item.priceCents,
                    showAddButton = (variant == ProductCardVariant.WithAddButton && qty == 0),
                    formatMoney = formatMoney,
                    onAdd = onAdd,
                    onInc = onInc,
                    onDec = onDec
                )
            }
        )
    }
}

@Composable
fun PizzaCard(
    item: ProductUi,
    @IntRange(from = 0)qty: Int,
    formatMoney: (Int) -> String,
    onAction: (ProductAction) -> Unit,
    modifier: Modifier = Modifier
) = ProductCard(
    item = item,
    qty = qty,
    formatMoney = formatMoney,
    onAction = onAction,
    variant = ProductCardVariant.NoAddButton,
    modifier = modifier
)

@Composable
fun OtherProductCard(
    item: ProductUi,
    @IntRange(from = 0) qty: Int,
    formatMoney: (Int) -> String,
    onAction: (ProductAction) -> Unit,
    modifier: Modifier = Modifier
) = ProductCard(
    item = item,
    qty = qty,
    formatMoney = formatMoney,
    onAction = onAction,
    variant = ProductCardVariant.WithAddButton,
    modifier = modifier
)

@Composable
private fun BottomContent(
    qty: Int,
    priceCents: Int,
    showAddButton: Boolean,
    formatMoney: (Int) -> String,
    onAdd: () -> Unit,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    if (qty == 0) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatMoney(priceCents),
                style = MaterialTheme.typography.titleLargeSemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f)
            )
            if (showAddButton) {
                LpSecondaryButton(
                    text = stringResource(R.string.add_to_cart),
                    onClick = onAdd,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    } else {
        val money by rememberUpdatedState(formatMoney)
        val total = remember(qty, priceCents) { money(qty * priceCents) }
        val unit  = remember(priceCents) { money(priceCents) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            QtySelector(qty = qty, onInc = onInc, onDec = onDec)
            Spacer(Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = total,
                    style = MaterialTheme.typography.titleLargeSemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${qty} × $unit",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun QtySelector(qty: Int, onInc: () -> Unit, onDec: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
       IconButton(onClick = onDec, modifier = Modifier.size(24.dp)) {
            Icon(
                Icons.Filled.Remove,
                contentDescription = stringResource(R.string.decrease_item_quantity),
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = qty.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        IconButton(onClick = onInc, modifier = Modifier.size(24.dp)) {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(R.string.increase_item_quantity),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun TrashButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(24.dp)) {
        Icon(
            Icons.Filled.Trash,
            contentDescription = stringResource(R.string.delete_item),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun ProductCardContent(
    item: ProductUi,
    showDescription: Boolean,
    topRight: (@Composable () -> Unit)? = null,
    bottomContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(120.dp)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.imageResId),
                contentDescription = item.name,
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMediumMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    if (topRight != null) {
                        Spacer(Modifier.width(8.dp))
                        topRight()
                    }
                }
            }

            if (showDescription && item.description.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(Modifier.height(8.dp))
            Box(Modifier.animateContentSize()) { bottomContent() }
        }
    }
}

@Preview(
    widthDp = 412,
    heightDp = 150,
    showBackground = true,
    backgroundColor = 0xffF0F3F6)
@Composable
private fun Pizza_Qty0() {
    val formatter = UsdMoneyFormatter(Locale.US)
    val formatMoney: (Int) -> String = formatter::format
    LazyPizzaTheme {
        val item = ProductUi(
            id = "pizza_margherita",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            priceCents = 899,
            category = CategoryUi.Pizza,
            imageResId = R.drawable.pizza_margherita
        )

        PizzaCard(
            item = item,
            qty = 0,
            formatMoney = formatMoney,
            onAction = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(
    widthDp = 412,
    heightDp = 150,
    showBackground = true,
    backgroundColor = 0xffF0F3F6)
@Composable
private fun Pizza_Qty2() {
    val formatter = UsdMoneyFormatter(Locale.US)
    val formatMoney: (Int) -> String = formatter::format
    LazyPizzaTheme {
        val item = ProductUi(
            id = "pizza_margherita",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            priceCents = 899,
            category = CategoryUi.Pizza,
            imageResId = R.drawable.pizza_margherita
        )

        PizzaCard(
            item = item,
            qty = 2,
            formatMoney = formatMoney,
            onAction = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(widthDp = 412, heightDp = 150, showBackground = true, backgroundColor = 0xFFF0F3F6)
@Composable
private fun Other_Qty0() {
    LazyPizzaTheme {
        val formatter = UsdMoneyFormatter(Locale.US)
        val formatMoney: (Int) -> String = formatter::format

        val drink = ProductUi(
            id = "drink_mineral",
            name = "Mineral Water",
            description = "",
            priceCents = 149,
            category = CategoryUi.Drinks,
            imageResId = R.drawable.mineral_water
        )

        OtherProductCard(
            item = drink,
            qty = 0,
            formatMoney = formatMoney,
            onAction = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(widthDp = 412, heightDp = 150, showBackground = true, backgroundColor = 0xFFF0F3F6)
@Composable
private fun Other_Qty2() {
    LazyPizzaTheme {
        val formatter = UsdMoneyFormatter(Locale.US)
        val formatMoney: (Int) -> String = formatter::format

        val drink = ProductUi(
            id = "drink_mineral",
            name = "Mineral Water",
            description = "",
            priceCents = 149,
            category = CategoryUi.Drinks,
            imageResId = R.drawable.mineral_water
        )

        OtherProductCard(
            item = drink,
            qty = 2,
            formatMoney = formatMoney,
            onAction = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

