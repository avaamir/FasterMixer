package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.JalaliCalendar
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable {
    fun toMessage() = Message(
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
) : Parcelable {

    companion object {
        fun newMessage(id: String, senderName: String, content: String, dateTime: String): Message {
            return Message(
                id = id,
                senderName = senderName,
                content = content,
                senderId = 0,
                eventName = senderName,
                dateTime = dateTime,
                viewed = false,
                userId = UserConfigs.user.value?.id ?: 0
            )
        }
    }
}
