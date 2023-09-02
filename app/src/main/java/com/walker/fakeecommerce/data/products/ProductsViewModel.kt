package com.walker.fakeecommerce.data.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(): ViewModel() {

    var productsUIState = mutableStateOf(ProductsUIState())

    fun onEvent(event: ProductsUIEvent) {
        when (event) {
            is ProductsUIEvent.OpenProductDetail -> {
                productsUIState.value = productsUIState.value.copy(
                    selectedProduct = event.product
                )
                event.onNavigate()
            }
            is ProductsUIEvent.UpdateProductQuantity -> {
                val allProductsTemp =  productsUIState.value.allProducts.toMutableList()
                allProductsTemp.find { it.id == event.product?.id }?.quantity = event.quantity
                productsUIState.value = productsUIState.value.copy(
                    allProducts = allProductsTemp
                )
                if (productsUIState.value.selectedProduct?.id == event.product?.id) {
                    productsUIState.value.selectedProduct = event.product?.copy(quantity = event.quantity)
                }
            }
        }
    }
}