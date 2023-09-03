package com.walker.fakeecommerce

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.walker.fakeecommerce.datasources.UserDataSource
import com.walker.fakeecommerce.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleDI {

    @Provides
    @Singleton
    fun provideStorageReference(): StorageReference =
        FirebaseStorage.getInstance().reference

    @Provides
    fun provideBaseUrl() = "https://api.escuelajs.co/api/v1/"

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideUserDataSource(apiService: ApiService) =
        UserDataSource(apiService)

}