package com.walker.fakeecommerce.datasources

import com.walker.fakeecommerce.model.User
import com.walker.fakeecommerce.network.ApiService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun postUser(name: String, email: String, password: String, avatar: String) =
        apiService.postUser(User(name, email, password, avatar))

}