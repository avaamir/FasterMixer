package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class EmptyBodyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.body?.contentLength() == 0L) {
            request = request.newBuilder().post(
                "{}".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            ).build()
        }

        return chain.proceed(request)
    }
}