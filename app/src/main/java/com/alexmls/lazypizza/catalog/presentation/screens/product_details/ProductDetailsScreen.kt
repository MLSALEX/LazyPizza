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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.catalog.presentation.components.ButtonOverlay
import com.alexmls.lazypizza.catalog.presentation.components.ProductImage
import com.alexmls.lazypizza.catalog.presentation.components.ToppingCallbacks
import com.alexmls.lazypizza.catalog.presentation.components.ToppingCard
import com.alexmls.lazypizza.catalog.presentation.model.ToppingUi
import com.alexmls.lazypizza.catalog.presentation.preview.PreviewToppings
import com.alexmls.lazypizza.catalog.presentation.utils.UsdFormat
import com.alexmls.lazypizza.core.designsystem.Adaptive
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                    state = state,
                    onAction = onAction,
                    contentPadding = innerPadding,
                    buttonText = totalText
                )
            },
            wide = {
                WideContent(
                    state = state,
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
    state: ProductDetailsScreenState,
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
            title = state.title,
            description = state.description,
            imageUrl = state.imageUrl,
            modifier = Modifier
                .weight(0.7f, fill = true)
                .fillMaxWidth()
        )

            ToppingsSection(
                toppings = state.toppings,
                qtyOf = state::qtyOf,
                onAction = onAction,
                gridState = gridState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                buttonText = buttonText
            )

    }
}

/* ---------- Wide layout  ---------- */

@Composable
private fun WideContent(
    state: ProductDetailsScreenState,
    onAction: (ProductDetailsScreenAction) -> Unit,
    contentPadding: PaddingValues,
    buttonText: String
) {
    val gridState = rememberLazyGridState()

    Row(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(bottom = 100.dp)
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
                    title = state.title,
                    description = state.description,
                    imageUrl = state.imageUrl
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
                    toppings = state.toppings,
                    qtyOf = state::qtyOf,
                    onAction = onAction,
                    gridState = gridState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true),
                    buttonText = buttonText
                )
            }
    }
}

/* ---------- Pieces ---------- */

@Composable
private fun TopSection(
    title: String,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(2f, fill = true)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .cutCornerOverlay(
                    corner = Corner.BottomEnd,
                    radius = 16.dp,
                    overlayColor = MaterialTheme.colorScheme.background
                )
        ) {
            ProductImage(
                url = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    spotColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.06f),
                    ambientColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.06f)
                )
                .background(MaterialTheme.colorScheme.background)
                .cutCornerOverlay(
                    corner = Corner.TopStart,
                    radius = 16.dp,
                    overlayColor = MaterialTheme.colorScheme.surface
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
}

@Composable
private fun ToppingsSection(
    toppings: List<ToppingUi>,
    qtyOf: (String) -> Int,
    onAction: (ProductDetailsScreenAction) -> Unit,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    buttonText: String
) {
    val shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
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
                    key = { it.id }
                ) { item ->
                    val qty = qtyOf(item.id)
                    val cb = remember(item.id) {
                        ToppingCallbacks(
                            addOne = { onAction(ProductDetailsScreenAction.AddOne(item.id)) },
                            inc = { onAction(ProductDetailsScreenAction.Inc(item.id)) },
                            decOrRemove = { onAction(ProductDetailsScreenAction.DecOrRemove(item.id)) }
                        )
                    }
                    ToppingCard(
                        item = item,
                        qty = qty,
                        callbacks = cb
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
