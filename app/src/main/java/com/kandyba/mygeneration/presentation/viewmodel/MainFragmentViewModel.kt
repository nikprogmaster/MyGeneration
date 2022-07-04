package com.kandyba.mygeneration.presentation.viewmodel

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kandyba.mygeneration.data.repository.EventsRepository
import com.kandyba.mygeneration.data.repository.RegionsRepository
import com.kandyba.mygeneration.data.repository.WallRepository
import com.kandyba.mygeneration.models.presentation.SingleLiveEvent
import com.kandyba.mygeneration.models.presentation.VkPost
import com.kandyba.mygeneration.models.presentation.calendar.Event
import com.kandyba.mygeneration.models.presentation.user.Region
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainFragmentViewModel(
    private val wallRepository: WallRepository,
    private val eventsRepository: EventsRepository,
    private val regionsRepository: RegionsRepository
) : BaseViewModel() {

    private var postCount = INITIAL_POSTS_COUNT

    private val _openBottomSheet = SingleLiveEvent<DialogFragment>()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    private val _vkPosts = MutableStateFlow<List<VkPost>>(emptyList())
    private val _allDataLoaded = MutableLiveData<Unit>()

    val openBottomSheet: LiveData<DialogFragment>
        get() = _openBottomSheet
    val events: StateFlow<List<Event>>
        get() = _events
    val vkPosts: StateFlow<List<VkPost>>
        get() = _vkPosts
    val allDataLoaded: LiveData<Unit>
        get() = _allDataLoaded

    // Use SupervisorJob instead of Job to get non-cancelable-parent coroutine
    val scope =
        CoroutineScope(Job() + Dispatchers.IO + Dispatchers.Main + CoroutineName("My coroutine"))

    private var regionCode: String = Region.COMMON.regionCode

    fun init(code: String) {
        regionCode = code
        viewModelScope.launch {
            val eventsLoaded = launch(context) { loadEvents(regionCode) }
            val vkPostsLoaded = launch(context) { loadVkPosts(false) }
            val regionsLoaded = launch(context) { loadRegions() }
            joinAll(eventsLoaded, vkPostsLoaded, regionsLoaded)
            _allDataLoaded.postValue(Unit)
        }
    }

    fun openBottomFragment(fragment: DialogFragment) {
        _openBottomSheet.value = fragment
    }

    fun addNewEvent(event: Event) {
        viewModelScope.launch(context) {
            try {
                val result = eventsRepository.addEvent(event)
                if (result) {
                    _events.postValue(eventsRepository.updateEvents(regionCode))
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun getEvents(code: String?) {
        code?.let { regionCode = it }
        viewModelScope.launch(context) {
            _events.postValue(eventsRepository.getEvents(regionCode))
        }
    }

    private suspend fun loadEvents(regionCode: String) {
        val events = eventsRepository.getEvents(regionCode)
        Log.i(TAG, events.toString())
        _events.postValue(events)
    }

    private suspend fun loadVkPosts(double: Boolean) {
        if (double) postCount *= 2

        // coroutineScope {  }

        // This scope have to handle errors by itself, otherwise the app will crash!
        //supervisorScope {  }

        // При запуске с помощью launch { } исключения появляются сразу
        val result = wallRepository.getWallPosts(postCount)
        _vkPosts.postValue(result)

        // При запуске с помощью async{} исключения появляются только при вызове метода await()
        // scope.async {  }
    }

    private suspend fun loadRegions() {
        regionsRepository.getRegions()
    }

    companion object {
        private const val TAG = "MainFragmentViewModel"
        private const val INITIAL_POSTS_COUNT = 100
    }
}