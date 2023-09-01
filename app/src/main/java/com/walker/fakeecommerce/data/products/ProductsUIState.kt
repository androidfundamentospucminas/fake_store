package com.walker.fakeecommerce.data.products

import com.walker.fakeecommerce.model.Product

data class ProductsUIState(
    var selectedProduct: Product? = null,
    val allProducts: List<Product> = listOf(
        Product(
            1,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 1",
            "Description of Product 1",
            "$19.99",
            "Category A"
        ),
        Product(
            2,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 2",
            "Description of Product 2",
            "$29.99",
            "Category B"
        ),
        Product(
            3,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 1",
            "Description of Product 1",
            "$19.99",
            "Category A"
        ),
        Product(
            4,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 2",
            "Description of Product 2",
            "$29.99",
            "Category B"
        ),
        Product(
            5,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 1",
            "Description of Product 1",
            "$19.99",
            "Category A"
        ),
        Product(
            6,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 2",
            "Description of Product 2",
            "$29.99",
            "Category B"
        ),
        Product(
            7,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 1",
            "Description of Product 1",
            "$19.99",
            "Category A"
        ),
        Product(
            8,
            "https://m.media-amazon.com/images/I/518bzP8VW1L.__AC_SY445_SX342_QL70_ML2_.jpg",
            "Product 2",
            "Description of Product 2",
            "$29.99",
            "Category B"
        ),
    )
)