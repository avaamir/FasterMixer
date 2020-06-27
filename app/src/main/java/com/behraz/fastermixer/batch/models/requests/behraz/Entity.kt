package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

data class Entity<Entity>(
    @SerializedName("entity")
    val entity: Entity,
    @SerializedName("isSuccess")
    val isSucceed: Boolean,
    @SerializedName("message")
    val message: String?
)

data class EntityRequest<Entity>(
    @SerializedName("entity")
    val entity: Entity
)
