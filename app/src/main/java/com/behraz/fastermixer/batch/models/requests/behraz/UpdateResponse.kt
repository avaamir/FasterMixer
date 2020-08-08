package com.behraz.fastermixer.batch.models.requests.behraz

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UpdateResponse(
    @SerializedName("appVersionID")
    private val appVersionID: String,
    @SerializedName("version")
    val version: Long,
    @SerializedName("link")
    val link: String,
    @SerializedName("fource")
    val isForce: Boolean,
    @SerializedName("description")
    val description: String
) : Parcelable {
    companion object {
        val NoResponse by lazy {
            UpdateResponse(
                "0", 0, "", false, ""
            )
        }
    }
}