package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class NetworkConnectionInterceptor(private val networkAvailability: NetworkAvailability) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (!networkAvailability.isInternetAvailable()) {
            networkAvailability.onInternetUnavailable()
            request = request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24
            ).build()
            val response = chain.proceed(request)
            if (response.cacheResponse == null) {
                //todo cache is unavailable
                println("debug: okhttp: cache is unavailable")
            }
            return response
        }
        return chain.proceed(request)
    }

    interface NetworkAvailability {
        fun onInternetUnavailable()
        fun isInternetAvailable(): Boolean
    }
}