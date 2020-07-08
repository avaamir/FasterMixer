package com.behraz.fastermixer.batch.models

data class Plan(
    val id: String,
    val ownerName: String,
    val address: String,
    val plannedAmount: Int,
    val sentAmount: Int,
    val waitingAmount: Int
) {
    val progress: Int get() = if (plannedAmount == 0) 100 else sentAmount * 100 / plannedAmount
}