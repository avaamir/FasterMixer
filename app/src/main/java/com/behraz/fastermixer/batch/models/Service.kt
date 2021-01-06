package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.ServiceState
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("vehicleId")
    val id: Int,
    @SerializedName("vehicleName")
    val vehicleName: String,
    @SerializedName("conditionType")
    private val _conditionType: Int,
    @SerializedName("loadingMachineTime")
    val loadingTime: String?,
    @SerializedName("moveToDestinationTime")
    val toDestTime: String?,
    @SerializedName("drainTime")
    val unLoadingTime: String?,
    @SerializedName("completedTime")
    val unLoadingCompletedTime: String?,
) {
    val serviceState get() = getEnumById(ServiceState::id, _conditionType)
}