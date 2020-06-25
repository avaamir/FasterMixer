package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.behraz.fastermixer.batch.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MapService {

    private const val Base_API_URL = "https://map.ir/"
    private val token by lazy {
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImJmZjI3NGY3NmE2MzAzZGFkZDk3YTRlMWJmNDI4ZjVhNGNmMzU0N2M2ZGJhNzE3MmY2ODVjMjFjMjg3ZDU5MjI2NmI0OWRlMTQ4MjBlNzViIn0.eyJhdWQiOiI5NzA0IiwianRpIjoiYmZmMjc0Zjc2YTYzMDNkYWRkOTdhNGUxYmY0MjhmNWE0Y2YzNTQ3YzZkYmE3MTcyZjY4NWMyMWMyODdkNTkyMjY2YjQ5ZGUxNDgyMGU3NWIiLCJpYXQiOjE1OTIyODI0NjgsIm5iZiI6MTU5MjI4MjQ2OCwiZXhwIjoxNTk0ODc0NDY4LCJzdWIiOiIiLCJzY29wZXMiOlsiYmFzaWMiXX0.JaZs7XHw9I6Y34B_KrGFv2tM4M7usse4MA49N7Rwn9MJCyhPniHli2tOgJ5DVPnZKld4hjNDikD1qossTMQ9eaTtIuGDy9xgFjL2ymNcqVjb0__UIUcPVFPD5uD5wymS19scuasmaldfdOT-DdTmlXvBooEYDyhJl76Ic9w0o8lRiX4uI8XmY-lA2i24msSL-F5i0sTbogkO5JZRJq9ilDMkiz9anj1xD1G4Vu0RGWJzbTLGfmHkJku8K2qa5zhtMvAD9PC7G8UN-9whbdu0ig2MTyrr699ArMc87kNw5c7iqOzZDoeX4JxM4iOtraZZ1Bp2eKCZ3BzviBhfQ6HrAg"
    }

    val client: MapClient by lazy {
        retrofitBuilder.build().create(MapClient::class.java)
    }

    private var internetConnectionListener: ApiService.InternetConnectionListener? = null

    private val retrofitBuilder: Retrofit.Builder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addNetworkInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .removeHeader("Content-Type")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", token)
                    .build()
                chain.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            addInterceptor(object : NetworkConnectionInterceptor() {
                override fun isInternetAvailable(): Boolean {
                    return ApiService.isNetworkAvailable()
                }

                override fun onInternetUnavailable() {
                    CoroutineScope(Main).launch {
                        internetConnectionListener?.onInternetUnavailable()
                    }
                }

            })
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }.build()

        Retrofit.Builder()
            .baseUrl(Base_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Synchronized
    fun setInternetConnectionListener(action: ApiService.InternetConnectionListener) {
        internetConnectionListener = action
        println("debugt: apiServiceLevel: attached")
    }

    @Synchronized
    fun removeInternetConnectionListener(action: ApiService.InternetConnectionListener) {
        if (action == internetConnectionListener) {
            internetConnectionListener = null
            println("debugt: apiServiceLevel: removed")
        }
    }


}