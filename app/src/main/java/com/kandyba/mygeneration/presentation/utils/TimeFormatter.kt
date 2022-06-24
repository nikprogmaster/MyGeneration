package com.kandyba.mygeneration.presentation.utils


fun formatTime(hour: Int, minute: Int): String {
    val h = if (hour < 10) "0$hour" else hour.toString()
    val m = if (minute < 10) "0$minute" else minute.toString()
    return "$h:$m"
}
