package com.walker.fakeecommerce.network

import retrofit2.HttpException
import retrofit2.Response


suspend fun <T : Any> doRequest(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(message = response.message())
        }
    } catch (e: HttpException) {
        NetworkResult.Error(message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Error(message = e.message)
    }
}