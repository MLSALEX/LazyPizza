package com.alexmls.lazypizza.app.presentation.shell.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.alexmls.lazypizza.app.navigation.NavTab

@Composable
fun TabItem(
    tab: NavTab,
    selected: Boolean,
    label: String,
    iconPainter: Painter,
    badgeCount: Int,
    onClick: () -> Unit,
    badgeOffset: DpOffset = DpOffset(6.dp, (-6).dp),
    labelTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    iconSize: Dp = 18.dp,
    badgeSize: Dp = 18.dp
) {
    val cs = MaterialTheme.colorScheme
    val iconTint = if (selected) cs.primary else cs.onSurfaceVariant
    val labelColor = if (selected) cs.onPrimaryContainer else cs.onSurfaceVariant
    val bubbleColor = if (selected) cs.primary.copy(alpha = 0.08f) else Color.Transparent

    val interactions = remember { MutableInteractionSource() }
    val iconRipple = ripple(bounded = true, radius = 34.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .semantics {
                contentDescription = label
                this.selected = selected
            }
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactions,
                indication = null
            )
    ) {
        Box(
            modifier = Modifier.size(34.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .indication(interactions, iconRipple)
            ) {
                Surface(
                    modifier = Modifier.matchParentSize(),
                    shape = CircleShape,
                    color = bubbleColor,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {}
                Icon(
                    painter = iconPainter,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(iconSize)
                )
            }

            if (tab == NavTab.Cart) {
                CartBadge(
                    count = badgeCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = badgeOffset.x, y = badgeOffset.y)
                        .zIndex(1f),
                    badgeSize = badgeSize
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(text = label, color = labelColor, style = labelTextStyle)
    }
}

@Composable
private fun CartBadge(
    count: Int,
    modifier: Modifier = Modifier,
    badgeSize: Dp = 18.dp
) {
    val cs = MaterialTheme.colorScheme
    val visible = count > 0
    val displayText = if (count > 99) "99+" else count.toString()

    val pop = remember { Animatable(1f) }
    LaunchedEffect(count) {
        if (visible) {
            pop.snapTo(0.85f)
            pop.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.45f, stiffness = 700f)
            )
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier.graphicsLayer {
            scaleX = pop.value
            scaleY = pop.value
        },
        enter = scaleIn(
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
            initialScale = 0.7f
        ) + fadeIn(),
        exit = fadeOut() + shrinkOut()
    ) {
        Surface(
            shape = CircleShape,
            color = cs.primary,
            contentColor = cs.onPrimary,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier.size(badgeSize),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = displayText,
                    transitionSpec = {
                        (scaleIn(initialScale = 0.85f) + fadeIn())
                            .togetherWith(scaleOut(targetScale = 1.1f) + fadeOut())
                    },
                    label = "badgeText"
                ) { value ->
                    Text(text = value, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}