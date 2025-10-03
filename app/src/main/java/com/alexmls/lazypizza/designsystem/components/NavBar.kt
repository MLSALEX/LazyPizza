package com.alexmls.lazypizza.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.designsystem.theme.bodyMediumBold

sealed interface NavBarAction {
    data object Back : NavBarAction
    data class Phone(val number: String) : NavBarAction
}

sealed interface NavBarConfig {
    data class TitleWithPhone(val title: String, val phone: String) : NavBarConfig
    data object BackOnly : NavBarConfig
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    config: NavBarConfig,
    onClick: (NavBarAction) -> Unit,
    barHeight: Dp = 64.dp,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.height(barHeight),
        navigationIcon = {
            if (config is NavBarConfig.BackOnly) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { onClick(NavBarAction.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        },
        title = {
            when (config) {
                is NavBarConfig.TitleWithPhone -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logo),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(config.title, style = MaterialTheme.typography.bodyMediumBold)

                        Spacer(Modifier.weight(1f))

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onClick(NavBarAction.Phone(config.phone)) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Call,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = config.phone,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                NavBarConfig.BackOnly -> Unit
            }
        },
        actions = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview_NavBar_TitleWithPhone() {
    LazyPizzaTheme {
        NavBar(
            config = NavBarConfig.TitleWithPhone(
                title = "LazyPizza",
                phone = "+1 (555) 321-7890"
            ),
            onClick = {} // в превью — заглушка
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview_NavBar_BackOnly() {
    LazyPizzaTheme {
        NavBar(
            config = NavBarConfig.BackOnly,
            onClick = {}
        )
    }
}