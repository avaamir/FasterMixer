package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Response

class GlobalErrorHandlerInterceptor(private val errorHandler: ApiResponseErrorHandler) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            val bodyString = response.body?.toString()
            errorHandler.onHandleError(response.code, bodyString)
        }
        return response
    }

    interface ApiResponseErrorHandler {
        fun onHandleError(code: Int, errorBody: String?)
    }
}


fun <T> String.fromJsonToModel(): T {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this, type)
}
