package com.walker.fakeecommerce.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.walker.fakeecommerce.R
import com.walker.fakeecommerce.components.ButtonComponent
import com.walker.fakeecommerce.data.cart.CartUIEvents
import com.walker.fakeecommerce.data.cart.CartViewModel
import com.walker.fakeecommerce.data.products.ProductsUIEvent
import com.walker.fakeecommerce.data.products.ProductsViewModel
import com.walker.fakeecommerce.model.Product
import com.walker.fakeecommerce.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productViewModel: ProductsViewModel,
    cartViewModel: CartViewModel
) {

    val product = productViewModel.productsUIState.value.selectedProduct

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product?.title ?: "-")},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.SHOPPING_CART.name)
                    }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        },
        content = {
            ProductDetailContent(cartViewModel, product, it, productViewModel)
        },
    )
}

@Composable
fun ProductDetailContent(
    cartViewModel: CartViewModel,
    product: Product?,
    paddingValues: PaddingValues,
    productViewModel: ProductsViewModel
) {

    val quantity = remember { mutableStateOf(product?.quantity ?: 0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        // Product details section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color.White)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SubcomposeAsyncImage(
                model = product?.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = product?.title ?: "-",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product?.description ?: "-",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = product?.price ?: "=",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product?.category ?: "-",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            QuantityContainer(quantity)

            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(
                value = stringResource(id = R.string.add_to_cart),
                onButtonClicked = {
                    cartViewModel.onEvent(CartUIEvents.AddProductToCart(product?.copy(quantity = quantity.value)))
                    productViewModel.onEvent(ProductsUIEvent.UpdateProductQuantity(product, quantity.value))
                },
                isEnabled = quantity.value > 0,
                imageVector = Icons.Default.ShoppingCart
            )
        }
    }
}

@Composable
fun QuantityContainer(quantity: MutableState<Int>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(border = BorderStroke(1.dp, Color.LightGray)),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            text = stringResource(id = R.string.quantity),
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(5.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            IconButton(
                onClick = {
                    if (quantity.value > 0) {
                        quantity.value--
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Minus"
                )
            }

            Text(
                text = quantity.value.toString(),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = { quantity.value++ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus"
                )
            }
        }
    }
}