package com.kandyba.mygeneration.presentation.binder

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.utils.datetime.parseDateFromCalendarDay
import com.kandyba.mygeneration.presentation.view.DayViewContainer
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import java.util.*

class CalendarDayBinder(
    private val eventsMap: HashMap<Long, MutableList<Event>>,
    private var context: Context?,
    private val onEventDayClick: (List<Event>, Long) -> Unit,
    private val onEmptyDayClick: (Long) -> Unit
) : DayBinder<DayViewContainer> {

    private val today: Calendar
        get() = Calendar.getInstance()

    override fun create(view: View) = DayViewContainer(view)

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        val date = day.parseDateFromCalendarDay()
        container.setText(day.date.dayOfMonth.toString())
        if (day.owner == DayOwner.THIS_MONTH) {
            container.setTextColor(Color.BLACK)
        } else {
            container.setTextColor(Color.GRAY)
        }
        setDefaultDaySettings(container)
        setViewForEventDay(date, container, R.drawable.event_day_selector)
        val fMonth = today.get(Calendar.MONTH) + 1
        if (day.day == today.get(Calendar.DAY_OF_MONTH) && day.date.monthValue == fMonth) {
            setDayBackground(container, R.drawable.today_selector)
            setViewForEventDay(date, container, R.drawable.today_with_event_selector)
        }
        container.view.setOnClickListener {
            if (eventsMap.containsKey(date)) {
                onEventDayClick.invoke(eventsMap.getOrDefault(date, mutableListOf()), date)
            } else {
                onEmptyDayClick.invoke(date)
            }
        }
    }

    fun utilizeContext() {
        context = null
    }

    private fun setViewForEventDay(
        date: Long,
        container: DayViewContainer,
        @DrawableRes background: Int
    ) {
        if (eventsMap.containsKey(date)) {
            context?.let {
                container.setBackground(ContextCompat.getDrawable(it, background))
                container.setTextColor(it.getColor(R.color.colorPrimary))
            }
        }
    }

    private fun setDefaultDaySettings(container: DayViewContainer) {
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless,
            typedValue,
            true
        )
        setDayBackground(container, typedValue.resourceId)
    }

    private fun setDayBackground(container: DayViewContainer, @DrawableRes background: Int) {
        container.setBackground(context?.let { ContextCompat.getDrawable(it, background) })
    }
}