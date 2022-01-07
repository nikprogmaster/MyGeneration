package com.kandyba.mygeneration.models.presentation.calendar

import android.view.View
import android.widget.TextView
import com.kandyba.mygeneration.R
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View): ViewContainer(view) {
    val dayView: TextView = view.findViewById(R.id.day)
}