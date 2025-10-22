package com.alexmls.lazypizza.core.designsystem.components


import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexmls.lazypizza.R

@Composable
fun ProductImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    bgColor: Color? = null,
    bgShape: Shape = CircleShape,
    inset: Dp = 0.dp,
    clipToBgShape: Boolean = false
) {
    val withBackground = bgColor?.let { c ->
        modifier.background(color = c, shape = bgShape)
    } ?: modifier

    val contentModifier = withBackground
        .padding(inset)
        .let { if (clipToBgShape && bgColor != null) it.clip(bgShape) else it }
    val isPreview = LocalInspectionMode.current
    if (isPreview) {
        Image(
            painter = painterResource(R.drawable.bacon),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = contentModifier.fillMaxSize()
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            modifier = contentModifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}