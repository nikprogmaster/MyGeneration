package com.kandyba.mygeneration.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timerTask

class AppViewModel : ViewModel() {

    private val showAnimation = MutableLiveData<Boolean>()
    private val openMainFragment = MutableLiveData<Unit>()
    private val launchProfile = MutableLiveData<Unit>()

    val openMainFragmentLiveData: LiveData<Unit>
        get() = openMainFragment

    val showAnimationLiveData: LiveData<Boolean>
        get() = showAnimation

    val launchProfileLiveData: LiveData<Unit>
        get() = launchProfile

    fun init() {
        showAnimation.postValue(true)
    }

    fun showAnimation(show: Boolean) {
        showAnimation.value = show
    }

    fun launchProfileActivity() {
        launchProfile.postValue(Unit)
    }
}