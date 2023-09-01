package com.walker.fakeecommerce.data.cart

import com.walker.fakeecommerce.model.Product

data class CartUIState(
    var products: List<Product> = listOf()
)