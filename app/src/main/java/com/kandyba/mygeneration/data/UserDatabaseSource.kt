package com.kandyba.mygeneration.data

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.kandyba.mygeneration.models.data.UserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.FileInputStream

class UserDatabaseSource {

    private val databaseReference: DatabaseReference?
        get() = try {
            FirebaseDatabase.getInstance().reference
        } catch (e: Throwable) {
            null
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserInfo(uid: String, userDatabaseEndpoint: String): Flow<UserModel?> = callbackFlow {
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
                var user = snapshot.getValue(UserModel::class.java)
                try {
                    offer(user)
                } catch (e: Throwable) {
                    Log.e(OFFER_ERROR, e.message.toString())
                }
            }
        })

        awaitClose { listener?.let { ref?.removeEventListener(it) } }
    }

    fun changeUserInfo(
        valueEndpoint: String,
        value: String,
        uid: String,
        userDatabaseEndpoint: String
    ) {
        var ref: DatabaseReference? = null
        try {
            ref = databaseReference
                ?.child(userDatabaseEndpoint)
                ?.child(uid)
                ?.child(valueEndpoint)
        } catch (e: Throwable) {
            Log.e(e.cause.toString(), e.message.toString())
        }
        ref?.setValue(value)
    }

    fun createUser(authUser: FirebaseUser, providerType: String?, userDatabaseEndpoint: String) {
        val user = createUserModelInternal(authUser, providerType)
        var ref: DatabaseReference? = null
        try {
            ref = databaseReference
                ?.child(userDatabaseEndpoint)
                ?.child(authUser.uid)
        } catch (e: Throwable) {
            Log.e(e.cause.toString(), e.message.toString())
        }
        ref?.setValue(user)
    }

    fun uploadUserAvatar(file: File?, id: String?): UploadTask? {
        Log.i("File path", file?.absolutePath.toString())
        Log.i("File path can", file?.canonicalPath.toString())
        var stream: FileInputStream? = null
        var ref: StorageReference? = null
        try {
            stream = FileInputStream(file?.absoluteFile)
            ref = Firebase.storage.reference.child("avatars/${id}.jpg")
        } catch (e: Throwable) {

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
    }
}