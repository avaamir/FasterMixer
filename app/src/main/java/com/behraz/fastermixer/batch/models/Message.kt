package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String,
    val sender: String,
    val content: String,
    val senderImage: String?,
    val isEmergency: Boolean
) : Parcelable