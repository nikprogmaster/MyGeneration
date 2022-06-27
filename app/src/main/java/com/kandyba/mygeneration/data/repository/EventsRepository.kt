package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.presentation.calendar.Event

interface EventsRepository {

    suspend fun getEvents(regionCode: String, afterDate: Long): List<Event>

    suspend fun updateEvents(regionCode: String, afterDate: Long)

    suspend fun addEvent(event: Event): Boolean
}