package com.alexmls.lazypizza.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.Logout
import com.alexmls.lazypizza.core.designsystem.theme.Person
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumBold
import com.alexmls.lazypizza.core.designsystem.theme.bodyMediumMedium

@Immutable
sealed interface NavBarConfig {
    data class TitleCenter(val title: String) : NavBarConfig
    data class TitleWithPhone(
        val title: String,
        val phone: String,
        val onPhone: (String) -> Unit,
        val isAuthorized: Boolean,
        val onUserClick: () -> Unit
    ) : NavBarConfig
    data class BackOnly(val onBack: () -> Unit) : NavBarConfig
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    config: NavBarConfig,
    modifier: Modifier = Modifier,
) {
    val colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)

    when (config) {
        is NavBarConfig.TitleCenter -> {
            CenterAlignedTopAppBar(
                colors = colors,
                title = {
                    Text(
                        text = config.title,
                        style = MaterialTheme.typography.bodyMediumMedium,
                        modifier = Modifier.semantics { heading() }
                    )
                }
            )
        }

        is NavBarConfig.BackOnly -> {
            val onBack by rememberUpdatedState(config.onBack)
            TopAppBar(
                colors = colors,
                navigationIcon = { BackButton(onBack) },
                title = {}
            )
        }

        is NavBarConfig.TitleWithPhone -> {
            val onPhone by rememberUpdatedState(config.onPhone)
            val onUserClick by rememberUpdatedState(config.onUserClick)

            PlainTopBar(
                modifier = modifier
            ) {
                BrandTitle(text = config.title)
                Spacer(Modifier.weight(1f))
                PhonePill(
                    phone = config.phone,
                    onClick = { onPhone(config.phone) }
                )

                UserIcon(
                    isAuthorized = config.isAuthorized,
                    onClick = onUserClick
                )
            }
        }
    }
}
@Composable
private fun PlainTopBar(
    modifier: Modifier = Modifier,
    barHeight: Dp = 64.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(barHeight)
            .padding( 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        content()
    }
}
@Composable
private fun BackButton(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(56.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
@Composable
private fun BrandTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMediumBold
        )
    }
}

@Composable
private fun PhonePill(phone: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Outlined.Call,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.width(6.dp))
        Text(text = phone, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun UserIcon(
    isAuthorized: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (isAuthorized) {
        Icons.Outlined.Logout
    } else {
        Icons.Outlined.Person
    }
    val iconTint = if (isAuthorized) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    val circleColor = if (isAuthorized) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(circleColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = iconTint
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFfff)
@Composable
private fun Preview_NavBar_TitleWithPhone() {
    LazyPizzaTheme {
        NavBar(
            config = NavBarConfig.TitleWithPhone(
                title = "LazyPizza",
                phone = "+1 (555) 321-7890",
                onPhone = { },
                isAuthorized = false,
                onUserClick = {}
            )
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview_NavBar_BackOnly() {
    LazyPizzaTheme {
        NavBar(
            config = NavBarConfig.BackOnly(
                onBack = { }
            )
        )
    }
}