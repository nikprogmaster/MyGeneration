package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.data.EventsDatadaseSource
import com.kandyba.mygeneration.models.presentation.calendar.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class EventsRepositoryImpl(private val eventsDatadaseSource: EventsDatadaseSource) :
    EventsRepository {

    override fun getEvents(calendarEndpoint: String): Flow<List<Event>> =
        eventsDatadaseSource.getUserEvents(calendarEndpoint)
            .flowOn(Dispatchers.IO)
}