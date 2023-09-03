package com.walker.fakeecommerce.model

data class Product(
    val id: Int,
    val images: List<String>? = null,
    val title: String,
    val description: String,
    val price: String,
    val category: Category,
    var quantity: Int = 0
)