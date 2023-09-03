package com.walker.fakeecommerce.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.walker.fakeecommerce.R
import com.walker.fakeecommerce.components.ButtonComponent
import com.walker.fakeecommerce.data.login.LoginUIEvent
import com.walker.fakeecommerce.data.products.ProductsUIEvent
import com.walker.fakeecommerce.data.products.ProductsViewModel
import com.walker.fakeecommerce.model.Product
import com.walker.fakeecommerce.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavHostController, viewModel: ProductsViewModel) {

    val products = viewModel.productsUIState.value.allProducts
    val isLoading = viewModel.productsUIState.value.productsAreLoading
    val hasError = viewModel.productsUIState.value.productLoadingError
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.PROFILE_SCREEN.name)
                    }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.SHOPPING_CART.name)
                    }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        },
        content = {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            if (hasError) {
                Column(modifier = Modifier.padding(it)) {
                    Text(text = stringResource(id = R.string.error_loading))
                    ButtonComponent(
                        value = stringResource(id = R.string.retry),
                        onButtonClicked = {
                            viewModel.onEvent(ProductsUIEvent.GetProducts)
                        },
                        imageVector = Icons.Default.ArrowCircleDown
                    )
                }
            }
            if (!isLoading && !hasError) {
                if (products.isNotEmpty()) {
                    ProductList(products, it, onItemClick = { product ->
                        viewModel.onEvent(
                            ProductsUIEvent.OpenProductDetail(
                                product,
                                onNavigate = {
                                    navController.navigate(Screen.PRODUCT_DETAIL_SCREEN.name)
                                }
                            )
                        )
                    }, listState)
                } else {
                    // viewModel.onEvent(ProductsUIEvent.GetProducts)
                    viewModel.onEvent(ProductsUIEvent.GetProductsPaginated)
                }
            }
        },
    )


    listState.OnBottomReached {
        // do on load more
        viewModel.onEvent(ProductsUIEvent.GetProductsPaginated)
    }
}

@Composable
fun ProductList(products: List<Product>, paddingValues: PaddingValues, onItemClick: (Product?) -> Unit, listState: LazyListState) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
        Text(text = "Produtos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 25.dp, bottom = 15.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(products.chunked(2)) { productList ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (product in productList) {
                        Column(
                            Modifier
                                .weight(1f)
                                .padding(10.dp)
                        ) {
                            ProductItem(product = product, onItemClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product?, onItemClick: (Product?) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = { onItemClick(product) })
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SubcomposeAsyncImage(
                model = product?.images?.get(0),
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = product?.title ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product?.price ?: "", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore : () -> Unit
){
    val shouldLoadMore = remember {
        derivedStateOf {

            // Pega último item visível
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            // Checa de último item visível é o último item da lista
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}