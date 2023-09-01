package com.walker.fakeecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.walker.fakeecommerce.composables.LoginScreen
import com.walker.fakeecommerce.composables.ProductDetailScreen
import com.walker.fakeecommerce.composables.ProductsScreen
import com.walker.fakeecommerce.composables.ShoppingScreen
import com.walker.fakeecommerce.composables.SignUpScreen
import com.walker.fakeecommerce.composables.TermsAndPolicyScreen
import com.walker.fakeecommerce.data.cart.CartViewModel
import com.walker.fakeecommerce.data.products.ProductsViewModel
import com.walker.fakeecommerce.navigation.Screen
import com.walker.fakeecommerce.ui.theme.FakeEcommerceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeEcommerceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProductApp()
                }
            }
        }
    }
}

@Composable
fun ProductApp(
    productViewModel: ProductsViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val navController = rememberNavController()

    val products = productViewModel.productsUIState.value.allProducts

    NavHost(navController = navController, startDestination = Screen.LOGIN_SCREEN.name) {
        composable(Screen.LOGIN_SCREEN.name) {
            LoginScreen(navController)
        }
        composable(Screen.PRODUCTS_SCREEN.name) {
            ProductsScreen(products, navController, productViewModel)
        }
        composable(Screen.SIGNUP_SCREEN.name) {
            SignUpScreen(navController)
        }
        composable(Screen.PRODUCT_DETAIL_SCREEN.name) {
            ProductDetailScreen(navController, productViewModel, cartViewModel)
        }
        composable(Screen.SHOPPING_CART.name) {
            ShoppingScreen(navController, cartViewModel, productViewModel)
        }
        composable(Screen.TERMS_AND_POLICY.name) {
            TermsAndPolicyScreen(navController)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FakeEcommerceTheme {
        Greeting("Android")
    }
}