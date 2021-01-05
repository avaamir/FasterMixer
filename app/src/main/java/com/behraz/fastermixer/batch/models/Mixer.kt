package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.ServiceState
import com.behraz.fastermixer.batch.models.enums.ServiceState.*
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.exhaustive
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
import java.util.*

data class Mixer(
    @SerializedName("vehicleId")
    val id: Int,
    @SerializedName("vehicleName")
    val carName: String,
    @SerializedName("driverName")
    private var _driverName: String?,
    @SerializedName("plaque")
    val pelak: String,
    @SerializedName("capacity")
    val capacity: Float,            // zarifyat machine
    @SerializedName("latitude")
    private val lat: Double,
    @SerializedName("longitude")
    private val lng: Double,
    @SerializedName("timeDifference")
    val lastDataTimeDiff: Double?,
    @SerializedName("owner")
    val owner: String?,
    @SerializedName("speed")
    val speed: Float?,
    @SerializedName("lastDataTime")
    val lastDataTime: Date?,

    @SerializedName("loadInfo")
    val loadInfo: LoadInfo?
) {
    val driverName get() = _driverName ?: ""
    val location: GeoPoint get() = GeoPoint(lat, lng)

    @Transient
    var state: String? = null
}

fun Mixer.normalizeStateByDistance(destArea: Fence): String {
    val meterDistance = destArea.center.distanceToAsDouble(location)
    val temp = meterDistance.toInt() / 100

    var suffix: String? = ""
    loadInfo?.let {
         suffix =  when (it.serviceState) {
            ToDest-> {
                "تا مقصد"
            }
            ToBatch -> {
                "تا بچ"
            }
            Loading, UnLoading, ServiceFinished -> {
                return it.serviceState.title
            }
            Created ->{ null }
        }?.exhaustiveAsExpression()
    }

    return when {
        destArea.contains(location) -> "وارد محدوده شد"
        temp == 0 || temp / 10 == 0 -> "${meterDistance.toInt()} متر" //meter
        temp % 10 == 0 -> "${temp / 10} کیلومتر" //km
        else -> "${temp / 10}.${temp % 10} کیلومتر" //km
    }.let {
        if (it.contains("محدوده"))
            it
        else
            "$it $suffix"
    }
}

data class LoadInfo(
    @SerializedName("planningServiceId")
    val serviceId: Int, //id service ke dare bar ro mibare
    @SerializedName("carat")
    val density: String,
    @SerializedName("conditionType")
    private val _state: Int,
    @SerializedName("islamp")
    val slump: String,
    @SerializedName("resistanceCategory")
    val resistance: String,
    @SerializedName("value")
    val _amount: Float,
    @SerializedName("rowNumber")
    val rowNumber: Int,


    @SerializedName("productTypeName")
    private val productTypeName: String?, //density,slump
) {
    val serviceState get() = getEnumById(ServiceState::id, _state)
    val amount get() = "$_amount متر مکعب"
}