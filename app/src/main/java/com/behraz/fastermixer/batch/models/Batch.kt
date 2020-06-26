package com.behraz.fastermixer.batch.models

data class Batch(
    val id: Int,
    val name: String, //ddd
    val isAvailable: Boolean
) {
    val state get() = if (isAvailable) "در دسترس" else "در حال استفاده"
}