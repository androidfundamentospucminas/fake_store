package com.walker.fakeecommerce.network

import com.walker.fakeecommerce.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.readToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}