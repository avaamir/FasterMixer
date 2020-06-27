package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class ChooseBatchRequest(
    @SerializedName("id")
    val batchId: String
)