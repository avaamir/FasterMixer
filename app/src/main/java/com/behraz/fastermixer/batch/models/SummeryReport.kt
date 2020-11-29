package com.behraz.fastermixer.batch.models

data class SummeryReport(
    val id: String,
    val carName: String,
    val onMove: String,
    val onStop: String,
    val onOff: String,
    val distance: String,
    val maxSpeed: String
)