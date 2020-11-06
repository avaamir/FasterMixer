package com.behraz.fastermixer.batch.models

data class Package(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val days: Int,
    val isReserved: Boolean,
    val isApplied: Boolean,
    val startDate: String?, //age apply ya reserved shode bashe date ham dare
    val endDate: String?,
    val remainingDays: Int?
)