package com.kandyba.mygeneration.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.kandyba.mygeneration.models.data.UserModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.FileInputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDatabaseSource : UsersSource {

    private val databaseReference: DatabaseReference?
        get() = try {
            FirebaseDatabase.getInstance().reference
        } catch (e: Throwable) {
            null
        }

    override fun getUserInfo(uid: String, userDatabaseEndpoint: String): Flow<UserModel?> =
        callbackFlow {
            var ref: DatabaseReference? = null
            try {
                ref = databaseReference
                    ?.child(userDatabaseEndpoint)
                    ?.child(uid)
            } catch (e: Throwable) {
                close(e)
            }
            val listener = ref?.addValueEventListener(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    Log.e(error.message, error.details)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    try {
                        trySend(user)
                    } catch (e: Throwable) {
                        Log.e(OFFER_ERROR, e.message.toString())
                    }
                }
            })

            awaitClose { listener?.let { ref?.removeEventListener(it) } }
        }

    override suspend fun changeUserInfo(
        valueEndpoint: String,
        value: String,
        uid: String,
        userDatabaseEndpoint: String
    ): Boolean =
        suspendCoroutine { continuation ->
            var ref: DatabaseReference? = null
            try {
                ref = databaseReference
                    ?.child(userDatabaseEndpoint)
                    ?.child(uid)
                    ?.child(valueEndpoint)
            } catch (e: Throwable) {
                Log.e(e.cause.toString(), e.message.toString())
            }
            val task = ref?.setValue(value)
            task?.addOnSuccessListener { continuation.resume(true) }
                ?.addOnFailureListener { continuation.resume(false) }
        }

    override suspend fun createUser(
        authUser: FirebaseUser,
        providerType: String?,
        userDatabaseEndpoint: String
    ): Boolean =
        suspendCoroutine { continuation ->
            val user = createUserModelInternal(authUser, providerType)
            var ref: DatabaseReference? = null
            try {
                ref = databaseReference
                    ?.child(userDatabaseEndpoint)
                    ?.child(authUser.uid)
            } catch (e: Throwable) {
                Log.e(e.cause.toString(), e.message.toString())
            }
            val task = ref?.setValue(user)
            task?.addOnSuccessListener { continuation.resume(true) }
                ?.addOnFailureListener { continuation.resume(false) }
        }

    // TODO: Need refactoring
    override fun uploadUserAvatar(file: File?, id: String?): UploadTask? {
        var stream: FileInputStream? = null
        var ref: StorageReference? = null
        try {
            stream = FileInputStream(file?.absoluteFile)
            ref = Firebase.storage.reference.child(IMAGE_PATH.format(id))
        } catch (e: Throwable) {
            Log.e(e.cause.toString(), e.message.toString())
        }
        return if (stream != null && ref != null) {
            ref.putStream(stream)
        } else null
    }

    private fun createUserModelInternal(firebaseUser: FirebaseUser, providerType: String?) =
        UserModel(
            firebaseUser.uid,
            firebaseUser.displayName.orEmpty(),
            providerType,
            firebaseUser.email.orEmpty(),
            firebaseUser.phoneNumber.orEmpty(),
            null,
            providerType
        )

    companion object {
        private const val OFFER_ERROR = "Offer Error"
        private const val IMAGE_PATH = "avatars/%s.jpg"
    }
}