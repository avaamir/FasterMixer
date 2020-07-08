package com.behraz.fastermixer.batch.models

import android.location.Location
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

data class Mixer(
    @SerializedName("planningServiceItemID")
    val id: String,
    @SerializedName("vehicleName")
    val carName: String,
    @SerializedName("driverName")
    var driverName: String,
    @SerializedName("plaque")
    val carId: String,
    @SerializedName("conditionName")
    val state: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("latPoint")
      var lat: String,
    @SerializedName("lngPoint")
      var lng: String,

    val totalAmount: Float?, //TODO not implemented server side
    val phone: String?, //TODO it is telephone list, not yet implemented server side

    @SerializedName("ended")
    private val ended: Boolean?,
    @SerializedName("productTypeName")
    private val productTypeName: String?, //density,slump
    @SerializedName("volume")
    private val amount: Float,  //hajme ke bayad por she
    @SerializedName("capacity")
    val capacity: Float            // zarifyat machine
) {
    val latLng: GeoPoint get() = GeoPoint(lat.toDouble(), lng.toDouble())
    val loadInfo: LoadInfo
        get() =
            LoadInfo(
                _amount = amount,
                _totalAmount = totalAmount ?: 0f,
                isDelivered = ended ?: false,  //TODO not implemented server side
                productTypeName = productTypeName
            )
}

data class LoadInfo(
    private val _amount: Float,
    private val _totalAmount: Float,
    private val isDelivered: Boolean,
    private val productTypeName: String? //density,slump
) {
    val amount get() = "$_amount متر مکعب"
    val slump get() = productTypeName?.split(",")?.get(1)
    val density get() = productTypeName?.split(",")?.get(0)
    val amountFromTotal get() = "$_totalAmount/$_amount"

}