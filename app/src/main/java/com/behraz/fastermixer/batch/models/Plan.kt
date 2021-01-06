package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import com.behraz.fastermixer.batch.models.enums.RequestState
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Plan(
    @SerializedName("id")
    val id: Int,
    @SerializedName("customerFullName")
    val customerName: String,
    @SerializedName("address")
    private val _address: String,
    @SerializedName("deliveryTime")
    val startTime: String,
    @SerializedName("value")
    val plannedAmount: Float,
    @SerializedName("islamp")
    val slump: String,
    @SerializedName("carat")
    val density: String,
    @SerializedName("resistanceCategory")
    val resistance: String,
    @SerializedName("sendValue")
    val sentAmount: Float,

    @SerializedName("requestState")
    private val _requestState: Int,
    @SerializedName("geofencePoint")
    private val _location: String?,
) : Parcelable {
    val address get() = if (_address.isNotBlank()) _address else "نامشخص"

    val locationFence get() = if (_location != null) Fence.strToFence(_location) else null //if requestState == Canceled or Reserved _location could be null
    val requestState get() = getEnumById(RequestState::id, _requestState)

    val waitingAmount get() = plannedAmount - sentAmount
    val progress: Int get() = if (plannedAmount == 0f) 100 else (sentAmount * 100 / plannedAmount).toInt()
}