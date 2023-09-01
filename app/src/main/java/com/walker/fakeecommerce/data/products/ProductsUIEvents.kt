package com.walker.fakeecommerce.data.products

import com.walker.fakeecommerce.model.Product

sealed class ProductsUIEvent{

    data class OpenProductDetail(val product: Product, val onNavigate: () -> Unit): ProductsUIEvent()
    data class UpdateProductQuantity(val product: Product?, val quantity: Int): ProductsUIEvent()
}