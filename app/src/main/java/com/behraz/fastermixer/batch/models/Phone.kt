package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Phone(
    val title: String?,
    val phoneNumber: String?
) : Parcelable