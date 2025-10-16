package com.alexmls.lazypizza.app.presentation.shell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.app.navigation.NavTab
import com.alexmls.lazypizza.app.navigation.NavigationBarState
import com.alexmls.lazypizza.app.navigation.utils.iconRes
import com.alexmls.lazypizza.app.navigation.utils.labelRes
import com.alexmls.lazypizza.app.presentation.shell.components.TabItem

@Composable
fun NavigationRail(
    state: NavigationBarState,
    onSelectTab: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(NavTab.Menu, NavTab.Cart, NavTab.History)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(min = 78.dp)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        tabs.forEach { tab ->
            val selected = state.activeTab == tab
            val label = stringResource(tab.labelRes())
            val painter = painterResource(tab.iconRes())
            val badge = if (tab == NavTab.Cart) state.cartCount else 0

            TabItem(
                tab = tab,
                selected = selected,
                label = label,
                iconPainter = painter,
                badgeCount = badge,
                onClick = { onSelectTab(tab) },
                badgeOffset = DpOffset(14.dp, (-6).dp),
                labelTextStyle = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(24.dp))
        }
        Spacer(Modifier.weight(1f))
    }
}