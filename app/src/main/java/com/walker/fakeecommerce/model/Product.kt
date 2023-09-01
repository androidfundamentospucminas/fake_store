package com.walker.fakeecommerce.model

data class Product(
    val id: Int,
    val image: String,
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    var quantity: Int = 0
)