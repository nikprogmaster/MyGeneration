package com.kandyba.mygeneration.data.repository

import android.util.Log
import androidx.collection.LruCache
import com.kandyba.mygeneration.data.EventsFirestoreSource
import com.kandyba.mygeneration.models.presentation.calendar.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class EventsRepositoryImpl(
    private val eventsFirestoreSource: EventsFirestoreSource,
    private val eventsCache: LruCache<String, List<Event>>
) : EventsRepository {

    private val startDate: Long

    init {
        val currentDate = Calendar.getInstance()
        val year: Int = if (currentDate.get(Calendar.MONTH) < 7) {
            currentDate.get(Calendar.YEAR) - 1
        } else {
            currentDate.get(Calendar.YEAR)
        }
        startDate = Calendar.getInstance().apply {
            this.set(year, 7, 0)
            this.set(Calendar.DAY_OF_MONTH, 1)
            this.set(Calendar.HOUR, 0)
            this.set(Calendar.HOUR_OF_DAY, 0)
            this.set(Calendar.ZONE_OFFSET, 0)
        }.timeInMillis
        Log.i("EventsRepositoryImpl", startDate.toString())
    }

    override suspend fun getEvents(regionCode: String): List<Event> =
        withContext(Dispatchers.IO) {
            eventsCache.get(EVENTS_KEY) ?: eventsFirestoreSource.getEvents(
                CALENDAR_DATABASE_ENDPOINT,
                regionCode,
                startDate
            ).apply { eventsCache.put(EVENTS_KEY, this) }
        }

    override suspend fun updateEvents(regionCode: String): List<Event> =
        withContext(Dispatchers.IO) {
            eventsFirestoreSource.getEvents(
                CALENDAR_DATABASE_ENDPOINT,
                regionCode,
                startDate
            ).apply { eventsCache.put(EVENTS_KEY, this) }
        }

    override suspend fun addEvent(event: Event): Boolean =
        withContext(Dispatchers.IO) {
            eventsFirestoreSource.addNewEvent(CALENDAR_DATABASE_ENDPOINT, event)
        }

    companion object {
        private const val EVENTS_KEY = "events_key"
        private const val CALENDAR_DATABASE_ENDPOINT = "calendar"
    }
}