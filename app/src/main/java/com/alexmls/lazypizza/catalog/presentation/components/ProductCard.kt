package com.alexmls.lazypizza.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.LpSecondaryButton
import com.alexmls.lazypizza.core.designsystem.components.ProductImage
import com.alexmls.lazypizza.core.designsystem.components.TrashButton
import com.alexmls.lazypizza.core.designsystem.controls.QtySelector
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium

@Composable
fun PizzaCard(
    item: ProductUi,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
    style: LpCardStyle = rememberLpCardStyle(),
) {
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
        onClick = onOpenDetails,
        shape = style.shape,
        colors = colors,
        border = style.borderDefault,
        modifier = base
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePanel(
                url = item.imageUrl,
                contentDescription = item.name,
                backgroundColor = style.imageAreaColor,
                modifier = Modifier.fillMaxHeight()
            )

            Spacer(Modifier.width(12.dp))

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    ProductTitle(item.name)
                    Spacer(Modifier.height(2.dp))
                    ProductDescription(item.description)
                }
                Spacer(Modifier.height(8.dp))
                ProductPrice(item.priceCents)
            }
        }
    }
}

@Composable
fun OtherProductCard(
    item: ProductUi,
    qty: Int,
    onAdd: () -> Unit,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    style: LpCardStyle = rememberLpCardStyle(),
) {
    val hasQty = qty > 0
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
                .height(120.dp)
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePanel(
                url = item.imageUrl,
                contentDescription = item.name,
                backgroundColor = style.imageAreaColor,
                modifier = Modifier.fillMaxHeight()
            )

            Spacer(Modifier.width(12.dp))

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProductTitle(
                        text = item.name,
                        modifier = Modifier.weight(1f)
                    )
                    if (hasQty) {
                        TrashButton(onClick = onRemove)
                    }
                }
                Spacer(Modifier.height(8.dp))

                if (!hasQty) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ProductPrice(item.priceCents, Modifier.weight(1f))
                        LpSecondaryButton(
                            text = stringResource(R.string.add_to_cart),
                            onClick = onAdd,
                            modifier = Modifier.height(40.dp)
                        )
                    }
                } else {
                    PriceWithQty(qty, item.priceCents, onInc, onDec)
                }
            }
        }
    }
}
@Composable
fun ProductImagePanel(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    imageSize: Dp = 112.dp,
    inset: Dp = 6.dp,
    backgroundColor: Color
) {
    Box(
        modifier = modifier
            .width(width)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        ProductImage(
            url = url,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(imageSize)
                .padding(inset)
        )
    }
}
@Composable
private fun PriceWithQty(
    qty: Int,
    unitCents: Int,
    onInc: () -> Unit,
    onDec: () -> Unit,
) {
    val total = remember(qty, unitCents) { UsdFormat.format(qty * unitCents) }
    val unit  = remember(unitCents)      { UsdFormat.format(unitCents) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        QtySelector(value = qty, onInc = onInc, onDec = onDec)
        Spacer(Modifier.width(12.dp))
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            Text(total, style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("${qty} × $unit", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun ProductTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMediumMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier
    )
}

@Composable
fun ProductDescription(
    text: String,
    modifier: Modifier = Modifier
) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.secondary,
            modifier = modifier
        )
    }
}

@Composable
fun ProductPrice(
    cents: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = UsdFormat.format(cents),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier
    )
}

@Preview(
    widthDp = 412,
    heightDp = 150,
    showBackground = true,
    backgroundColor = 0xffF0F3F6)
@Composable
private fun Pizza_Clickable() {
    LazyPizzaTheme {
        val item = ProductUi(
            id = "pizza_margherita",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            priceCents = 899,
            category = CategoryUi.Pizza,
            imageUrl = "https://pl-coding.com/wp-content/uploads/lazypizza/pizza/Margherita.png"
        )
        PizzaCard(
            item = item,
            onOpenDetails = {  },
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
        val drink = ProductUi(
            id = "drink_mineral",
            name = "Mineral Water",
            description = "",
            priceCents = 149,
            category = CategoryUi.Drinks,
            imageUrl = "https://pl-coding.com/wp-content/uploads/lazypizza/drink/mineral-water.png"
        )
        OtherProductCard(
            item = drink,
            qty = 0,
            onAdd = { },
            onInc = { },
            onDec = { },
            onRemove = { },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(widthDp = 360, heightDp = 150, showBackground = true, backgroundColor = 0xFFF0F3F6)
@Composable
private fun Other_Qty2() {
    LazyPizzaTheme {
        val drink = ProductUi(
            id = "drink_mineral",
            name = "Mineral Water",
            description = "",
            priceCents = 149,
            category = CategoryUi.Drinks,
            "https://pl-coding.com/wp-content/uploads/lazypizza/sauce/bbq.png"
        )

        OtherProductCard(
            item = drink,
            qty = 2,
            onAdd = { },
            onInc = { },
            onDec = { },
            onRemove = { },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

