package com.alexmls.lazypizza.feature.catalog.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.designsystem.components.LpSecondaryButton
import com.alexmls.lazypizza.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.designsystem.theme.bodyMediumMedium
import com.alexmls.lazypizza.designsystem.theme.titleLargeSemiBold
import com.alexmls.lazypizza.feature.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.feature.catalog.presentation.model.ProductUi

@Composable
fun PizzaCard(
    item: ProductUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val click by rememberUpdatedState(onClick)
    val shape = RoundedCornerShape(20.dp)
    val shadowColor = MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        onClick = { click(item.id) },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f)),
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = shape,
                spotColor = shadowColor.copy(0.06f), //  - less evident
                ambientColor = shadowColor
            )
    ) {
        ProductCardContent(item = item)
    }
}
@Composable
fun OtherProductCard(
    item: ProductUi,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val addCb by rememberUpdatedState(onAdd)
    val shape = RoundedCornerShape(20.dp)
    val shadowColor = MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f)),
        modifier = modifier.shadow(
            elevation = 16.dp,
            shape = shape,
            spotColor = shadowColor.copy(0.06f),
            ambientColor = shadowColor
        )
    ) {
        ProductCardContent(
            item = item,
            rightSlot = {
                LpSecondaryButton(
                    text = stringResource(R.string.add_to_cart),
                    onClick = { addCb(item.id) },
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .height(40.dp)
                )
            }
        )
    }
}
@Composable
private fun ProductCardContent(
    item: ProductUi,
    rightSlot: @Composable RowScope.() -> Unit = {}
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
                .padding(vertical = 12.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMediumMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (item.description.isNotEmpty()) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.priceLabel,
                    style = MaterialTheme.typography.titleLargeSemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
                rightSlot()
            }
        }
    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xffF0F3F6
)
@Composable
private fun PizzaCardSamplePr() {
    LazyPizzaTheme {
        val item = ProductUi(
            id = "pizza_margherita",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            priceCents = 899,
            priceLabel = "$8.99",
            category = CategoryUi.Pizza,
            imageResId = R.drawable.pizza_margherita,
        )

        Column(
            Modifier.width(412.dp)
                .height(150.dp)
                .padding(16.dp)
        ){
            PizzaCard(
                item = item,
                onClick = {}
            )
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xffF0F3F6)
@Composable
private fun OtherProductCardPreview() {
    LazyPizzaTheme {
        val drink = ProductUi(
            id = "drink_mineral",
            name = "Mineral Water",
            description = "",
            priceCents = 149,
            priceLabel = "$1.49",
            category = CategoryUi.Drinks,
            imageResId = R.drawable.mineral_water,
        )
        Column(
            Modifier.width(412.dp)
                .height(150.dp)
                .padding(16.dp)
        ){
            OtherProductCard(item = drink, onAdd = {})
        }
    }
}