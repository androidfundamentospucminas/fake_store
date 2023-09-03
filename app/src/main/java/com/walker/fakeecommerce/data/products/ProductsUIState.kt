package com.walker.fakeecommerce.data.products

import com.walker.fakeecommerce.model.Product

data class ProductsUIState(
    var selectedProduct: Product? = null,
    val allProducts: List<Product> = listOf(),
    var productsAreLoading: Boolean = false,
    var infiteScrollIsLoading: Boolean = false,
    var productLoadingError: Boolean = false
)