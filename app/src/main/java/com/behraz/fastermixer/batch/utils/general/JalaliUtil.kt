package com.behraz.fastermixer.batch.utils.general

import java.util.*


fun numberToSolarMonthName(number: Int) = when (number) {
    1 -> "فروردین"
    2 -> "اردیبهشت"
    3 -> "خرداد"
    4 -> "تیر"
    5 -> "مرداد"
    6 -> "شهریور"
    7 -> "مهر"
    8 -> "آبان"
    9 -> "آذر"
    10 -> "دی"
    11 -> "بهمن"
    12 -> "اسفند"
    else -> throw IllegalStateException("$number: does not exist in months")
}
fun solarMonthNameToNumber(name: String) = when (name) {
    "فروردین" -> "01"
    "اردیبهشت" -> "02"
    "خرداد" -> "03"
    "تیر" -> "04"
    "مرداد" -> "05"
    "شهریور" -> "06"
    "مهر" -> "07"
    "آبان" -> "08"
    "آذر" -> "09"
    "دی" -> "10"
    "بهمن" -> "11"
    "اسفند" -> "12"
    else -> throw IllegalStateException("$name: does not exist in months")
}


fun Date.toJalali(): SolarDate {
    var date: Int
    val month: Int
    val year: Int


    val ld: Int
    val miladiYear = this.year + 1900
    val miladiMonth = this.month + 1
    val miladiDate = this.date
    val miladiDay = this.day

    val buf1 = IntArray(12)
    val buf2 = IntArray(12)
    buf1[0] = 0
    buf1[1] = 31
    buf1[2] = 59
    buf1[3] = 90
    buf1[4] = 120
    buf1[5] = 151
    buf1[6] = 181
    buf1[7] = 212
    buf1[8] = 243
    buf1[9] = 273
    buf1[10] = 304
    buf1[11] = 334
    buf2[0] = 0
    buf2[1] = 31
    buf2[2] = 60
    buf2[3] = 91
    buf2[4] = 121
    buf2[5] = 152
    buf2[6] = 182
    buf2[7] = 213
    buf2[8] = 244
    buf2[9] = 274
    buf2[10] = 305
    buf2[11] = 335
    if (miladiYear % 4 != 0) {
        date = buf1[miladiMonth - 1] + miladiDate
        if (date > 79) {
            date -= 79
            if (date <= 186) {
                when (date % 31) {
                    0 -> {
                        month = date / 31
                        date = 31
                    }
                    else -> {
                        month = date / 31 + 1
                        date %= 31
                    }
                }
                year = miladiYear - 621
            } else {
                date -= 186
                when (date % 30) {
                    0 -> {
                        month = date / 30 + 6
                        date = 30
                    }
                    else -> {
                        month = date / 30 + 7
                        date %= 30
                    }
                }
                year = miladiYear - 621
            }
        } else {
            ld = if (miladiYear > 1996 && miladiYear % 4 == 1) {
                11
            } else {
                10
            }
            date += ld
            when (date % 30) {
                0 -> {
                    month = date / 30 + 9
                    date = 30
                }
                else -> {
                    month = date / 30 + 10
                    date %= 30
                }
            }
            year = miladiYear - 622
        }
    } else {
        date = buf2[miladiMonth - 1] + miladiDate
        ld = if (miladiYear >= 1996) {
            79
        } else {
            80
        }
        if (date > ld) {
            date -= ld
            if (date <= 186) {
                when (date % 31) {
                    0 -> {
                        month = date / 31
                        date = 31
                    }
                    else -> {
                        month = date / 31 + 1
                        date %= 31
                    }
                }
                year = miladiYear - 621
            } else {
                date -= 186
                when (date % 30) {
                    0 -> {
                        month = date / 30 + 6
                        date = 30
                    }
                    else -> {
                        month = date / 30 + 7
                        date %= 30
                    }
                }
                year = miladiYear - 621
            }
        } else {
            date += 10
            when (date % 30) {
                0 -> {
                    month = date / 30 + 9
                    date = 30
                }
                else -> {
                    month = date / 30 + 10
                    date %= 30
                }
            }
            year = miladiYear - 622
        }
    }
    return SolarDate(year, month, date, miladiDay, this.hours, this.minutes, this.seconds)
}

data class SolarDate internal constructor(
    val year: Int,
    val month: Int,
    val date: Int,
    val day: Int, //day of week in miladi
    private var hour: Int? = null,
    private var minute: Int? = null,
    private var second: Int? = null
) {
    val dayName: String = when (day) {
        0 -> "يکشنبه"
        1 -> "دوشنبه"
        2 -> "سه شنبه"
        3 -> "چهارشنبه"
        4 -> "پنج شنبه"
        5 -> "جمعه"
        6 -> "شنبه"
        else -> throw IllegalStateException("$date:: does not exist in day of week")
    }

    val monthName: String = numberToSolarMonthName(month)

    fun setTime(hour: Int, minute: Int, second: Int? = null) {
        this.hour = hour
        this.minute = minute
        this.second = second
    }

    override fun toString() =
        "$year/$month/$date" +
                if (hour != null)
                    " $hour:$minute" + if (second != null) ":$second" else ""
                else
                    ""
}