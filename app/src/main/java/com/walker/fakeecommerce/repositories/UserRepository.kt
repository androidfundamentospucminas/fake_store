package com.walker.fakeecommerce.repositories

import com.walker.fakeecommerce.datasources.UserDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {

    suspend fun postUser(name: String, email: String, password: String, avatar: String) =
        userDataSource.postUser(name, email, password, avatar)

    suspend fun postLogin(email: String, password: String) =
        userDataSource.postLogin(email, password)

    suspend fun getProfile() =
        userDataSource.getProfile()
}