package com.kandyba.mygeneration.data.repository

import com.kandyba.mygeneration.models.presentation.calendar.Event

/**
 * Интерфейс репозитория для работы с событиями в календаре
 */
interface EventsRepository {

    /**
     * Получить события
     *
     * @param regionCode код региона
     * @return список событий
     */
    suspend fun getEvents(regionCode: String): List<Event>

    /**
     * Обновить события, в том числе и кэш
     *
     * @param regionCode код региона
     * @return новый список событий
     */
    suspend fun updateEvents(regionCode: String): List<Event>

    /**
     * Добавить событие
     *
     * @param event новое событие
     * @return true если успешно добавлено, иначе false
     */
    suspend fun addEvent(event: Event): Boolean
}