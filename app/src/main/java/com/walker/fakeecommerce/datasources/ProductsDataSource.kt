package com.walker.fakeecommerce.datasources

import com.walker.fakeecommerce.network.ApiService
import javax.inject.Inject

class ProductsDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts() = apiService.getProducts()

    suspend fun getProductsPaginated(offset: Int, limit: Int) = apiService.getProductsPaginated(offset, limit)
}