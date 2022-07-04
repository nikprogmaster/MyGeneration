package com.kandyba.mygeneration.models.presentation.calendar

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.kandyba.mygeneration.presentation.utils.datetime.formatDate
import com.kandyba.mygeneration.presentation.utils.datetime.formatTransitionDate
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.MonthScrollListener
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarManager(
    private val titleLayout: LinearLayout,
    private val calendarView: CalendarView,
    private val arrow: ImageView,
    private val monthTitle: TextView,
    private val arrowUp: Drawable?,
    private val arrowDown: Drawable?
) {

    private val today = Calendar.getInstance()
    private val currentMonth = YearMonth.now()
    private val firstMonth = defineFirstMonth()
    private val lastMonth = firstMonth.plusMonths(11)

    fun setupCalendar() {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        editMonthConfig(InDateStyle.FIRST_MONTH, 1, false)
        calendarView.scrollToDate(currentMonth.atDay(today.get(Calendar.DAY_OF_MONTH)))

        setScrollListener()
        setClickListener()
    }

    private fun setClickListener() {
        titleLayout.setOnClickListener {
            val monthToWeek = calendarView.maxRowCount == 6
            val firstDate = calendarView.findFirstVisibleDay()?.date ?: return@setOnClickListener
            val lastDate = calendarView.findLastVisibleDay()?.date ?: return@setOnClickListener

            val oneWeekHeight = calendarView.daySize.height
            val oneMonthHeight = oneWeekHeight * 6

            val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
            val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { anim ->
                calendarView.layoutParams.height = anim.animatedValue as Int
            }

            animator.doOnStart {
                if (!monthToWeek) {
                    editMonthConfig(InDateStyle.ALL_MONTHS, 6, true)
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    editMonthConfig(InDateStyle.FIRST_MONTH, 1, false)
                }

                if (monthToWeek) {
                    val openedMonth = firstDate.plusDays(7)
                    if (openedMonth.month.value == currentMonth.month.value) {
                        calendarView.scrollToDate(
                            currentMonth.atDay(today.get(Calendar.DAY_OF_MONTH))
                        )
                    } else {
                        calendarView.scrollToDate(firstDate)
                    }
                    arrow.setImageDrawable(arrowDown)
                } else {
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        calendarView.scrollToMonth(firstDate.yearMonth)
                    } else {
                        calendarView.scrollToMonth(minOf(firstDate.yearMonth.next, lastMonth))
                    }
                    arrow.setImageDrawable(arrowUp)
                }
            }
            animator.duration = 250
            animator.start()
        }
    }

    private fun editMonthConfig(
        inDateStyle: InDateStyle,
        maxRowCount: Int,
        hasBoundaries: Boolean
    ) {
        calendarView.updateMonthConfiguration(
            inDateStyle = inDateStyle,
            maxRowCount = maxRowCount,
            hasBoundaries = hasBoundaries
        )
    }

    private fun setScrollListener() {
        calendarView.monthScrollListener = object : MonthScrollListener {
            override fun invoke(p1: CalendarMonth) {
                if (calendarView.maxRowCount == 6) {
                    monthTitle.text = formatDate(p1.yearMonth.month.value)
                } else {
                    val firstDate = p1.weekDays.first().first().date.monthValue
                    val lastDate = p1.weekDays.last().last().date.monthValue
                    monthTitle.text = formatTransitionDate(firstDate, lastDate)
                }
            }
        }
    }

    private fun defineFirstMonth(): YearMonth =
        if (YearMonth.now().monthValue > 7) {
            YearMonth.of(YearMonth.now().year, 8)
        } else {
            YearMonth.of(YearMonth.now().year - 1, 8)
        }
}