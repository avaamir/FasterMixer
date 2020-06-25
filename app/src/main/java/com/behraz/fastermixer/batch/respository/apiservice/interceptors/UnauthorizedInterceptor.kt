package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

abstract class UnauthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) { //Unauthorized
            CoroutineScope(Main).launch {
                onUnauthorized()
            }
        }
        return response
    }

    abstract fun onUnauthorized()
}