package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.google.gson.annotations.SerializedName

data class MixerMission(
    @SerializedName("destLocation")
    private val _destLocation: String?,
    @SerializedName("conditionTitle")
    val conditionTitle: String?,
    @SerializedName("missionID")
    val missionId: String?,
    @SerializedName("startMissionTime")
    val startMissionTime: String?,
    @SerializedName("endMissionTime")
    val endMissionTime: String?
) {
    val destLocation: CircleFence? get() = if (_destLocation != null) CircleFence.circleFenceToCenterGeoPoint(_destLocation) else null
}