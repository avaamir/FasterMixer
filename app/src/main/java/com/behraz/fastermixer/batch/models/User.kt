package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_tb")
@Parcelize
data class User(
    @PrimaryKey
    private val _id: Int = 0,
    val name: String,
    val phone: String,
    val profilePic: String? = null,
    val token: String? = null
) : Parcelable {
    val id: Int get() = _id
}