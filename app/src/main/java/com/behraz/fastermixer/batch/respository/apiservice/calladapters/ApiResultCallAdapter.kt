package com.behraz.fastermixer.batch.respository.apiservice.calladapters

import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType
import com.behraz.fastermixer.batch.models.requests.behraz.failedRequest
import com.behraz.fastermixer.batch.models.requests.behraz.parseHttpCodeToErrorType
import okhttp3.Request
import okhttp3.ResponseBody
import okio.IOException
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultCall<T : Any>(
    private val delegate: Call<ApiResult<T>>,
    private val errorBodyConverter: Converter<ResponseBody, ApiResult<T>>
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<ApiResult<T>> {
            override fun onResponse(call: Call<ApiResult<T>>, response: Response<ApiResult<T>>) {
                val body = response.body()
                val errorType = response.code().parseHttpCodeToErrorType()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(body.also {
                                it.errorType = errorType
                            })
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(
                                failedRequest(
                                    errorType
                                )
                            )
                        )
                    }
                } else {
                    val errorBody = response.errorBody()
                    val apiResult = if (errorBody != null && errorBody.contentLength() != -1L) {
                        try {
                            errorBodyConverter.convert(errorBody)
                                ?.also {
                                    it.errorType = errorType
                                }
                                ?: failedRequest(
                                    errorType
                                )
                        } catch (ex: Exception) {
                            failedRequest(errorType)
                        }
                    } else {
                        failedRequest(errorType)
                    }
                    callback.onResponse(this@ApiResultCall, Response.success(apiResult))
                }
            }

            override fun onFailure(call: Call<ApiResult<T>>, t: Throwable) {
                callback.onResponse(
                    this@ApiResultCall, Response.success(
                        failedRequest(
                            when (t) {
                                is IOException -> ErrorType.NetworkError
                                else -> ErrorType.Unknown
                            }, t.message
                        )
                    )
                )
            }

        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = delegate.clone()

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()


    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

}

class ApiResultCallAdapter<T : Any>(
    private val responseType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ApiResult<T>>
) : CallAdapter<ApiResult<T>, Call<ApiResult<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<ApiResult<T>>): Call<ApiResult<T>> {
        return ApiResultCall(call, errorBodyConverter)
    }
}

class ApiResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType) //Must be an ApiResult
        // if the response type is not ApiResult then we can't handle this type, so we return null
        if (getRawType(responseType) != ApiResult::class.java) {
            return null
        }

        // the response type is ApiResult and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as ApiResult<Foo> or ApiResult<out Foo>" }

        //val successBodyType = getParameterUpperBound(0, responseType)
        //val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter = retrofit.nextResponseBodyConverter<ApiResult<Any>>(
            null,
            responseType,
            annotations
        ) //ErrorBody va SuccessBody yeksan hast

        return ApiResultCallAdapter(responseType, errorBodyConverter)
    }

}

