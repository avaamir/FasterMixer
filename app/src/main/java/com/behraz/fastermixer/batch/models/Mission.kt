package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.Fence
import com.google.gson.annotations.SerializedName
import java.util.*

data class Mission(
    @SerializedName("destLocation")
    private val _destLocation: String, //makani ke server tashkhis dade bayad bere
    @SerializedName("conditionTitle")
    val conditionTitle: String,
    @SerializedName("missionID")
    private val _missionId: String,
    @SerializedName("startMissionTime")
    val startMissionTime: Date?,
    @SerializedName("endMissionTime")
    val endMissionTime: String?,
    @SerializedName("clientDatetime")
    val dataDateTime: Date?,
    @SerializedName("address")
    val address: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("requestLocation")
    private val _requestLocation: String?,
    @SerializedName("batchLocation")
    private val _batchLocation: String?
) {

    val requestLocation get() = if (_requestLocation != null) Fence.strToFence(_requestLocation) else null
    val batchLocation get() = if (_batchLocation != null) Fence.strToFence(_batchLocation) else null


    val missionId get() = _missionId + conditionTitle

    val summery get() = (if (conditionTitle.isNotEmpty()) "$conditionTitle\r\n" else "") + "آدرس: $address" + if (description.isNullOrBlank()) "" else "\r\nتوضیحات: $description"

    companion object {
        val NoMission = Mission(
            "In Ro Dorost KON", "", "0", null, null, null, "", null, null, null
        )
    }

    val destFence: Fence get() = Fence.strToFence(_destLocation)
}