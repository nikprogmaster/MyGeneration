package com.kandyba.mygeneration.presentation.utils.datetime

import com.kandyba.mygeneration.models.presentation.calendar.Event

/**
 * Создать из списка событий мапу с ключом timestamp (мс)
 *
 * @param events список событий
 * @return хэш-мап с ключом timestamp (мс) и значением - список событий
 */
fun addEventsToMap(events: List<Event>): Map<Long, List<Event>> {
    val map = HashMap<Long, MutableList<Event>>()
    for (event in events) {
        val timeInMs = event.timestamp
        if (map.containsKey(timeInMs)) {
            map[timeInMs]?.add(event)
        } else {
            map[timeInMs] = mutableListOf(event)
        }
    }
    return map.mapValues { it.value.toList() }
}