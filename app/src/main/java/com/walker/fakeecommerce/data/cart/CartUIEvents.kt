package com.walker.fakeecommerce.data.cart

import com.walker.fakeecommerce.model.Product

sealed class CartUIEvents {
    data class AddProductToCart(val product: Product?): CartUIEvents()
    data class RemoveProductFromCart(val product: Product?): CartUIEvents()
}