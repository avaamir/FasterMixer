package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class SeenMessageRequest(@SerializedName("id") val id: List<String>)