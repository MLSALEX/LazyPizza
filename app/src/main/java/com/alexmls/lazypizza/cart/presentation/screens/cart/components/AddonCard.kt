package com.alexmls.lazypizza.cart.presentation.screens.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.cart.presentation.model.AddonUi
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.ProductUi
import com.alexmls.lazypizza.catalog.presentation.preview.PreviewProducts
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.card_style.LpCardStyle
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.ProductImage
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme

@Composable
fun AddonCard(
    addon: AddonUi,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    style: LpCardStyle = rememberLpCardStyle()
) {
    val onAddUp by rememberUpdatedState(onAdd)
    val price = remember(addon.priceCents) { UsdFormat.format(addon.priceCents) }

    Card(
        shape = style.shape,
        colors = CardDefaults.cardColors(
            containerColor = style.container,
            contentColor = contentColorFor(style.container)
        ),
        border = style.borderDefault,
        modifier = modifier
            .width(160.dp)
            .shadow(
                elevation = 12.dp,
                shape = style.shape,
                spotColor = style.shadowSpot,
                ambientColor = style.shadowAmbient
            )
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(
                    url = addon.imageUrl,
                    contentDescription = addon.name,
                    modifier = Modifier
                        .size(108.dp)
                        .padding(vertical = 6.dp)
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ){
                Text(
                    text = addon.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Row(
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    IconButton(
                        onClick = onAddUp,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_to_cart),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F3F6)
@Composable
private fun Preview_AddonCard() {
    LazyPizzaTheme {
        val sample = PreviewProducts.basic4.first {
            it.category == CategoryUi.Drinks || it.category == CategoryUi.Sauces
        }.toAddonUi()

        AddonCard(
            addon = sample,
            onAdd = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

private fun ProductUi.toAddonUi(): AddonUi =
    AddonUi(
        id = id,
        name = name,
        imageUrl = imageUrl,
        priceCents = priceCents
    )