package com.alexmls.lazypizza.catalog.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexmls.lazypizza.R

@Composable
fun ProductImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val isPreview = LocalInspectionMode.current
    if (isPreview) {
        Image(
            painter = painterResource(R.drawable.pizza_margherita),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(112.dp).clip(CircleShape)
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            modifier = modifier
                .size(112.dp),
            contentScale = ContentScale.Crop
        )
    }
}