package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.ServiceState
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.google.gson.annotations.SerializedName
import java.util.*

data class Mission(
    //moshtarak beyn pomp va mixer
    @SerializedName("missionId")
    private val _missionId: Int,
    @SerializedName("address")
    private val _address: String,
    @SerializedName("requestLocation")
    private val _requestLocation: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("batchLocation")
    private val _batchLocation: String,
    //=====
    //faghat mixer darad
    @SerializedName("destinationLocation")
    private val _destLocation: String?, //makani ke server tashkhis dade bayad bere
    @SerializedName("conditionId")
    private val _conditionId: Int?,
    @SerializedName("startMissionTime")
    val startMissionTime: Date?,
) {

    private val address get() = if(_address.isNotBlank()) _address else "ندارد"

    val serviceState
        get() = if (_conditionId == null)
            ServiceState.Unknown
        else
            getEnumById(ServiceState::id, _conditionId)

    val requestLocation get() = Fence.strToFence(_requestLocation)
    val batchLocation get() = Fence.strToFence(_batchLocation)
    val destFence: Fence get() = Fence.strToFence(_destLocation ?: _requestLocation)

    val missionId: String get() = "${_missionId}_${serviceState.id}" //missionId for entire lifecycle of a service is same, so we combine it with missionCondition to make our unique missionId

    val summery
        get() = (if (serviceState != ServiceState.Created)
            serviceState.title + "\r\n"
        else
            "") + "آدرس: $address " +
                if (description.isNullOrBlank())
                    ""
                else
                    "\r\nتوضیحات: $description"

    companion object {
        val NoMission = Mission(
            _missionId = 1, //IN RO DOROST KON
            _address = "",
            _requestLocation = "",
            description = null,
            _batchLocation = "",
            _destLocation = "",
            _conditionId = null,
            startMissionTime = null
        )
    }

}