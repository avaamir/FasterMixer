package com.behraz.fastermixer.batch.models.enums

/**state service darun paln ra moshakhas mikonad */
enum class ServiceState(val id: Int, val title: String) {
    Unknown(-1, "وضعیت سرویس نامشخص"),
    Created(0, "ساخته شده"),
    ToBatch(1, "حرکت به سمت بچینگ"),
    Loading(2, "بارگیری"),
    ToDest(5, "به سمت مقصد"),
    UnLoading(6, "تخلیه"),
    ServiceFinished(11, "پایان سرویس")
}