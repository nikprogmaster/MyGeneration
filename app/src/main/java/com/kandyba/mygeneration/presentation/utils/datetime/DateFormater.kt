package com.kandyba.mygeneration.presentation.utils.datetime

import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.presentation.calendar.Month
import com.kizitonwose.calendarview.model.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Получить из числа название месяца
 *
 * @param month номер месяца
 * @return название месяца
 */
fun getMonthByNumber(month: Int): String =
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

/**
 * Отформатировать переходную дату
 *
 * @param firstMonth первый месяц
 * @param secondMonth второй месяц
 * @return "месяц1" или "месяц1 - месяц2"
 */
fun formatTransitionDate(firstMonth: Int, secondMonth: Int): String {
    return if (firstMonth == secondMonth) {
        getMonthByNumber(firstMonth)
    } else {
        "${getMonthByNumber(firstMonth)} - ${getMonthByNumber(secondMonth)}"
    }
}

/**
 * Преобразовать строку в формате "dd.mm.yyyy" в [Calendar]
 *
 * @param date строка с датой
 * @return [Calendar]
 */
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

/**
 * Прпеобразовать из [CalendarDay] в дату в мс
 */
fun CalendarDay.parseDateFromCalendarDay(): Long {
    return Calendar.getInstance().also { calendar ->
        calendar.set(this.date.year, this.date.monthValue - 1, this.day)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.ZONE_OFFSET, 0)
    }.timeInMillis
}

/**
 * Форматировать дату к виду "dd month"
 */
fun Calendar.formatDateWithWords() =
    "${this[Calendar.DAY_OF_MONTH]} ${
        this.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    }"

/**
 * Форматровать дату к виду "dd.mm.yyyy"
 */
fun Calendar.formatDateWithDigits() =
    SimpleDateFormat(FIREBASE_FORMAT, Locale.getDefault()).format(Date(this.timeInMillis)).orEmpty()

const val FIREBASE_FORMAT = "dd.MM.yyyy"
