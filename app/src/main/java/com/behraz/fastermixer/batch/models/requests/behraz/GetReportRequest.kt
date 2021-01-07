package com.behraz.fastermixer.batch.models.requests.behraz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetReportRequest(
    var startYear: String? = null,
    var startMonth: String? = null,
    var startDate: String? = null,
    var startHour: String? = null,
    var startMinute: String? = null,
    var startSecond: String? = null,


    var endYear: String? = null,
    var endMonth: String? = null,
    var endDate: String? = null,
    var endHour: String? = null,
    var endMinute: String? = null,
    var endSecond: String? = null,

    var vehicleId: Int = 0
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

}