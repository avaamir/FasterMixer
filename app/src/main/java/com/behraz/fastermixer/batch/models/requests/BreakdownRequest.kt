package com.behraz.fastermixer.batch.models.requests

import com.google.gson.annotations.SerializedName

class BreakdownRequest private constructor(
    @SerializedName("title")
    val title: String,
) {
    @SerializedName("description")
    val description: String = title

    @SerializedName("id")
    private val id = 0 //needed by server

    companion object {
        val FIXED = BreakdownRequest("تجهیز درست شد")
        val LAB = BreakdownRequest("حضور آزمایشگاه")
        val SOS = BreakdownRequest("نیاز به کمک فوری")
        val BREAKDOWN = BreakdownRequest("اعلام خرابی دستگاه")
        val STOP = BreakdownRequest("اعلام توقف")
    }
}