package com.walker.fakeecommerce

import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.walker.fakeecommerce.datasources.ProductsDataSource
import com.walker.fakeecommerce.datasources.UserDataSource
import com.walker.fakeecommerce.network.ApiService
import com.walker.fakeecommerce.repositories.ProductsRepository
import com.walker.fakeecommerce.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleDI {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences("fake_ecommerce_manager", Context.MODE_PRIVATE)

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

    @Provides
    @Singleton
    fun provideUserReposito(userDataSource: UserDataSource) =
        UserRepository(userDataSource)

    @Provides
    @Singleton
    fun provideProductsDataSource(apiService: ApiService) =
        ProductsDataSource(apiService)

    @Provides
    @Singleton
    fun provideProductsRepository(productsDataSource: ProductsDataSource) =
        ProductsRepository(productsDataSource)

}