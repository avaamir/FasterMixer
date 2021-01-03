package com.behraz.fastermixer.batch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "contacts")
data class Contact(
    val displayName: String,
    @PrimaryKey
    val mobileNumber: String,
    var company: String,
    val gpsId: String,
    val homeNumber: String? = null,
    val workNumber: String? = null,
    val emailID: String? = null,
    val jobTitle: String? = null
) {
    @Transient
    var isChecked: Boolean = false
}