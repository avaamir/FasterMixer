package com.behraz.fastermixer.batch.models.requests

import com.google.gson.annotations.SerializedName

class BreakdownRequest private constructor(
    @SerializedName("Description")
    val description: String
) {
    companion object {
        val FIXED = BreakdownRequest("تجهیز درست شد")
        val LAB = BreakdownRequest("حضور آزمایشگاه")
        val SOS = BreakdownRequest("نیاز به کمک فوری")
        val BREAKDOWN = BreakdownRequest("اعلام خرابی دستگاه")
        val STOP = BreakdownRequest("اعلام توقف")
    }
}
