package com.behraz.fastermixer.batch.respository.apiservice.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthCookieInterceptor : Interceptor { //this class handler session cookies for mmd server
    private var cookie: String? = null


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        val newRequest = cookie?.let { //make new Request and add cookie
            val builder = originalRequest
                .newBuilder()
            builder.addHeader("Cookie", it)
            cookie = null // clear saved cookie from last req
            builder.build()
        }

        val originalResponse = chain.proceed(newRequest ?: originalRequest)

        //read cookie from response and save it for next request
        val cookies = originalResponse.headers("Set-Cookie")
        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                //println("debug: $cookie")
                if (cookie.contains("laravel_session")) {
                    this.cookie = cookie
                }
            }
        }
        return originalResponse
    }
}