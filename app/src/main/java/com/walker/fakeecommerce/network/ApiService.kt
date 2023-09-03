package com.walker.fakeecommerce.network

import com.walker.fakeecommerce.model.AccessToken
import com.walker.fakeecommerce.model.LoginUser
import com.walker.fakeecommerce.model.Product
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Aula 4
    @GET("products")
    suspend fun getProducts() : Response<List<Product>>

    // Aula 7
    @GET("products")
    suspend fun getProductsPaginated(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ) : Response<List<Product>>

    // Aula 5
    @GET("auth/profile")
    suspend fun getProfile(): Response<Profile>

    // Aula 6
    @PUT("users/{id}")
    suspend fun editProfile(
        @Path("id") id: String,
        @Body profile: Profile,
    ): Response<Profile>

    // Aula 6
    @DELETE("users/{id}")
    suspend fun deleteProfile(
        @Path("id") id: String
    ): Response<Boolean>

    // Aula 2
    @POST("users")
    suspend fun postUser(
        @Body user: User
    ): Response<User>

    // Aula 3
    @POST("auth/login")
    suspend fun postLogin(
        @Body loginUser: LoginUser
    ): Response<AccessToken>
}