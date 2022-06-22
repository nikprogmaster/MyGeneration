package com.kandyba.mygeneration.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class AppViewModel : BaseViewModel() {

    private val openMainFragment = MutableLiveData<Unit>()
    private val launchProfile = MutableLiveData<Unit>()

    val openMainFragmentLiveData: LiveData<Unit>
        get() = openMainFragment


    val launchProfileLiveData: LiveData<Unit>
        get() = launchProfile

    fun launchProfileActivity() {
        launchProfile.postValue(Unit)
    }
}