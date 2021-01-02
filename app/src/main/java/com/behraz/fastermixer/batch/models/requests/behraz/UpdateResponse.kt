package com.behraz.fastermixer.batch.models.requests.behraz

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateResponse(
    @SerializedName("version")
    val version: Long,
    @SerializedName("url")
    val link: String,
    @SerializedName("fource")
    val isForce: Boolean,
    @SerializedName("description")
    val description: String
) : Parcelable {
    companion object {
        val NoResponse by lazy {
            UpdateResponse(
                0, "", false, ""
            )
        }
    }
}