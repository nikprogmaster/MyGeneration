package com.kandyba.mygeneration.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CalendarDialogViewModel : BaseViewModel() {

    private val addNewEventMLD = MutableLiveData<String>()

    val addNewEvent: LiveData<String>
        get() = addNewEventMLD

    fun addNewEvent(event: String) {
        addNewEventMLD.value = event
    }
}