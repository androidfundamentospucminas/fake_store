package com.walker.fakeecommerce.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.walker.fakeecommerce.data.cart.CartUIEvents
import com.walker.fakeecommerce.data.cart.CartViewModel
import com.walker.fakeecommerce.data.products.ProductsUIEvent
import com.walker.fakeecommerce.data.products.ProductsViewModel
import com.walker.fakeecommerce.model.Product
import com.walker.fakeecommerce.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(navController: NavHostController, cartViewModel: CartViewModel, productViewModel: ProductsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cart")},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = {
            ShoppingCart(
                navController = navController,
                cartViewModel = cartViewModel,
                paddingValues = it,
                productViewModel = productViewModel)
        },
    )
}

@Composable
fun ShoppingCart(navController: NavHostController, cartViewModel: CartViewModel, paddingValues: PaddingValues, productViewModel: ProductsViewModel) {
    val cartItems = cartViewModel.cartUIState.value.products

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = paddingValues.calculateTopPadding(), horizontal = 10.dp)
    ) {
        Text(
            text = "Products List:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display cart items
        LazyColumn {
            items(cartItems) {item ->
                CartItemRow(
                    item,
                    onItemClick = {
                        productViewModel.onEvent(
                            ProductsUIEvent.OpenProductDetail(
                                it,
                                onNavigate = {
                                    navController.navigate(Screen.PRODUCT_DETAIL_SCREEN.name) {
                                        popUpTo(Screen.PRODUCTS_SCREEN.name)
                                    }
                                },
                            )
                        )
                    },
                    onItemRemove = {
                        productViewModel.onEvent(
                            ProductsUIEvent.UpdateProductQuantity(it, 0)
                        )
                        cartViewModel.onEvent(
                            CartUIEvents.RemoveProductFromCart(it)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Pedido realizado!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Checkout")
        }
    }
}

@Composable
fun CartItemRow(item: Product, onItemClick: (Product) -> Unit, onItemRemove: (Product) -> Unit) {
    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onItemClick(item) }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = item.title, fontSize = 16.sp)
            Text(text = "${item.quantity} x ${item.price}", fontSize = 16.sp)
            IconButton(onClick = { onItemRemove(item) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Item")
            }
        }
        Divider(color = Color.LightGray)
    }
}