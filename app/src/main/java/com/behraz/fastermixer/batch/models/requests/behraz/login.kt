package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("companyid")
    val factoryCode: String,
) {
    @SerializedName("grant_type")
    private val grantType = "password" //needed by server
}


class GetTokenResponse(
    @SerializedName("access_token")
    val token: String?
)