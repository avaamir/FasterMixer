package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

class CheckMacResponse(
    @SerializedName("valid")
    val isValid: Boolean
)