package com.alexmls.lazypizza.catalog.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexmls.lazypizza.R
import com.alexmls.lazypizza.catalog.presentation.components.OtherProductCard
import com.alexmls.lazypizza.catalog.presentation.components.PizzaCard
import com.alexmls.lazypizza.catalog.presentation.components.ProductCallbacks
import com.alexmls.lazypizza.catalog.presentation.model.CategorySectionUi
import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi
import com.alexmls.lazypizza.catalog.presentation.model.SectionItemUi
import com.alexmls.lazypizza.catalog.presentation.preview.PreviewProducts
import com.alexmls.lazypizza.catalog.presentation.utils.CATEGORY_ORDER
import com.alexmls.lazypizza.catalog.presentation.utils.buildSectionStartIndex
import com.alexmls.lazypizza.catalog.presentation.utils.displayName
import com.alexmls.lazypizza.catalog.presentation.utils.titleRes
import com.alexmls.lazypizza.core.common.openDialer
import com.alexmls.lazypizza.core.designsystem.Adaptive
import com.alexmls.lazypizza.core.designsystem.LayoutType
import com.alexmls.lazypizza.core.designsystem.components.LpSearchField
import com.alexmls.lazypizza.core.designsystem.components.NavBar
import com.alexmls.lazypizza.core.designsystem.components.NavBarAction
import com.alexmls.lazypizza.core.designsystem.components.NavBarConfig
import com.alexmls.lazypizza.core.designsystem.components.NoResultsMsg
import com.alexmls.lazypizza.core.designsystem.rememberLayoutType
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { e ->
            when (e) {
                is HomeEvent.NavigateToDetails -> {
                    onNavigateToDetails(e.productId)
                }
                is HomeEvent.Dial -> openDialer(context, e.number)
            }
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    layout: LayoutType = rememberLayoutType()
) {
    val act by rememberUpdatedState(onAction)

    val filteredItems by remember(state.items, state.search) {
        derivedStateOf {
            val q = state.search.trim().lowercase()
            state.items
                .asSequence()
                .filter { q.isEmpty() || it.name.lowercase().contains(q) }
                .toList()
        }
    }
    val sections by remember(filteredItems, state.qty) {
        derivedStateOf {
            CATEGORY_ORDER.mapNotNull { cat ->
                val list = filteredItems
                    .filter { it.category == cat }
                    .map { p -> SectionItemUi(product = p, qty = state.qty[p.id] ?: 0) }
                if (list.isEmpty()) null else CategorySectionUi(cat, list)
            }
        }
    }
    val listState  = rememberLazyListState()
    val gridState  = rememberLazyGridState()
    val scope      = rememberCoroutineScope()

    val sectionStart by remember(sections) {
        derivedStateOf { buildSectionStartIndex(sections) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavBar(
                config = NavBarConfig.TitleWithPhone(state.title, state.phone),
                onClick = { navAction ->
                    if (navAction is NavBarAction.Phone) {
                        act(HomeAction.ClickPhone(state.phone))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            HeroBanner(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            )
            LpSearchField(
                query = state.search,
                onQueryChange = { query -> act(HomeAction.SearchChanged(query))  },
                modifier = Modifier.fillMaxWidth()
            )
            CategoryList(
                categories = state.categories,
                onCategoryClick = { cat ->
                    val target = sectionStart[cat] ?: return@CategoryList
                    scope.launch {
                        if (layout == LayoutType.Wide) {
                            gridState.animateScrollToItem(target)
                        } else {
                            listState.animateScrollToItem(target)
                        }
                    }
                }
            )

            Box(Modifier.weight(1f)) {
                if (filteredItems.isEmpty()) {
                    NoResultsMsg()
                } else {
                    Adaptive(
                        layout = layout,
                        mobile = {
                            CategorizedLazyColumn(
                                sections = sections,
                                onAction = onAction,
                                listState = listState
                            )
                        },
                        wide = {
                            CategorizedGrid2Cols(
                                sections = sections,
                                onAction = onAction,
                                gridState = gridState
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroBanner(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.banner),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .height(140.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun CategoryList(
    categories: List<CategoryUi>,
    onCategoryClick: (CategoryUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val onClick by rememberUpdatedState(onCategoryClick)
    val containerColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val border = ButtonDefaults.outlinedButtonBorder(enabled = true)
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.name }) { category ->
            Surface(
                shape = MaterialTheme.shapes.small,
                border = border,
                color = containerColor,
                onClick = { onClick(category) }
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(category.titleRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(category: CategoryUi, modifier: Modifier = Modifier) {
    Text(
        text = category.displayName(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
}

@Composable
fun CategorizedLazyColumn(
    sections: List<CategorySectionUi>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    val act by rememberUpdatedState(onAction)
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = modifier
    ) {
        sections.forEach { section ->
            stickyHeader(
                key = "header_${section.category.name}"
            ) {
                CategoryHeader(section.category)
            }

            items(
                items = section.items,
                key = { it.product.id },
                contentType = { sectionItem ->
                    when (sectionItem.product.category) {
                        CategoryUi.Pizza -> "pizza"
                        else             -> "other"
                    }
                }
            ) { sectionItem ->
                val cardModifier = Modifier
                    .padding(vertical = 4.dp)
                val id = sectionItem.product.id
                val callbacks = remember(id, act, sectionItem.product.category) {
                    if (sectionItem.product.category == CategoryUi.Pizza) {
                        ProductCallbacks(
                            open   = { act(HomeAction.OpenDetails(id)) },
                            add    = { act(HomeAction.Add(id)) },
                            inc    = { act(HomeAction.Inc(id)) },
                            dec    = { act(HomeAction.Dec(id)) },
                            remove = { act(HomeAction.Remove(id)) }
                        )
                    } else {
                        ProductCallbacks(
                            open   = {},
                            add    = { act(HomeAction.Add(id)) },
                            inc    = { act(HomeAction.Inc(id)) },
                            dec    = { act(HomeAction.Dec(id)) },
                            remove = { act(HomeAction.Remove(id)) }
                        )
                    }
                }
                when (sectionItem.product.category) {
                    CategoryUi.Pizza -> {
                        PizzaCard(
                            item = sectionItem.product,
                            qty = sectionItem.qty,
                            callbacks = callbacks,
                            modifier = cardModifier
                        )
                    }
                    else -> {
                        OtherProductCard(
                            item = sectionItem.product,
                            qty = sectionItem.qty,
                            callbacks = callbacks,
                            modifier = cardModifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorizedGrid2Cols(
    sections: List<CategorySectionUi>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    val act by rememberUpdatedState(onAction)
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        sections.forEach { section ->
            item(
                key = "header_${section.category.name}",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CategoryHeader(
                    category = section.category,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(
                items = section.items,
                key = { it.product.id },
                contentType = { sectionItem ->
                    when (sectionItem.product.category) {
                        CategoryUi.Pizza -> "pizza"
                        else             -> "other"
                    }
                }
            ) { sectionItem ->
                val cardModifier = Modifier
                    .fillMaxWidth()
                val id = sectionItem.product.id
                val callbacks = remember(id, act) {
                    ProductCallbacks(
                        open   = { act(HomeAction.OpenDetails(id)) },
                        add    = { act(HomeAction.Add(id)) },
                        inc    = { act(HomeAction.Inc(id)) },
                        dec    = { act(HomeAction.Dec(id)) },
                        remove = { act(HomeAction.Remove(id)) }
                    )
                }
                when (sectionItem.product.category) {
                    CategoryUi.Pizza -> {
                        PizzaCard(
                            item = sectionItem.product,
                            qty = sectionItem.qty,
                            callbacks = callbacks,
                            modifier = cardModifier
                        )
                    }
                    else -> {
                        OtherProductCard(
                            item = sectionItem.product,
                            qty = sectionItem.qty,
                            callbacks = callbacks,
                            modifier = cardModifier
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 412, heightDp = 800, showBackground = true, backgroundColor = 0xFFFAFBFC)
@Composable
private fun HomePreview_Mobile() {
    val state = previewHomeState()
    LazyPizzaTheme {
        HomeScreen(
            state = state.copy(selected = null),
            onAction = {}
        )
    }
}
@Preview(widthDp = 1000, heightDp = 800, showBackground = true, backgroundColor = 0xFFF0F3F6)
@Composable
private fun HomePreview_Wide() {
    val state = previewHomeState()
    LazyPizzaTheme {
        HomeScreen(
            state = state,
            onAction = {},
            layout = LayoutType.Wide
        )
    }
}

private fun previewHomeState(): HomeState {
    val items = PreviewProducts.basic4
    val qty = items.associate { it.id to 0 }
    return HomeState(
        title = "LazyPizza",
        phone = "+1 (555) 321-7890",
        isLoading = false,
        search = "",
        selected = null,
        categories = CategoryUi.entries,
        items = items,
        qty = qty
    )
}