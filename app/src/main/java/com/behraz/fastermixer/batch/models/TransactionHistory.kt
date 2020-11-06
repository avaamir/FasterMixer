package com.behraz.fastermixer.batch.models

data class TransactionHistory(
    val id: String,
    val packageName: String,
    val orderNumber: String,
    val paymentResult: String,
    val date: String
)