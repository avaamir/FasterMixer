package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
import java.util.*

data class Mixer(
    @SerializedName("vehicleID")
    val id: String,
    @SerializedName("planningServiceItemID")
    val serviceId: String, //id service ke dare bar ro mibare
    @SerializedName("vehicleName")
    val carName: String,
    @SerializedName("driverName")
    private var _driverName: String?,
    @SerializedName("plaque")
    val pelak: String,
    @SerializedName("conditionName")
    var state: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("latPoint")
    var lat: String,
    @SerializedName("lngPoint")
    var lng: String,

    val totalAmount: Float?, //TODO not implemented server side
    val phone: String?, //TODO it is telephone list, not yet implemented server side

    @SerializedName("lastDataTime")
    val lastDataTime: Date?,
    @SerializedName("durationTime")
    val lastDataTimeDiff: Long?,
    @SerializedName("speed") val speed: Float?,


    @SerializedName("ended")
    private val ended: Boolean?,
    @SerializedName("productTypeName")
    private val productTypeName: String?, //density,slump
    @SerializedName("volume")
    private val amount: Float,  //hajme ke bayad por she
    @SerializedName("capacity")
    val capacity: Float            // zarifyat machine
) {
    val driverName get() = _driverName ?: ""
    val pelakForMapLayer get() = pelak.split(",")
        .run { "${get(3)} ${get(2)} ${get(1)} ${get(0)}" }
    val latLng: GeoPoint get() = GeoPoint(lat.toDouble(), lng.toDouble())
    val loadInfo: LoadInfo
        get() =
            LoadInfo(
                _amount = amount,
                _totalAmount = totalAmount ?: 0f,
                isDelivered = ended ?: false,  //TODO not implemented server side
                productTypeName = productTypeName
            )

    fun normalizeStateByDistance(destArea: CircleFence) {
        val meterDistance = destArea.center.distanceToAsDouble(latLng)
        val temp = meterDistance.toInt() / 100
        return when {
            meterDistance < destArea.radius -> "وارد محدوده شد"
            temp == 0 || temp / 10 == 0 -> "${meterDistance.toInt()} متر" //meter
            temp % 10 == 0 -> "${temp / 10} کیلومتر" //km
            else -> "${temp / 10}.${temp % 10} کیلومتر" //km
        }.let {
            state = if (it.contains("رسید") || it.contains("محدوده"))
                it
            else
                "$it مانده"
        }
    }

    fun normalizeStateByDistance(meterDistance: Double, radius: Double) {
        val temp = meterDistance.toInt() / 100
        return when {
            meterDistance < radius -> "وارد محدوده شد"
            temp == 0 || temp / 10 == 0 -> "${meterDistance.toInt()} متر" //meter
            temp % 10 == 0 -> "${temp / 10} کیلومتر" //km
            else -> "${temp / 10}.${temp % 10} کیلومتر" //km
        }.let {
            state = if (it.contains("محدوده"))
                it
            else
                "$it مانده"
        }
    }
}

data class LoadInfo(
    private val _amount: Float,
    private val _totalAmount: Float,
    private val isDelivered: Boolean,
    private val productTypeName: String? //density,slump
) {
    val amount get() = "$_amount متر مکعب"
    val slump get() = if (productTypeName.isNullOrBlank()) "نامشخص" else productTypeName.split(",")[1]
    val density get() = if (productTypeName.isNullOrBlank()) "نامشخص" else productTypeName.split(",")[0]
    val amountFromTotal get() = "$_totalAmount/$_amount"

}