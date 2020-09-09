package com.behraz.fastermixer.batch.utils.general

import java.util.*
import java.util.concurrent.TimeUnit

fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit = TimeUnit.MINUTES): Long {
    val diffInMillies: Long = date2.time - date1.time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}

operator fun Date.minus(date: Date): Long { //Return Duration In minutes
    return getDateDiff(this, date, TimeUnit.MINUTES)
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

    return "${if (days < 10) "0$days" else days} : ${if (hours < 10) "0$hours" else hours} : ${if (minutes < 10) "0$minutes" else minutes} : ${if (seconds < 10) "0$seconds" else seconds}"
}
