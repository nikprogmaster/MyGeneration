package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.presentation.calendar.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {

    fun getEvents(calendarEndpoint: String): Flow<List<Event>>
}