package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.getEnumById
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
        get() = _message ?: if (isSucceed)
            Constants.SERVER_SUCCEED
        else
            errorType.message

    /*override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApiResult<*>) return false
        return Objects.equals(entity, other.entity) &&
                (_message == other._message) &&
                (isSucceed == other.isSucceed) &&
                (errorType == other.errorType)
    }


    override fun hashCode(): Int {
        return Objects.hash(entity, _message, isSucceed, errorType)
    }

     fun copy(
        entity: T? = this.entity,
        isSucceed: Boolean = this.isSucceed,
        _message: String? = this._message,
        errorType: ErrorType = this.errorType
    ) = ApiResult(entity, isSucceed, _message, errorType)

    override fun toString(): String {
        return "[entity=$entity, isSucceed=$isSucceed, _message=$_message, errorType=$errorType]"
    }*/
}

interface DtoMapper<TO> {
    fun toEntity(): TO
}


enum class ErrorType(val code: Int, val message: String) {
    Unknown(0, Constants.SERVER_ERROR),
    NetworkError(1, "خطا در اینترنت دستگاه"),
    OK(200, "موفق"),
    BadRequest(400, "پارامترهای ارسالی نادرست است"),
    UnAuthorized(401, "نیاز به ورود مجدد"),
    Forbidden(403, "عدم دسترسی"),
    NotFound(404, "منبع یافت نشد"),
    ServerError(500, Constants.SERVER_ERROR)
}

fun Int.parseHttpCodeToErrorType() =
    when (this) {
        in 200..299 -> {
            ErrorType.OK
        }
        504 -> ErrorType.NetworkError
        else -> getEnumById(ErrorType::code, this)
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