package com.kandyba.mygeneration.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.data.AccountType
import com.kandyba.mygeneration.models.data.User
import com.kandyba.mygeneration.models.presentation.user.UserConverter
import com.kandyba.mygeneration.models.presentation.user.UserField
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userConverter: UserConverter
) : ViewModel() {

    private val showLoggedUserLayout = MutableLiveData<Boolean>()
    private val userInfo = MutableLiveData<User>()
    private val signInUser = MutableLiveData<Unit>()
    private val sharedPreferencesUserInfo = MutableLiveData<Map<UserField, String?>>()
    private val showReservedUserInfo = MutableLiveData<Unit>()
    private val showProgressBar = MutableLiveData<Boolean>()

    private lateinit var auth: FirebaseAuth
    private lateinit var settings: SharedPreferences

    /** [LiveData] для отображения layout залогиненного пользователя */
    val showLoggedUserLayoutLiveData: LiveData<Boolean>
        get() = showLoggedUserLayout

    /** [LiveData] для получения информации о пользователе */
    val userInfoLiveData: LiveData<User>
        get() = userInfo

    /** [LiveData] о том, что пользователь залогинился */
    val signInUserLiveData: LiveData<Unit>
        get() = signInUser

    /** [LiveData] для записи информации в shared preferences */
    val sharedPreferencesUserInfoLiveData: LiveData<Map<UserField, String?>>
        get() = sharedPreferencesUserInfo

    /** [LiveData] для показа сохраненной информации о пользователе */
    val showReservedUserInfoLiveData: LiveData<Unit>
        get() = showReservedUserInfo

    /** [LiveData] для отображения загрузки */
    val showProgressBarLiveData: LiveData<Boolean>
        get() = showProgressBar

    fun init(prefs: SharedPreferences) {
        auth = FirebaseAuth.getInstance()
        settings = prefs
        if (settings.getString(UserField.ID.preferencesKey, null) == null
            || auth.currentUser == null
        ) {
            showLoggedUserLayout.value = false
        } else {
            showReservedUserInfo.value = Unit
            decideIfUserIsAlive()
        }
    }

    private fun decideIfUserIsAlive() {
        auth.currentUser?.reload()?.addOnCompleteListener {
            if (auth.currentUser == null) {
                showLoggedUserLayout.value = false
            }
        }
    }

    fun successfullySigned(firebaseUser: FirebaseUser, providerType: String? = null) {
        showLoggedUserLayout.value = true
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ref = databaseReference.child(USER_DATABASE_ENDPOINT).child(firebaseUser.uid)
        ref.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                if (user != null) {
                    userInfo.value = user
                    sharedPreferencesUserInfo.value = userConverter.convertForSettings(user)
                    showProgressBar.value = false
                } else {
                    user = providerType?.let { createUser(firebaseUser, it) }
                    databaseReference.child(USER_DATABASE_ENDPOINT).child(firebaseUser.uid)
                        .setValue(user)
                }
            }

        })
    }

    private fun deleteUserFromDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ref = databaseReference.child(USER_DATABASE_ENDPOINT).child(
            settings.getString(UserField.ID.preferencesKey, EMPTY_STRING) ?: EMPTY_STRING
        )
        ref.removeValue()
    }

    private fun createUser(firebaseUser: FirebaseUser, providerType: String) =
        User(
            firebaseUser.uid,
            firebaseUser.displayName ?: EMPTY_STRING,
            providerType,
            firebaseUser.email ?: EMPTY_STRING,
            firebaseUser.phoneNumber ?: EMPTY_STRING,
            EMPTY_STRING,
            AccountType.TEAMER.title,
            EMPTY_STRING,
            EMPTY_STRING
        )


    fun changeUserInfo(changedFields: Map<UserField, String>) {
        for (i in changedFields) {
            when (i.key) {
                UserField.NAME -> changeUserField(i.key.preferencesKey, i.value)
                UserField.PHONE -> changeUserField(i.key.preferencesKey, i.value)
                UserField.CITY -> changeUserField(i.key.preferencesKey, i.value)
                UserField.BIRTHDAY -> changeUserField(i.key.preferencesKey, i.value)
                UserField.ACCOUNT_TYPE -> changeUserField(i.key.preferencesKey, i.value)
                UserField.EMAIL -> changeUserField(i.key.preferencesKey, i.value)
            }
        }
        auth.currentUser?.let { successfullySigned(it) }
    }

    private fun changeUserField(endpoint: String, value: String) {
        val userId = auth.currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference
        userId?.let {
            val ref = databaseReference.child(USER_DATABASE_ENDPOINT)
                .child(userId)
                .child(endpoint)
            ref.setValue(value)
        }

    }

    fun signInUser() {
        signInUser.value = Unit
        showProgressBar.value = true
    }

    fun uploadUserAvatar(file: File?, id: String?) {
        Log.i("File path", file?.absolutePath.toString())
        Log.i("File path can", file?.canonicalPath.toString())
        val stream = FileInputStream(file?.absoluteFile)
        val storage = Firebase.storage

        id?.let {
            val avatarRef = storage.reference.child("avatars/${it}.jpg")
            val uploadTask = avatarRef.putStream(stream)
            uploadTask.addOnFailureListener {
                Log.i("Avatar", "Error")
            }.addOnSuccessListener { taskSnapshot ->
                Log.i("Avatar", "Success")
            }
        }
    }

    companion object {
        private const val USER_DATABASE_ENDPOINT = "users"
    }
}