package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.google.gson.annotations.SerializedName

data class Entity<Entity>(
    @SerializedName("entity")
    val entity: Entity,
    @SerializedName("isSuccess")
    val isSucceed: Boolean,
    @SerializedName("message")
    val _message: String?
) {
    val message get() = _message ?: Constants.SERVER_ERROR
}

data class EntityRequest<Entity>(
    @SerializedName("entity")
    val entity: Entity
)
