package com.behraz.fastermixer.batch.utils.fastermixer

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.behraz.GetAdminAccountPage
import com.behraz.fastermixer.batch.utils.general.now
import org.osmdroid.util.GeoPoint
import java.util.*

fun fakeDrawRoadReport() = listOf(
    GeoPoint(31.891413345001638, 54.35357135720551),
    GeoPoint(31.891413345001638, 54.36357135720551),
    GeoPoint(31.892413345001638, 54.37357135720551),
    GeoPoint(31.893413345001638, 54.38357135720551),
    GeoPoint(31.894413345001638, 54.39357135720551),
    GeoPoint(31.895413345001638, 54.40357135720551),
    GeoPoint(31.896413345001638, 54.45357135720551),
    GeoPoint(31.897413345001638, 54.46357135720551),
    GeoPoint(31.898413345001638, 54.47357135720551),
    GeoPoint(31.899413345001638, 54.48357135720551),
)


fun fakeSummeryReports() = listOf(
    SummeryReport("1", "آمیکو سفید", "100 ساعت", "61 ساعت", "50 ساعت", "100 کیلومتر", "100"),
    SummeryReport("2", "آمیکو سفید", "100 ساعت", "62 ساعت", "50 ساعت", "100 کیلومتر", "100"),
    SummeryReport("3", "آمیکو سفید", "100 ساعت", "63 ساعت", "50 ساعت", "100 کیلومتر", "100"),
    SummeryReport("4", "آمیکو سفید", "100 ساعت", "64 ساعت", "50 ساعت", "100 کیلومتر", "100"),
    SummeryReport("5", "آمیکو سفید", "100 ساعت", "65 ساعت", "50 ساعت", "100 کیلومتر", "100"),
)

fun fakeFullReports() = listOf(
    FullReport(1, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
    FullReport(2, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
    FullReport(3, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
    FullReport(4, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
    FullReport(5, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
    FullReport(6, "آمیکو سفید", "روشن", "2 ساعت", "120", "89", "100", "11:39 تا 12:49", "99/2/22"),
)


fun fakeAdminManageAccountPage() = GetAdminAccountPage(
    fakePackages(),
    fakeTransactions(),
    "امیرحسین",
    "09362163813",
    "بارز",
    false
)

fun fakeTransactions(): List<TransactionHistory> = listOf(
    TransactionHistory("1", "پکیج A", "213124124", "پرداخت موفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
    TransactionHistory("2", "پکیج A", "213124124", "پرداخت ناموفق", "99/2/3"),
)

fun fakePackages(): List<Package> = listOf(
    Package("1", "پکیج A", "desc", "24000", 10, false, true, "99/2/3", "99/2/23", 5),
    Package("2", "پکیج B", "desc1", "48000", 10, false, false, null, null, null),
    Package("3", "پکیج C", "desc2", "100000", 90, true, false, "99/2/5", "99/2/7", 30),
    Package("4", "پکیج D", "desc3", "1000000", 10, false, false, null, null, null),
    Package("5", "پکیج E", "desc4", "23000", 10, false, false, null, null, null),
)


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