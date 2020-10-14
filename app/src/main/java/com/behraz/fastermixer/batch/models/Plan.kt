package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("planningID")
    val id: String,
    @SerializedName("projectName")
    val ownerName : String,
    @SerializedName("customerAddress")
    val address : String,
    @SerializedName("value")
    val plannedAmount : Int,
    @SerializedName("realValue")
    val sentAmount : Int,
    @SerializedName("started")
    val started : Boolean,
    @SerializedName("dateTime")
    val dateTime : String,
    @SerializedName("requestTitle")
    val requestTitle : String,
    @SerializedName("productTypeName")
    val productTypeName : String,
    @SerializedName("deliveryTime")
    val deliveryTime : String,
    @SerializedName("requestTypeName")
    val requestTypeName : String,
    @SerializedName("requestState")
    val requestState : Int,
    @SerializedName("endedDescription")
    val endedDescription : String,
    @SerializedName("requestLab")
    val requestLab : Boolean,
    @SerializedName("requestAddress")
    val requestAddress : String,
    @SerializedName("description")
    val description : String

) {

    val waitingAmount get() = plannedAmount - sentAmount
    val progress: Int get() = if (plannedAmount == 0) 100 else sentAmount * 100 / plannedAmount
}