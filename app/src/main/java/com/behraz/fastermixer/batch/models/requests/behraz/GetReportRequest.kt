package com.behraz.fastermixer.batch.models.requests.behraz

import android.os.Parcelable
import com.behraz.fastermixer.batch.models.enums.ReportType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetReportRequest(
    @Transient var startYear: String? = "1399",
    @Transient var startMonth: String? = "10",
    @Transient var startDate: String? = "15",
    @Transient var startHour: String? = "12",
    @Transient var startMinute: String? = "00",
    @Transient var startSecond: String? = "00",

    @Transient var endYear: String? = "1399",
    @Transient var endMonth: String? = "10",
    @Transient var endDate: String? = "15",
    @Transient var endHour: String? = "12",
    @Transient var endMinute: String? = "10",
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

    val request
        get() = Request(
            startDate = "$startYear/$startMonth/$startDate",
            startTime = "$startHour:$startMinute:$startSecond",
            endDate = "$endYear/$endMonth/$endDate",
            endTime = "$endHour:$endMinute:$endSecond",
            vehicleId = vehicleId
        )


    inner class Request internal constructor(
        @SerializedName("firstDate") val startDate: String,
        @SerializedName("firstTime") val startTime: String,
        @SerializedName("lastDate") val endDate: String,
        @SerializedName("lastTime") val endTime: String,
        @SerializedName("vehicleId") val vehicleId: Int
    )

}