package com.walker.fakeecommerce.data.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walker.fakeecommerce.repositories.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {

    var productsUIState = mutableStateOf(ProductsUIState())

    private var currentOffset = 0
    private val currentLimit = 10

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

            is ProductsUIEvent.GetProducts -> {
                viewModelScope.launch {
                    productsUIState.value = productsUIState.value.copy(
                        productsAreLoading = true,
                        productsLoadingError = false
                    )

                    val response = productsRepository.getProducts(currentOffset, currentLimit)

                    if (response.isSuccessful) {
                        val currentProducts = productsUIState.value.allProducts.toMutableList()

                        currentProducts.addAll(response.body() ?: emptyList())

                        productsUIState.value = productsUIState.value.copy(
                            allProducts = currentProducts,
                            productsAreLoading = false,
                            productsLoadingError = false
                        )
                    } else {
                        productsUIState.value = productsUIState.value.copy(
                            productsAreLoading = false,
                            productsLoadingError = true
                        )
                    }

                    currentOffset += currentLimit
                }
            }
        }
    }
}