package com.behraz.fastermixer.batch.models

data class FullReport(
    val id: Int,
    val carName: String,
    val state: String,
    val duration: String,
    val maxSpeed: String,
    val aveSpeed: String,
    val distance: String,
    val time: String,
    val date: String
)