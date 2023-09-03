package com.walker.fakeecommerce

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.walker.fakeecommerce.datasources.ProductsDataSource
import com.walker.fakeecommerce.datasources.UserDataSource
import com.walker.fakeecommerce.network.ApiService
import com.walker.fakeecommerce.network.AuthInterceptor
import com.walker.fakeecommerce.repositories.ProductsRepository
import com.walker.fakeecommerce.repositories.UserRepository
import com.walker.fakeecommerce.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleDI {

    @Provides
    @Singleton
    fun provideStorageRef(): StorageReference = FirebaseStorage.getInstance().reference

    @Provides
    fun provideBaseUrl() = "https://api.escuelajs.co/api/v1/"

    @Singleton
    @Provides
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideProductsDataSource(apiService: ApiService) =
        ProductsDataSource(apiService)

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource) =
        UserRepository(userDataSource)

    @Provides
    @Singleton
    fun provideUserDataSource(apiService: ApiService) =
        UserDataSource(apiService)

    @Provides
    @Singleton
    fun provideProductsRepository(productsDataSource: ProductsDataSource) =
        ProductsRepository(productsDataSource)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("fake_ecommerce_session_manager", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionManager(sharedPreferences: SharedPreferences): SessionManager =
        SessionManager(sharedPreferences)
}