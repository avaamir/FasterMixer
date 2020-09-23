package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.respository.persistance.typeconverter.PhoneListConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "user_tb")
data class User(
    @PrimaryKey
    @SerializedName("personId")
    var personId: String,
    @SerializedName("name")
    var name: String?,
    @SerializedName("token")
    var token: String,
    @SerializedName("profilePic")
    var profilePic: String?,
    @SerializedName("roleId")
    var roleId: Int?,
    @SerializedName("personalCode")
    var personalCode: String?,
    @SerializedName("equipmentId")
    var equipmentId: String?
)  {

    @TypeConverters(PhoneListConverter::class)
    @SerializedName("telephoneList")
    var phones: List<Phone>? = null

    @TypeConverters(PhoneListConverter::class)
    @SerializedName("adminPhones")
    var adminPhones: List<Phone>? = null

    val userType: UserType
        get() = when (roleId) {
            1 -> UserType.Pomp
            2 -> UserType.Mixer
            3 -> UserType.Batch
            4-> UserType.Admin
            else -> throw IllegalAccessError("userType can be 1..3")
        }


}