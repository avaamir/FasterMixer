package com.behraz.fastermixer.batch.models.requests.behraz

import android.os.Parcelable
import com.behraz.fastermixer.batch.models.enums.ReportType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetReportRequest(
    @Transient var startYear: String? = "1399",
    @Transient var startMonth: String? = "10",
    @Transient var startDate: String? = "20",
    @Transient var startHour: String? = "07",
    @Transient var startMinute: String? = "30",
    @Transient var startSecond: String? = "00",

    @Transient var endYear: String? = "1399",
    @Transient var endMonth: String? = "10",
    @Transient var endDate: String? = "20",
    @Transient var endHour: String? = "16",
    @Transient var endMinute: String? = "30",
    @Transient var endSecond: String? = "00",

    @Transient var reportType: ReportType? = null,
    @Transient var vehicleId: Int = 0
) : Parcelable {

    val hasEmptyDateField
        get() = startYear.isNullOrBlank()
                || startMonth.isNullOrBlank()
                || startDate.isNullOrBlank()
                || startHour.isNullOrBlank()
                || startMinute.isNullOrBlank()
                || startSecond.isNullOrBlank()

                || endYear.isNullOrBlank()
                || endMonth.isNullOrBlank()
                || endDate.isNullOrBlank()
                || endHour.isNullOrBlank()
                || endMinute.isNullOrBlank()
                || endSecond.isNullOrBlank()

    val drawRoadRequest
        get() = DrawRoadRequest(
            startDate = "$startYear/$startMonth/$startDate",
            startTime = "$startHour:$startMinute:$startSecond",
            endDate = "$endYear/$endMonth/$endDate",
            endTime = "$endHour:$endMinute:$endSecond",
            vehicleId = vehicleId
        )

    val summeryReportRequest get() = SummeryReportRequest(
        startDate = "$startYear/$startMonth/$startDate",
        endDate = "$endYear/$endMonth/$endDate",
        vehicleId = vehicleId
    )

    fun fullReportRequest(page: Int) = FullReportRequest(
        startDate = "$startYear/$startMonth/$startDate",
        endDate = "$endYear/$endMonth/$endDate",
        vehicleId = vehicleId,
        page = page
    )


    open inner class SummeryReportRequest internal constructor(
        @SerializedName("firstDate") val startDate: String,
        @SerializedName("lastDate") val endDate: String,
        @SerializedName("vehicleId") val vehicleId: Int
    )

    inner class FullReportRequest internal constructor(
        startDate: String,
        endDate: String,
        vehicleId: Int,
        @SerializedName("index")
        var page: Int,
    ) : SummeryReportRequest(startDate, endDate, vehicleId)/* {
        @SerializedName("count")
        val count = 20 //Need by server , chunk size
    }*/


    inner class DrawRoadRequest internal constructor(
        startDate: String,
        @SerializedName("firstTime")
        val startTime: String,
        endDate: String,
        @SerializedName("lastTime")
        val endTime: String,
        vehicleId: Int,
    ) : SummeryReportRequest(startDate, endDate, vehicleId)

}