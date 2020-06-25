package com.behraz.fastermixer.batch.utils.general

import android.content.Intent
import android.os.Parcelable

fun Intent.putParcelableExtra(key: String, value: Parcelable) { //this function helps for put enums without type casting ( intent.putExtra(SampleEnumClass.SampleType as Parcelable) ) in an intent
    putExtra(key, value)
}
