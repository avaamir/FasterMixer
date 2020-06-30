package com.behraz.fastermixer.batch.utils.fastermixer

import com.behraz.fastermixer.batch.models.*


fun fakeCustomers() = listOf(
    Customer(
        id = 1,
        name = "1امیرحسین مهدی پور",
        startTime = "14:32",
        address = "یزد - باغ ملی - کارخانه اقبال - بهراز",
        _slump = 8,
        _amount = 2,
        _density = 4,
        _mixerCount = 2,
        jobType = "سقف",
        area = listOf(LatLng(0.0, 0.0))
    ),
    Customer(
        id = 2,
        name = "2امیرحسین مهدی پور",
        startTime = "14:32",
        address = "یزد - باغ ملی - کارخانه اقبال - بهراز",
        _slump = 8,
        _amount = 2,
        _density = 4,
        _mixerCount = 2,
        jobType = "سقف",
        area = listOf(LatLng(0.0, 0.0))
    ),
    Customer(
        id = 3,
        name = "3امیرحسین مهدی پور",
        startTime = "14:32",
        address = "یزد - باغ ملی - کارخانه اقبال - بهراز",
        _slump = 8,
        _amount = 2,
        _density = 4,
        _mixerCount = 2,
        jobType = "سقف",
        area = listOf(LatLng(0.0, 0.0))
    )
)

fun fakeProgresses(isMixer: Boolean) = if (isMixer) {
    listOf(
        Progress(1, "شروع حرکت", 2),
        Progress(2, "رسیدن به محل", 2),
        Progress(3, "پایان ماموریت", 1),
        Progress(4, "ثبت گزارش", 0),
        Progress(5, "برگشت به پایگاه", 0)
    )
} else {
    listOf(
        Progress(1, "دریافت اطلاعات مشتری", 1),
        Progress(2, "رسیدن به محل", 0),
        Progress(3, "شروع بتن ریزی", 0),
        Progress(4, "اتمام کار مشتری", 0)
    )
}


fun fakeBatches() = listOf(
    Equipment("1", "بچ شماره یک", true),
    Equipment("2", "بچ شماره دو", false),
    Equipment("3", "بچ شماره سه", true),
    Equipment("4", "بچ شماره چهار", true),
    Equipment("5", "بچ شماره پنج", false)
)


fun fakeMessages() = listOf(
    Message(
        id = "1",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        isSendMessage = false
    ), Message(
        id = "2",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        isSendMessage = false
    ), Message(
        id = "3",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        isSendMessage = false
    ), Message(
        id = "4",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        isSendMessage = false
    )
)


fun fakeMixers(): List<Mixer> {
    val mixers = mutableListOf<Mixer>()
    for (i in 1..6) {
        mixers.add(
            Mixer(
                id = i,
                carName = "امیرحسین مهدی پور",
                phone = "0936216381$i",
                carId = "12,ب,234,63",
                state = "ده دقیقه تا پمپ",
                driverName = "حسن جعفری",
                owner = "بهراز$i",
                latLng = LatLng(0.0, 0.0),
                loadInfo =
                LoadInfo(
                    startTime = "14:32",
                    _slump = i,
                    _density = 7,
                    _amount = 6,
                    _totalAmount = 20,
                    isDelivered = false
                )
            )
        )
    }
    return mixers
}