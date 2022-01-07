package com.kandyba.mygeneration.models.presentation.calendar

import android.util.Log
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kizitonwose.calendarview.model.CalendarDay
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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

fun addEventsToMap(events: List<Event>): HashMap<Calendar, List<Event>> {
    val map = HashMap<Calendar, List<Event>>()
    for (event in events) {
        event.startDay?.let {
            val cal = parseDateFromString(event.startDay)
            if (map.containsKey(cal)) {
                map[cal]?.toMutableList()?.add(event)
            } else {
                map[cal] = mutableListOf(event)
            }
        }
    }
    return map.also { m -> m.map { it.value.toList() } }
}

fun parseDateFromCalendarDay(day: CalendarDay): Calendar {
    return Calendar.getInstance().apply {
        this.set(day.date.year, day.date.monthValue, day.day)
        this.set(Calendar.HOUR_OF_DAY, 0)
        this.set(Calendar.MINUTE, 0)
        this.set(Calendar.MILLISECOND, 0)
        this.set(Calendar.SECOND, 0)
    }
}