package com.walker.fakeecommerce.data.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walker.fakeecommerce.network.NetworkResult
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

    private var currentLimit = 10

    fun onEvent(event: ProductsUIEvent) {
        when (event) {
            is ProductsUIEvent.GetProductsPaginated -> {
                viewModelScope.launch {
                    productsUIState.value = productsUIState.value.copy(
                        infiteScrollIsLoading = false
                    )

                    val response =
                        productsRepository.getProductsPaginated(currentOffset, currentLimit)

                    if (response.isSuccessful) {
                        val allProductsTemp = productsUIState.value.allProducts.toMutableList()

                        allProductsTemp.addAll(response.body() ?: emptyList())
                        productsUIState.value = productsUIState.value.copy(
                            allProducts = allProductsTemp,
                            infiteScrollIsLoading = false,
                            productLoadingError = false
                        )
                    } else {
                        productsUIState.value = productsUIState.value.copy(
                            allProducts = listOf(),
                            productLoadingError = true,
                            infiteScrollIsLoading = false
                        )
                    }

                    currentOffset = currentLimit
                    currentLimit += currentLimit
                }
            }
            is ProductsUIEvent.GetProducts -> {
                viewModelScope.launch {
                    productsUIState.value = productsUIState.value.copy(
                        productsAreLoading = true,
                        productLoadingError = false,
                        allProducts = listOf()
                    )

                    val response = productsRepository.getProducts()

                    if (response.isSuccessful) {
                        productsUIState.value = productsUIState.value.copy(
                            allProducts = response.body() ?: emptyList(),
                            productsAreLoading = false,
                            productLoadingError = false
                        )
                    } else {
                        productsUIState.value = productsUIState.value.copy(
                            allProducts = listOf(),
                            productLoadingError = true,
                            productsAreLoading = false
                        )
                    }

                    /*
                    when (response) {
                        is NetworkResult.Success -> {
                            response.data?.let {
                                productsUIState.value = productsUIState.value.copy(
                                    allProducts = it,
                                    productsAreLoading = false,
                                    productLoadingError = false
                                )
                            }
                        }
                        is NetworkResult.Error -> {
                            productsUIState.value = productsUIState.value.copy(
                                allProducts = listOf(),
                                productLoadingError = true,
                                productsAreLoading = false
                            )
                        }

                        is NetworkResult.Loading -> {
                            productsUIState.value = productsUIState.value.copy(
                                productsAreLoading = true,
                                productLoadingError = false,
                                allProducts = listOf()
                            )
                        }
                    } */
                }
            }
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