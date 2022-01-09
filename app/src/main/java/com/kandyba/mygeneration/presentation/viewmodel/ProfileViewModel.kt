
package com.kandyba.mygeneration.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private val sharedPreferencesUserInfo = MutableLiveData<Map<UserField, String?>>()
    private val showReservedUserInfo = MutableLiveData<Unit>()
    private val showProgressBar = MutableLiveData<Boolean>()
    private val clearSharedPreferences = MutableLiveData<Unit>()

    private lateinit var auth: FirebaseAuth
    private lateinit var settings: SharedPreferences

    val showLoggedUserLayoutLiveData: LiveData<Boolean>
        get() = showLoggedUserLayout
    val userInfoLiveData: LiveData<User>
        get() = userInfo
    val signInUserLiveData: LiveData<Unit>
        get() = signInUser
    val sharedPreferencesUserInfoLiveData: LiveData<Map<UserField, String?>>
        get() = sharedPreferencesUserInfo
    val showReservedUserInfoLiveData: LiveData<Unit>
        get() = showReservedUserInfo
    val showProgressBarLiveData: LiveData<Boolean>
        get() = showProgressBar
    val clearSharedPreferencesLiveData: LiveData<Unit>
        get() = clearSharedPreferences

    fun init(prefs: SharedPreferences) {
        auth = FirebaseAuth.getInstance()
        settings = prefs

        auth.currentUser?.reload()?.addOnCompleteListener {
            val user = auth.currentUser
            if (user == null) {
                showLoggedUserLayout.value = false
                clearSharedPreferences.value = Unit
                deleteUserFromDatabase()
            } else {
                showReservedUserInfo.value = Unit
                showLoggedUserLayout.value = true
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

    companion object {
        private const val USER_DATABASE_ENDPOINT = "users"
    }
}