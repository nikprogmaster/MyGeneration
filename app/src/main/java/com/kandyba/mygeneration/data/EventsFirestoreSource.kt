package com.kandyba.mygeneration.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.user.Region
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class EventsFirestoreSource {

    private val firestore: FirebaseFirestore?
        get() = try {
            Firebase.firestore
        } catch (e: Throwable) {
            null
        }

    suspend fun getEvents(eventsEndpoint: String, regionCode: String, dateInMs: Long): List<Event> =
        suspendCoroutine { continuation ->
            val regions = mutableListOf(regionCode)
            if (regionCode != Region.COMMON.regionCode) {
                regions.add(Region.COMMON.regionCode)
            }
            firestore?.let {
                it.collection(eventsEndpoint)
                    .whereGreaterThan(TIMESTAMP_FIELD, dateInMs)
                    .whereIn(REGION_FIELD, regions)
                    .get()
                    .addOnSuccessListener { snap ->
                        continuation.resume(snap.toObjects(Event::class.java))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

    suspend fun addNewEvent(eventsEndpoint: String, event: Event): Boolean =
        suspendCoroutine { continuation ->
            firestore?.let {
                it.collection(eventsEndpoint)
                    .add(event)
                    .addOnSuccessListener {
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                    }
            }
        }

    companion object {
        private const val TIMESTAMP_FIELD = "timestamp"
        private const val REGION_FIELD = "regionCode"
    }
}