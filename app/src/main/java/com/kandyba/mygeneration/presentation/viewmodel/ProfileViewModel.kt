package com.kandyba.mygeneration.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.kandyba.mygeneration.data.repository.UserRepository
import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.presentation.user.User
import com.kandyba.mygeneration.models.presentation.user.UserConverter
import com.kandyba.mygeneration.models.presentation.user.UserField
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val userConverter: UserConverter,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val showLoggedUserLayout = MutableLiveData<Boolean>()
    private val userInfo = MutableLiveData<User>()
    private val signInUser = MutableLiveData<Unit>()
    private val sharedPreferencesUserInfo = MutableLiveData<Map<UserField, String?>>()
    private val showReservedUserInfo = MutableLiveData<Unit>()
    private val showProgressBar = MutableLiveData<Boolean>()

    private lateinit var auth: FirebaseAuth
    private lateinit var settings: SharedPreferences
    private val changedFieldsList = mutableMapOf<UserField, String>()

    /** [LiveData] для отображения layout залогиненного пользователя */
    val showLoggedUserLayoutLiveData: LiveData<Boolean>
        get() = showLoggedUserLayout

    /** [LiveData] для получения информации о пользователе */
    val userModelInfoLiveData: LiveData<User>
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
        viewModelScope.launch {
            userRepository.getUserInfo(firebaseUser.uid)
                .catch { Log.e(TAG, it.message.toString()) }
                .collect { user ->
                    if (user != null) {
                        userInfo.value = user
                        sharedPreferencesUserInfo.value = userConverter.convertForSettings(user)
                        showProgressBar.value = false
                    } else {
                        userRepository.createUser(firebaseUser, providerType)
                    }
                }
        }
    }

    fun addChangedField(field: UserField, value: String) {
        changedFieldsList[field] = value
    }


    fun changeUserInfo() {
        for (i in changedFieldsList) {
            changeUserField(i.key.preferencesKey, i.value)
        }
        auth.currentUser?.let { successfullySigned(it) }
    }

    private fun changeUserField(endpoint: String, value: String) {
        viewModelScope.launch {
            auth.currentUser?.uid?.let { userRepository.changeUserInfo(value, endpoint, it) }
        }
    }

    fun signInUser() {
        signInUser.value = Unit
        showProgressBar.value = true
    }

    fun changeUserAvatar(file: File?, id: String?) {
        viewModelScope.launch {
            userRepository.uploadUserAvatar(file, id)
                ?.addOnSuccessListener { }
                ?.addOnFailureListener { }
        }
    }


    private fun deleteUserFromDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ref = databaseReference.child(USER_DATABASE_ENDPOINT).child(
            settings.getString(UserField.ID.preferencesKey, EMPTY_STRING) ?: EMPTY_STRING
        )
        ref.removeValue()
    }

    companion object {
        private const val TAG = "ProfileViewModel"
        private const val USER_DATABASE_ENDPOINT = "users"
    }
}