package com.kandyba.mygeneration.presentation.view

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.kandyba.mygeneration.R
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    private val dayView: TextView = view.findViewById(R.id.day)

    fun setText(text: String) {
        dayView.text = text
    }

    fun setTextColor(@ColorInt color: Int) {
        dayView.setTextColor(color)
    }

    fun setBackground(backGround: Drawable?) {
        dayView.background = backGround
    }
}