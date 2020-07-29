package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("UserName")
    val username: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("CompanyCode")
    val companyCode: String
)