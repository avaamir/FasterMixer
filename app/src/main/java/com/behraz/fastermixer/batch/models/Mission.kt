package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.google.gson.annotations.SerializedName
import java.util.*

data class Mission(
    @SerializedName("destLocation")
    private val _destLocation: String,
    @SerializedName("conditionTitle")
    val conditionTitle: String,
    @SerializedName("missionID")
    val missionId: String,
    @SerializedName("startMissionTime")
    val startMissionTime: String?,
    @SerializedName("endMissionTime")
    val endMissionTime: String?,
    @SerializedName("clientDatetime")
    val dataDateTime: Date?
) {

    companion object {
        val NoMission = Mission("In Ro Dorost KON", "", "0", null, null, null)
    }

    val destLocation: CircleFence get() = CircleFence.circleFenceToCenterGeoPoint(_destLocation, dataDateTime)
}