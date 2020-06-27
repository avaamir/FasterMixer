package com.behraz.fastermixer.batch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_tb")
data class User(
    @PrimaryKey
    @SerializedName("personId")
    val personId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("telephoneList")
    val phones: List<Phone>,
    @SerializedName("token")
    val token: String,
    @SerializedName("profilePic")
    val profilePic: String?,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("adminPhones")
    val adminPhones: List<Phone>
)