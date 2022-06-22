package com.kandyba.mygeneration.data

import android.util.Log
import com.google.firebase.database.*
import com.kandyba.mygeneration.models.presentation.calendar.Event
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EventsDatadaseSource() {
    fun getUserEvents(calendarEndpoint: String): Flow<List<Event>> = callbackFlow {

        var eventsCollection: DatabaseReference? = null
        try {
            eventsCollection = FirebaseDatabase.getInstance()
                .reference
                .child(calendarEndpoint)
        } catch (e: Throwable) {
            // If Firebase cannot be initialized, close the stream of data
            // flow consumers will stop collecting and the coroutine will resume
            close(e)
        }

        val listener = eventsCollection?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(error.message, error.details)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()
                for (i in snapshot.children) {
                    val event = i.getValue(Event::class.java)
                    if (event != null)
                        eventList.add(event)
                }
                try {
                    offer(eventList)
                } catch (e: Throwable) {
                    Log.e(OFFER_ERROR, e.message.toString())
                }
            }
        })
        awaitClose { listener?.let { eventsCollection?.removeEventListener(it) } }
    }

    companion object {
        private const val OFFER_ERROR = "Offer Error"
    }
}

