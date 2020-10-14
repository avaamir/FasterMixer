package com.behraz.fastermixer.batch.utils.fastermixer

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.utils.general.now
import java.util.*

/*fun fakeAdminEquipments() = listOf(
    AdminEquipment("1", "میکسر زرد", "24 ب 623 53", 1),
    AdminEquipment("2", "لودر زرد", "14 ب 603 63", 2),
    AdminEquipment("3", "پمپ زرد", "24 ب 613 53", 3),
    AdminEquipment("4", "فرغون زرد", "22 ب 421 53", 4),
    AdminEquipment("5", "پمپ زرد", "22 ب 433 53", 1)
)*/
/*fun fakePlans() = listOf(
    Plan("1", "علی اکبری", "یزد - صفاییه", 50, 10, 4),
    Plan("2", "علی اکبری", "یزد - صفاییه", 30, 10, 5),
    Plan("3", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("4", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("5", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("6", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("7", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("8", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("9", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("10", "علی اکبری", "یزد - صفاییه", 10, 7, 6),
    Plan("11", "علی اکبری", "یزد - صفاییه", 20, 10, 7)
)*/
/*
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
    )
)
*/

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
        _isEvent = 1,
        userId = null,
        dateTime = now().toString()
    ), Message(
        id = "2",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        dateTime = now().toString(),
        _isEvent = 1,
        userId = null
    ), Message(
        id = "3",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        isDelivered = false,
        dateTime = now().toString(),
        _isEvent = 1,
        userId = null
    ), Message(
        id = "4",
        sender = "باسکول",
        content = "مغیارت بار برای میکسر یک",
        priority = 1,
        senderId = "1",
        senderImage = null,
        viewed = false,
        dateTime = now().toString(),
        isDelivered = false,
        _isEvent = 1,
        userId = null
    )
)


fun fakeMixers(): List<Mixer> {
    val mixers = mutableListOf<Mixer>()
    for (i in 1..6) {
        mixers.add(
            Mixer(
                id = "$i",
                serviceId = "$i",
                carName = "امیرحسین مهدی پور$i",
                phone = "0936216381$i",
                pelak = "12,ب,234,63",
                state = "ده دقیقه تا پمپ",
                _driverName = "حسن جعفری",
                owner = "بهراز$i",
                lat = "$i",
                lng = "42.2${7 - i}",
                amount = 4f,
                capacity = 6f,
                ended = false,
                productTypeName = "8,2",
                totalAmount = 300f,
                lastDataTime = Calendar.getInstance().time,
                speed = 0.0f,
                lastDataTimeDiff = 0.0
            )
        )
    }
    return mixers
}