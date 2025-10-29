package com.alexmls.lazypizza.catalog.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.LpSecondaryButton
import com.alexmls.lazypizza.core.designsystem.components.PriceWithQty
import com.alexmls.lazypizza.core.designsystem.components.ProductDescription
import com.alexmls.lazypizza.core.designsystem.components.ProductImagePanel
import com.alexmls.lazypizza.core.designsystem.components.ProductPrice
import com.alexmls.lazypizza.core.designsystem.components.ProductTitle
import com.alexmls.lazypizza.core.designsystem.components.TrashButton
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme

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
                .height(IntrinsicSize.Min)
                .requiredHeightIn(min = 120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePanel(
                url = item.imageUrl,
                contentDescription = item.name,
                backgroundColor = style.imageAreaColor,
                modifier = Modifier.fillMaxHeight()
            )

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
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
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImagePanel(
                url = item.imageUrl,
                contentDescription = item.name,
                backgroundColor = style.imageAreaColor,
                modifier = Modifier.fillMaxHeight()
            )

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
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

