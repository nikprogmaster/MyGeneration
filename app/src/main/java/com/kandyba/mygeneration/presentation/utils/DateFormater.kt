package com.kandyba.mygeneration.presentation.utils

import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.calendar.Month
import com.kizitonwose.calendarview.model.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(month: Int): String =
    when (month) {
        1 -> Month.JANUARY.monthName
        2 -> Month.FEBRUARY.monthName
        3 -> Month.MARCH.monthName
        4 -> Month.APRIL.monthName
        5 -> Month.MAY.monthName
        6 -> Month.JUNE.monthName
        7 -> Month.JULY.monthName
        8 -> Month.AUGUST.monthName
        9 -> Month.SEPTEMBER.monthName
        10 -> Month.OCTOBER.monthName
        11 -> Month.NOVEMBER.monthName
        12 -> Month.DECEMBER.monthName
        else -> EMPTY_STRING
    }

fun parseDateFromString(date: String): Calendar {
    val calendar = Calendar.getInstance()
    return calendar.also {
        val day = date.substring(0, 2).toInt()
        val month = date.substring(3, 5).toInt()
        val year = date.substring(6, 10).toInt()
        calendar.set(year, month, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
    }
}

fun addEventsToMap(events: List<Event>): HashMap<Long, MutableList<Event>> {
    val map = HashMap<Long, MutableList<Event>>()
    for (event in events) {
        val timeInMs = event.timestamp
        if (map.containsKey(timeInMs)) {
            map[timeInMs]?.add(event)
        } else {
            map[timeInMs] = mutableListOf(event)
        }
    }
    return map
}

fun parseDateFromCalendarDay(day: CalendarDay): Long {
    return Calendar.getInstance().apply {
        this.set(day.date.year, day.date.monthValue - 1, day.day)
        this.set(Calendar.HOUR, 0)
        this.set(Calendar.HOUR_OF_DAY, 0)
        this.set(Calendar.MINUTE, 0)
        this.set(Calendar.MILLISECOND, 0)
        this.set(Calendar.SECOND, 0)
        this.set(Calendar.ZONE_OFFSET, 0)
    }.timeInMillis
}

fun Calendar.formatDateWithWords() =
    "${this[Calendar.DAY_OF_MONTH]} ${
        this.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    }"

fun Calendar.formatDateWithDigits() =
    SimpleDateFormat(FIREBASE_FORMAT, Locale.getDefault()).format(Date(this.timeInMillis))

const val FIREBASE_FORMAT = "dd.MM.yyyy"
