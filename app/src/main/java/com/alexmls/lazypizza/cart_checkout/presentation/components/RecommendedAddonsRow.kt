package com.alexmls.lazypizza.cart_checkout.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.cart_checkout.presentation.model.AddonUi
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.components.AddonCard

@Composable
fun RecommendedAddonsRow(
    title: String,
    items: List<AddonUi>,
    onAddClick: (AddonUi) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val onAddUp by rememberUpdatedState(onAddClick)

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(items, key = { it.id }) { a ->
                AddonCard(
                    addon = a,
                    onAdd = { onAddUp(a) }
                )
            }
        }
    }
}