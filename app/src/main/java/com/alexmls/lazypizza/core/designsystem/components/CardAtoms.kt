package com.alexmls.lazypizza.core.designsystem.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.controls.QtySelector
import com.alexmls.lazypizza.core.designsystem.theme.bodyLargeMedium

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
fun PriceWithQty(
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
        style = MaterialTheme.typography.bodyLargeMedium,
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