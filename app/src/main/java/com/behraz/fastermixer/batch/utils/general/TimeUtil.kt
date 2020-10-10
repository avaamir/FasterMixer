package com.behraz.fastermixer.batch.utils.general

import java.util.*
import java.util.concurrent.TimeUnit

fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit = TimeUnit.MINUTES): Long {
    val diffInMillies: Long = date2.time - date1.time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}

operator fun Date.minus(date: Date): Long { //Return Duration In minutes
    return getDateDiff(date, this, TimeUnit.SECONDS)
}

/*fun getDateFromTimestamp(timestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.UK)
    calendar.timeInMillis = timestamp * 1000
    return DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString()
}*/

fun getDateFromTimestamp(timestamp: Long): Date {
    val calendar = Calendar.getInstance()
    val tz = TimeZone.getDefault()
    calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
    return Date(timestamp * 1000)
}

fun now(): Date = Calendar.getInstance().time


fun millisToTimeString(millis: Long): String {
    var uptime = millis
    val days: Long = TimeUnit.MILLISECONDS.toDays(uptime)
    uptime -= TimeUnit.DAYS.toMillis(days)
    val hours: Long = TimeUnit.MILLISECONDS.toHours(uptime)
    uptime -= TimeUnit.HOURS.toMillis(hours)
    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(uptime)
    uptime -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(uptime)

    return "${if (days < 10) "0$days" else days.toString()} : ${if (hours < 10) "0$hours" else hours.toString()} : ${if (minutes < 10) "0$minutes" else minutes.toString()} : ${if (seconds < 10) "0$seconds" else seconds.toString()}"
}

fun estimateTime(duration: Long, sourceUnit: TimeUnit = TimeUnit.MINUTES): String {
    if (sourceUnit == TimeUnit.MILLISECONDS || sourceUnit == TimeUnit.NANOSECONDS || sourceUnit == TimeUnit.MICROSECONDS) {
        throw IllegalArgumentException("millis nanos and micros are not valid for TimeUnit")
    }
    TimeUnit.values().reversedArray().forEach { destTimeUnit ->
        val amount = destTimeUnit.convert(duration, sourceUnit)
        if (amount > 0) {
            return "$amount " + when (destTimeUnit) {
                TimeUnit.SECONDS -> "ثانیه"
                TimeUnit.MINUTES -> "دقیقه"
                TimeUnit.HOURS -> "ساعت"
                TimeUnit.DAYS -> "روز"
                else -> throw IllegalArgumentException("millis nanos and micros are not valid for TimeUnit")
            }.exhaustiveAsExpression() + " پیش"
        }
    }
    return "همین الان"
}








