package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

abstract class UnauthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.body?.source()?.buffer?.size ?: 2049 <= 2048) {
            response.peekBody(2048).also { body ->
                Gson().fromJson(body.charStream(), Entity::class.java)?.also {
                    if (!it.isSucceed) {
                        if (it.message.contains("Your Not have Access To This Action")) {     //Unauthorized
                            CoroutineScope(Main).launch {
                                onUnauthorized()
                            }
                        }
                    }
                }
            }
        }
        /*if (response.code == 401) { //Unauthorized
            CoroutineScope(Main).launch {
                onUnauthorized()
            }
        }*/
        return response
    }

    abstract fun onUnauthorized()
}