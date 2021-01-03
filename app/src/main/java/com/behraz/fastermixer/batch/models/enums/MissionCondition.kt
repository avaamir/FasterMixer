package com.behraz.fastermixer.batch.models.enums

enum class MissionCondition(val id: Int, val title: String) {
    Created(0, "ساخته شده"),
    ToBatch(1, "حرکت به سمت بچینگ"),
    Loading(2, "بارگیری"),
    ToDest(5, "به سمت مقصد"),
    UnLoading(6, "تخلیه"),
    ServiceFinished(11, "پایان سرویس")
}