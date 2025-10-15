package com.alexmls.lazypizza.catalog.presentation.screens.product_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.catalog.presentation.components.ButtonOverlay
import com.alexmls.lazypizza.catalog.presentation.components.ProductImage
import com.alexmls.lazypizza.catalog.presentation.components.ToppingCard
import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi
import com.alexmls.lazypizza.catalog.presentation.preview.PreviewToppings
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.Adaptive
import com.alexmls.lazypizza.core.designsystem.card_style.rememberLpCardStyle
import com.alexmls.lazypizza.core.designsystem.components.NavBar
import com.alexmls.lazypizza.core.designsystem.components.NavBarAction
import com.alexmls.lazypizza.core.designsystem.components.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.rememberLayoutType
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import com.alexmls.lazypizza.core.designsystem.theme.modifier.Corner
import com.alexmls.lazypizza.core.designsystem.theme.modifier.cutCornerOverlay
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProductDetailsRoot(
    viewModel: ProductDetailsScreenViewModel = koinViewModel(),
    onBack: () -> Unit,
    onAddToCart: (totalCents: Int, selected: Map<String, Int>) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductDetailsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ProductDetailsScreenAction.ClickBack -> onBack()
                ProductDetailsScreenAction.ClickAddToCart -> {
                    onAddToCart(state.totalCents, state.qty)
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ProductDetailsScreen(
    state: ProductDetailsScreenState,
    onAction: (ProductDetailsScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val layout = rememberLayoutType()
    val totalText by remember(state.totalCents) {
        derivedStateOf { "Add to Cart for ${UsdFormat.format(state.totalCents)}" }
    }
    val topProps = rememberTopProps(state)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            NavBar(
                config = NavBarConfig.BackOnly,
                onClick = { if (it is NavBarAction.Back) onAction(ProductDetailsScreenAction.ClickBack) }
            )
        }
    ) { innerPadding ->
        Adaptive(
            layout = layout,
            mobile = {
                MobileContent(
                    top = topProps,
                    toppings = state.toppings,
                    qtyOf = state::qtyOf,
                    onAction = onAction,
                    contentPadding = innerPadding,
                    buttonText = totalText
                )
            },
            wide = {
                WideContent(
                    top = topProps,
                    toppings = state.toppings,
                    qtyOf = state::qtyOf,
                    onAction = onAction,
                    contentPadding = innerPadding,
                    buttonText = totalText
                )
            }
        )
    }
}

/* ---------- Mobile layout ---------- */

@Composable
private fun MobileContent(
    top: TopProps,
    toppings: List<ToppingUi>,
    qtyOf: (String) -> Int,
    onAction: (ProductDetailsScreenAction) -> Unit,
    contentPadding: PaddingValues,
    buttonText: String
) {
    val gridState = rememberLazyGridState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TopSection(
            title = top.title,
            description = top.description,
            imageUrl = top.imageUrl,
            style = TopSectionStyle.Mobile,
            modifier = Modifier
                .weight(0.7f, fill = true)
                .fillMaxWidth()
        )

            ToppingsSection(
                toppings = toppings,
                qtyOf = qtyOf,
                onAction = onAction,
                gridState = gridState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                buttonText = buttonText,
                shape = RectangleShape
            )

    }
}

/* ---------- Wide layout  ---------- */

@Composable
private fun WideContent(
    top: TopProps,
    toppings: List<ToppingUi>,
    qtyOf: (String) -> Int,
    onAction: (ProductDetailsScreenAction) -> Unit,
    contentPadding: PaddingValues,
    buttonText: String
) {
    val gridState = rememberLazyGridState()

    Row(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Box(
                Modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {
                TopSection(
                    title = top.title,
                    description = top.description,
                    imageUrl = top.imageUrl,
                    style = TopSectionStyle.Wide
                )
            }

            Spacer(Modifier.height(24.dp)
                .weight(0.9f))
        }

        Spacer(Modifier.width(24.dp))

       Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
            ) {
                ToppingsSection(
                    toppings = toppings,
                    qtyOf = qtyOf,
                    onAction = onAction,
                    gridState = gridState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true),
                    buttonText = buttonText,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp
                    )
                )
            }
    }
}

/* ---------- Pieces ---------- */

enum class TopSectionStyle { Mobile, Wide }

@Composable
fun TopSection(
    title: String,
    description: String,
    imageUrl: String,
    style: TopSectionStyle,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        ImageBlock(
            title = title,
            imageUrl = imageUrl,
            style = style,
            modifier = Modifier
                .weight(2f, fill = true)
                .fillMaxWidth()
        )
        InfoBlock(
            title = title,
            description = description,
            style = style,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ImageBlock(
    title: String,
    imageUrl: String,
    style: TopSectionStyle,
    modifier: Modifier = Modifier
) {
    val bgColor = if (style == TopSectionStyle.Mobile)
        MaterialTheme.colorScheme.surface else Color.Transparent

    val overlayColor = if (style == TopSectionStyle.Mobile)
        MaterialTheme.colorScheme.background else Color.Unspecified

    Box(
        modifier = modifier
            .background(bgColor)
            .cutCornerOverlay(
                corner = Corner.BottomEnd,
                radius = 16.dp,
                overlayColor = overlayColor,
                enabled = (style == TopSectionStyle.Mobile)
            )
    ) {
        ProductImage(
            url = imageUrl,
            contentDescription = title,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun InfoBlock(
    title: String,
    description: String,
    style: TopSectionStyle,
    modifier: Modifier = Modifier
) {
    val bgColor = if (style == TopSectionStyle.Mobile)
        MaterialTheme.colorScheme.background else Color.Transparent

    val overlayColor = if (style == TopSectionStyle.Mobile)
        MaterialTheme.colorScheme.surface else Color.Unspecified

    val needsShadow = style == TopSectionStyle.Mobile

    Box(
        modifier = modifier
            .then(
                if (needsShadow) {
                    Modifier.shadow(
                        elevation = 12.dp,
                        spotColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.06f),
                        ambientColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.06f)
                    )
                } else Modifier
            )
            .background(bgColor)
            .cutCornerOverlay(
                corner = Corner.TopStart,
                radius = 16.dp,
                overlayColor = overlayColor,
                enabled = (style == TopSectionStyle.Mobile)
            )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Immutable
data class TopProps(
    val title: String,
    val description: String,
    val imageUrl: String
)

@Composable
private fun rememberTopProps(state: ProductDetailsScreenState): TopProps =
    remember(state.title, state.description, state.imageUrl) {
        TopProps(state.title, state.description, state.imageUrl)
    }

@Composable
private fun ToppingsSection(
    toppings: List<ToppingUi>,
    qtyOf: (String) -> Int,
    onAction: (ProductDetailsScreenAction) -> Unit,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    buttonText: String,
    shape: Shape
) {
    val cardStyle = rememberLpCardStyle()
    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "ADD EXTRA TOPPINGS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 68.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
                    .weight(1f, fill = true),
            ) {
                items(
                    items = toppings,
                    key = { it.id },
                    contentType = { "topping" }
                ) { item ->
                    val id = item.id
                    val qty = qtyOf(id)

                    val onActionState by rememberUpdatedState(onAction)

                    val addOne = remember(id, onActionState) { { onActionState(ProductDetailsScreenAction.AddOne(id)) } }
                    val inc =    remember(id, onActionState) { { onActionState(ProductDetailsScreenAction.Inc(id)) } }
                    val decOrRemove = remember(id, onActionState) { { onActionState(ProductDetailsScreenAction.DecOrRemove(id)) } }

                    ToppingCard(
                        item = item,
                        qty = qty,
                        onAddOne = addOne,
                        onInc = inc,
                        onDecOrRemove = decOrRemove,
                        style = cardStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        ButtonOverlay(
            text = buttonText,
            onClick = { onAction(ProductDetailsScreenAction.ClickAddToCart) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

/* ---------- Preview ---------- */

@Preview(apiLevel = 35, widthDp = 412, heightDp = 917, showBackground = true)
@Composable
private fun ProductDetailsPreview_Mobile() {
    LazyPizzaTheme {
        ProductDetailsScreen(
            state = ProductDetailsScreenState(
                title = "Margherita",
                description = "Tomato sauce, Mozzarella, Fresh basil, Olive oil",
                imageUrl = "https://pl-coding.com/lazypizza/pizza_margherita.png",
                toppings = PreviewToppings.all,
                totalCents = 1299
            ),
            onAction = {}
        )
    }
}


@Preview(apiLevel = 35, widthDp = 840, heightDp = 917, showBackground = true)
@Composable
private fun ProductDetailsPreview_Wide() {
    LazyPizzaTheme {
        ProductDetailsScreen(
            state = ProductDetailsScreenState(
                title = "Margherita",
                description = "Tomato sauce, Mozzarella, Fresh basil, Olive oil",
                imageUrl = "https://pl-coding.com/lazypizza/pizza_margherita.png",
                toppings = PreviewToppings.all,
                totalCents = 1699
            ),
            onAction = {}
        )
    }
}
