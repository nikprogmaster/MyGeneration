package com.kandyba.mygeneration.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.presentation.utils.datetime.formatDateWithDigits
import java.util.*

/**
 * Адаптер для списка событий
 *
 * @param events список событий
 */
class EventAdapter(private val events: ArrayList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = events[position]
        holder.bindViews(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    /**
     * ViewHolder для ячейки события
     *
     * @param itemView корневая вью элемента
     */
    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.event_title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val time: TextView = itemView.findViewById(R.id.event_time)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val eventNumber: TextView = itemView.findViewById(R.id.event_number)

        /**
         * Забиндить вью
         *
         * @param event событие
         */
        fun bindViews(event: Event) {
            name.text = event.name
            description.text = event.description
            date.text = Calendar.getInstance()
                .apply { timeInMillis = event.timestamp }
                .formatDateWithDigits()
            val timeDisplayed = "${event.startTime} - ${event.finishTime}"
            time.text = timeDisplayed
            if (events.size > 1) {
                eventNumber.visibility = View.VISIBLE
                val number = "${events.indexOf(event) + 1}."
                eventNumber.text = number
            }
        }
    }
}