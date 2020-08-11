package com.behraz.fastermixer.batch.models

data class Contact(
    val displayName: String,
    val mobileNumber: String,
    var company: String,
    val homeNumber: String? = null,
    val workNumber: String? = null,
    val emailID: String? = null,
    val jobTitle: String? = null
) {
    @Transient
    var isChecked = false
}