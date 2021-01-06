package com.behraz.fastermixer.batch.models.enums

enum class RequestState(val id: Int, val title: String) {
    Reserved(1, "رزرو شده"),
    Planned(2, "برنامه ریزی شده"),
    Started(3, "در حال اجرا"),
    Finished(4, "اتمام یافته"),
    Canceled(5, "لغو شده")
}
