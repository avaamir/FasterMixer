package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType
import com.behraz.fastermixer.batch.models.requests.behraz.parseHttpCodeToErrorType
import com.behraz.fastermixer.batch.utils.general.log
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
            log("responseCode:${response.code}::${chain.request().url}")
            errorHandler.onHandleError(response.code.parseHttpCodeToErrorType(), bodyString)
        }
        return response
    }

    interface ApiResponseErrorHandler {
        fun onHandleError(errorType: ErrorType, errorBody: String?)
    }
}


fun <T> String.fromJsonToModel(): T {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this, type)
}
