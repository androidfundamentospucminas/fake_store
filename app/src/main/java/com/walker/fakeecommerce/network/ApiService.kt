package com.walker.fakeecommerce.network

import com.walker.fakeecommerce.model.AccessToken
import com.walker.fakeecommerce.model.LoginUser
import com.walker.fakeecommerce.model.Product
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("products?offset=0&limit=10")
    suspend fun getProducts(): Response<List<Product>>

    @GET("auth/profile")
    suspend fun getProfile(): Response<Profile>

    @PUT("users/{id}")
    suspend fun updateProfile(
        @Path("id") id: String,
        @Body profile: Profile
    ): Response<Profile>

    @DELETE("users/{id}")
    suspend fun deleteProfile(
        @Path("id") id: String
    ): Response<Boolean>

    @POST("users")
    suspend fun postUser(
        @Body user: User
    ): Response<User>

    @POST("auth/login")
    suspend fun postLogin(
        @Body loginUser: LoginUser
    ): Response<AccessToken>

}