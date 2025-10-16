package com.alexmls.lazypizza.app.presentation.shell.components

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    badgeOffset: DpOffset = DpOffset(8.dp, (-8).dp),
    labelTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    iconSize: Dp = 16.dp,
    badgeSize: Dp = 16.dp
) {
    val cs = MaterialTheme.colorScheme
    val iconTint = if (selected) cs.primary else cs.onSurfaceVariant
    val labelColor = if (selected) cs.onPrimaryContainer else cs.onSurfaceVariant
    val bubbleColor = if (selected) cs.primary.copy(alpha = 0.08f) else Color.Transparent

    val interactions = remember { MutableInteractionSource() }
    val iconRipple = ripple(bounded = true, radius = 28.dp)

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
            modifier = Modifier.size(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
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

            if (tab == NavTab.Cart && badgeCount > 0) {
                val text = if (badgeCount > 99) "99+" else badgeCount.toString()
                Surface(
                    shape = CircleShape,
                    color = cs.primary,
                    contentColor = cs.onPrimary,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = badgeOffset.x, y = badgeOffset.y)
                        .zIndex(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(badgeSize)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(text = label, color = labelColor, style = labelTextStyle)
    }
}