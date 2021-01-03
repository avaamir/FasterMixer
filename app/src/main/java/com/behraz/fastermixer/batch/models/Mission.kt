package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.MissionCondition
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.google.gson.annotations.SerializedName
import java.util.*

data class Mission(
    //moshtarak beyn pomp va mixer
    @SerializedName("missionId")
    private val _missionId: Int,
    @SerializedName("address")
    val address: String,
    @SerializedName("requestLocation")
    private val _requestLocation: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("batchLocation")
    private val _batchLocation: String?,
    //=====
    //faghat mixer darad
    @SerializedName("destinationLocation")
    private val _destLocation: String, //makani ke server tashkhis dade bayad bere
    @SerializedName("conditionId")
    private val _conditionId: Int?,
    @SerializedName("startMissionTime")
    val startMissionTime: Date?,
) {

    val missionCondition
        get() = if (_conditionId == null)
            MissionCondition.Created
        else
            getEnumById(MissionCondition::id, _conditionId)

    val requestLocation get() = if (_requestLocation != null) Fence.strToFence(_requestLocation) else null
    val batchLocation get() = if (_batchLocation != null) Fence.strToFence(_batchLocation) else null


    val missionId: String get() = "${_missionId}_${missionCondition.id}" //missionId for entire lifecycle of a service is same, so we combine it with missionCondition to make our unique missionId

    val summery
        get() = (missionCondition.title) + "آدرس: $address" + if (description.isNullOrBlank()) ""
        else "\r\nتوضیحات: $description"

    companion object {
        val NoMission = Mission(
            _missionId = 1, //IN RO DOROST KON
            address = "",
            _requestLocation = null,
            description = null,
            _batchLocation = null,
            _destLocation = "",
            _conditionId = null,
            startMissionTime = null
        )
    }

    val destFence: Fence get() = Fence.strToFence(_destLocation)
}