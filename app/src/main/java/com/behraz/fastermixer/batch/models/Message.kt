package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.behraz.fastermixer.batch.models.requests.behraz.DtoMapper
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class AdminMessageDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val content: String,
    @SerializedName("senderId")
    val senderId: Int?,
    @SerializedName("senderName")
    val senderName: String?,
    @SerializedName("eventName")
    val eventName: String?,
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("equipmentName")
    val equipmentName: String?,

    /*@SerializedName("messageType")
    private val _messageType: Int*/
) : DtoMapper<Message> {

    override fun toEntity() = Message(
        id = id.toString(),
        senderName = ("${senderName ?: ""} " + (equipmentName ?: "")).trim(),
        content = content,
        senderId = 0,
        eventName = senderName ?: eventName ?: "نامشخص",
        dateTime = dateTime,
        viewed = false,
        userId = UserConfigs.user.value?.id ?: 0
    )
}

data class MessageDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val content: String,
    @SerializedName("senderId")
    val senderId: Int,
    @SerializedName("senderName")
    val senderName: String,
    @SerializedName("eventName")
    val eventName: String,
    @SerializedName("dateTime")
    val dateTime: String
) : DtoMapper<Message> {

    override fun toEntity() = Message(
        id = id.toString(),
        senderName = senderName,
        content = content,
        senderId = 0,
        eventName = senderName,
        dateTime = dateTime,
        viewed = false,
        userId = UserConfigs.user.value?.id ?: 0
    )
}


@Parcelize
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String,
    val content: String,
    val senderId: Int,
    val senderName: String,
    val eventName: String,
    val dateTime: String,

    var viewed: Boolean,
    var userId: Int?,
) : Parcelable