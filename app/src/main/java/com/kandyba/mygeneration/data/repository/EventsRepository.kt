package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.presentation.calendar.Event

interface EventsRepository {

    suspend fun getEvents(regionCode: String): List<Event>

    suspend fun updateEvents(regionCode: String): List<Event>

    suspend fun addEvent(event: Event): Boolean
}