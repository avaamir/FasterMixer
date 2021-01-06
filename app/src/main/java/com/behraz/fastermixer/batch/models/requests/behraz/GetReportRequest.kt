package com.behraz.fastermixer.batch.models.requests.behraz

data class GetReportRequest(
    val startDate: String,
    val endDate: String,
    val vehicleId: Int
)