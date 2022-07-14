package com.kandyba.mygeneration.data.source

import com.kandyba.mygeneration.models.presentation.calendar.Event

/**
 * Источник для работы с собитиями календаря
 */
interface EventsSource {

    /**
     * Получить события
     *
     * @param eventsEndpoint эндпоинт для доступа к коллекции
     * @param regionCode код региона
     * @param dateInMs дата в мс, начиная с которой нужно получить события
     *
     * @return список событий
     */
    suspend fun getEvents(eventsEndpoint: String, regionCode: String, dateInMs: Long): List<Event>

    /**
     * Добавить новое событие
     *
     * @param eventsEndpoint эндпоинт для доступа к коллекции
     * @param event новое событие
     *
     * @return true если успешно добавлено, иначе false
     */
    suspend fun addNewEvent(eventsEndpoint: String, event: Event): Boolean
}