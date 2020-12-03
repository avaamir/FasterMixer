package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.behraz.fastermixer.batch.utils.general.now
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    @SerializedName("messageID")
    val id: String,
    @SerializedName("senderName")
    val sender: String,
    @SerializedName("senderID")
    val senderId: String,
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("text")
    val content: String,
    @SerializedName("state") //TODO not implemented server side
    val isDelivered: Boolean,
    @SerializedName("priority") //TODO not implemented server side
    val priority: Int,
    @SerializedName("messageType")
    val _isEvent: Int, //is Event Or Message?
    val senderImage: String?, //TODO not implemented server side
    var userId: String?,
    @SerializedName("viewed")
    var viewed: Boolean
) : Parcelable {

    companion object {
        fun newMessage(id: String, senderName: String, content: String): Message {
            return Message(id, senderName, "", now().toString(), content, true, 0, 1, null, null, true)
        }
    }

    val shouldPopUp get() = _isEvent != 1
    val isEmergency: Boolean get() = priority == 1  //TODO not implemented server side
}
