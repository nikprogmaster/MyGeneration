package com.kandyba.mygeneration.data.repository

import android.util.Log
import androidx.collection.LruCache
import com.kandyba.mygeneration.data.EventsFirestoreSource
import com.kandyba.mygeneration.models.presentation.calendar.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventsRepositoryImpl(
    private val eventsFirestoreSource: EventsFirestoreSource,
    private val eventsCache: LruCache<String, List<Event>>
) : EventsRepository {

    override suspend fun getEvents(regionCode: String, afterDate: Long): List<Event> =
        withContext(Dispatchers.IO) {
            eventsCache.get(EVENTS_KEY) ?: eventsFirestoreSource.getEvents(
                CALENDAR_DATABASE_ENDPOINT,
                regionCode,
                afterDate
            ).apply { eventsCache.put(EVENTS_KEY, this) }
        }

    override suspend fun updateEvents(regionCode: String, afterDate: Long) {
        withContext(Dispatchers.IO) {
            val e = eventsFirestoreSource.getEvents(
                CALENDAR_DATABASE_ENDPOINT,
                regionCode,
                afterDate
            ).apply { eventsCache.put(EVENTS_KEY, this) }
            Log.i("EventsRepositoryImpl", e.toString())
        }
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