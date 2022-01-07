
package com.kandyba.mygeneration.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.data.AccountType
import com.kandyba.mygeneration.models.data.User
import com.kandyba.mygeneration.models.presentation.user.UserConverter
import com.kandyba.mygeneration.models.presentation.user.UserField
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userConverter: UserConverter
) : ViewModel() {

    private val showLoggedUserLayout = MutableLiveData<Boolean>()
    private val userInfo = MutableLiveData<User>()
    private val signInUser = MutableLiveData<Unit>()
    private val sharedPreferencesUserInfo = MutableLiveData<User>()
    private val showReservedUserInfo = MutableLiveData<Unit>()
    private val showProgressBar = MutableLiveData<Boolean>()
    private val clearSharedPreferences = MutableLiveData<Unit>()

    private lateinit var auth: FirebaseAuth
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    val showLoggedUserLayoutLiveData: LiveData<Boolean>
        get() = showLoggedUserLayout
    val userInfoLiveData: LiveData<User>
        get() = userInfo
    val signInUserLiveData: LiveData<Unit>
        get() = signInUser
    val sharedPreferencesUserInfoLiveData: LiveData<User>
        get() = sharedPreferencesUserInfo
    val showReservedUserInfoLiveData: LiveData<Unit>
        get() = showReservedUserInfo
    val showProgressBarLiveData: LiveData<Boolean>
        get() = showProgressBar
    val clearSharedPreferencesLiveData: LiveData<Unit>
        get() = clearSharedPreferences

    @SuppressLint("CommitPrefEdits")
    fun init(prefs: SharedPreferences) {
        auth = FirebaseAuth.getInstance()
        settings = prefs
        editor = settings.edit()

        auth.currentUser?.reload()?.addOnCompleteListener {
            val user = auth.currentUser
            if (user == null) {
                showLoggedUserLayout.value = false
                clearSharedPreferences.value = Unit
                deleteUserFromDatabase()
                Log.i("User", "is null")
            } else {
                showReservedUserInfo.value = Unit
                showLoggedUserLayout.value = true
                Log.i("User", "is not null")
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
                    sharedPreferencesUserInfo.value = user
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
            settings.getString(USER_ID_KEY, EMPTY_STRING) ?: EMPTY_STRING
        )
        ref.removeValue()
    }

    private fun createUser(firebaseUser: FirebaseUser, providerType: String) =
        User(
            firebaseUser.uid,
            firebaseUser.displayName ?: "",
            providerType,
            firebaseUser.email ?: "",
            firebaseUser.phoneNumber ?: "",
            "",
            AccountType.TEAMER.title,
            "",
            ""
        )


    fun changeUserInfo(changedFields: MutableMap<UserField, String>) {
        for (i in changedFields) {
            when (i.key) {
                UserField.FIO -> changeUserField(NAME_ENDPOINT, i.value)
                UserField.PHONE -> changeUserField(PHONE_NUMBER_ENDPOINT, i.value)
                UserField.CITY -> changeUserField(CITY_ENDPOINT, i.value)
                UserField.BIRTHDAY -> changeUserField(BIRTHDAY_ENDPOINT, i.value)
                UserField.ACCOUNT_TYPE -> changeUserField(ACCOUNT_ENDPOINT, i.value)
                UserField.EMAIL -> changeUserField(EMAIL_ENDPOINT, i.value)
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

    companion object {
        private const val USER_DATABASE_ENDPOINT = "users"
        private const val NAME_ENDPOINT = "name"
        private const val PHONE_NUMBER_ENDPOINT = "phoneNumber"
        private const val EMAIL_ENDPOINT = "email"
        private const val ACCOUNT_ENDPOINT = "accountType"
        private const val CITY_ENDPOINT = "city"
        private const val BIRTHDAY_ENDPOINT = "birthday"
        private const val USER_ID_KEY = "id"
    }
}