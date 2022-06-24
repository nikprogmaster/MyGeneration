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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class MainFragmentViewModel(
    private val wallRepository: WallRepository,
    private val eventsRepository: EventsRepository,
    private val regionsRepository: RegionsRepository
) : BaseViewModel() {

    private var postCount = INITIAL_POSTS_COUNT

    private val _events = MutableLiveData<List<Event>>()
    private val _openBottomSheet = SingleLiveEvent<DialogFragment>()
    private val _vkPosts = MutableLiveData<List<VkPost>>()
    private val _allDataLoaded = MutableLiveData<Unit>()

    val events: LiveData<List<Event>>
        get() = _events
    val openBottomSheet: LiveData<DialogFragment>
        get() = _openBottomSheet
    val vkPosts: LiveData<List<VkPost>>
        get() = _vkPosts
    val allDataLoaded: LiveData<Unit>
        get() = _allDataLoaded

    // Use SupervisorJob instead of Job to get non-cancelable-parent coroutine
    val scope =
        CoroutineScope(Job() + Dispatchers.IO + Dispatchers.Main + CoroutineName("My coroutine"))

    private val coroutineContext = SupervisorJob() + Dispatchers.IO

    fun init() {
        viewModelScope.launch {
            val eventsLoaded = async { loadEvents() }
            val vkPostsLoaded = async { loadVkPosts(false) }
            val regionsLoaded = async { loadRegions() }
            eventsLoaded.await()
            vkPostsLoaded.await()
            regionsLoaded.await()
            _allDataLoaded.postValue(Unit)
        }
    }

    private suspend fun loadEvents() {
        val events = eventsRepository.getEvents(CALENDAR_DATABASE_ENDPOINT)
            .first()
        _events.postValue(events)
    }

    private suspend fun loadVkPosts(double: Boolean) {
        if (double) postCount *= 2

        // coroutineScope {  }

        // This scope have to handle errors by itself, otherwise the app will crash!
        //supervisorScope {  }

        // При запуске с помощью launch { } исключения появляются сразу
        try {
            val result = wallRepository.getWallPosts(postCount)
            _vkPosts.postValue(result)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }

        // При запуске с помощью async{} исключения появляются только при вызове метода await()
        // scope.async {  }
    }

    private suspend fun loadRegions() {
        regionsRepository.getRegions()
    }

    fun openBottomFragment(fragment: DialogFragment) {
        _openBottomSheet.value = fragment
    }

    companion object {
        private const val TAG = "MainFragmentViewModel"
        private const val CALENDAR_DATABASE_ENDPOINT = "calendar"
        private const val INITIAL_POSTS_COUNT = 100
    }
}