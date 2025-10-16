package com.alexmls.lazypizza.app.presentation.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.app.navigation.NavTab
import com.alexmls.lazypizza.app.navigation.NavigationBarState
import com.alexmls.lazypizza.app.navigation.utils.iconRes
import com.alexmls.lazypizza.app.navigation.utils.labelRes
import com.alexmls.lazypizza.app.presentation.shell.components.TabItem
import com.alexmls.lazypizza.core.designsystem.components.DropShadowAbove

@Composable
fun BottomBar(
    state: NavigationBarState,
    onSelectTab: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(NavTab.Menu, NavTab.Cart, NavTab.History)
    val base = MaterialTheme.shapes.medium

    val topOnly = RoundedCornerShape(
        topStart = base.topStart,
        topEnd   = base.topEnd,
        bottomStart = CornerSize(0),
        bottomEnd   = CornerSize(0)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.navigationBars.only(
                    WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
                )
            )
    ) {
        DropShadowAbove(
            modifier = Modifier.align(Alignment.TopCenter),
            color = Color(0x0F03131F),
            offsetY = (-4).dp,
            blur = 16.dp
        )

        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = topOnly,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val selected = state.activeTab == tab
                    val label = stringResource(tab.labelRes())
                    val painter = painterResource(tab.iconRes())
                    val badgeCount = if (tab == NavTab.Cart) state.cartCount else 0

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        TabItem(
                            tab = tab,
                            selected = selected,
                            label = label,
                            iconPainter = painter,
                            badgeCount = badgeCount,
                            onClick = { onSelectTab(tab) }
                        )
                    }
                }
            }
        }
    }
}