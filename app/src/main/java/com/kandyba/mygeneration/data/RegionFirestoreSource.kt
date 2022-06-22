package com.kandyba.mygeneration.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kandyba.mygeneration.models.data.RegionModel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RegionFirestoreSource {

    private val firestore: FirebaseFirestore?
        get() = try {
            Firebase.firestore
        } catch (e: Throwable) {
            null
        }

    suspend fun getRegions(regionEndpoint: String): List<RegionModel> =
        suspendCoroutine { continuation ->
            firestore?.let {
                it.collection(regionEndpoint)
                    .get()
                    .addOnSuccessListener { result ->
                        continuation.resume(result.toObjects(RegionModel::class.java))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

    companion object {
        private const val TAG = "RegionsError"

    }
}