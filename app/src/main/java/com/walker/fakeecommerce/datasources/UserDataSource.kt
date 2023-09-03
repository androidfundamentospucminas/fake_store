package com.walker.fakeecommerce.datasources

import com.walker.fakeecommerce.model.LoginUser
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.model.User
import com.walker.fakeecommerce.network.ApiService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun postUser(name: String, email: String, password: String, avatar: String) =
        apiService.postUser(User(name, email, password, avatar))

    suspend fun postLogin(email: String, password: String) =
        apiService.postLogin(LoginUser(email, password))

    suspend fun getProfile() =
        apiService.getProfile()

    suspend fun updateProfile(profile: Profile) =
        apiService.updateProfile(profile.id, profile)

    suspend fun deleteProfile(id: String) =
        apiService.deleteProfile(id)

}