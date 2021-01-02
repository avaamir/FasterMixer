package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.google.gson.annotations.SerializedName


data class ApiResult<T : Any> internal constructor(
    @SerializedName("data")
    val entity: T?,
    @SerializedName("isSuccess")
    val isSucceed: Boolean,
    @SerializedName("message")
    private val _message: String?,
    @Transient var errorType: ErrorType
) {
    val message
        get() = _message ?: if (isSucceed) Constants.SERVER_SUCCEED else Constants.SERVER_ERROR
}

enum class ErrorType(val code: Int) {
    Unknown(0),
    NetworkError(1),
    OK(200),
    UnAuthorized(401),
    Forbidden(403),
    ServerError(500)
}

fun Int.parseHttpCodeToErrorType() =
    when (this) {
        in 200..299 -> {
            ErrorType.OK
        }
        401 -> {
            ErrorType.UnAuthorized
        }
        403 -> {
            ErrorType.Forbidden
        }
        500 -> {
            ErrorType.ServerError
        }
        else -> ErrorType.Unknown
    }


fun <T : Any> failedRequest(errorType: ErrorType, message: String? = null): ApiResult<T> {
    return ApiResult(null, false, message, errorType)
}

fun <T : Any> succeedRequest(t: T): ApiResult<T> =
    ApiResult(t, true, Constants.SERVER_SUCCEED, ErrorType.OK)

data class EntityRequest<T>(
    @SerializedName("data")
    val entity: T
)