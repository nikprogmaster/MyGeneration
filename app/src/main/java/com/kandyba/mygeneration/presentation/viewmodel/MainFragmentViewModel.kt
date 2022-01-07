package com.kandyba.mygeneration.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kandyba.mygeneration.domain.WallInteractor
import com.kandyba.mygeneration.models.data.WallResponse
import com.kandyba.mygeneration.models.presentation.calendar.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainFragmentViewModel(
    private val wallInteractor: WallInteractor
) : BaseViewModel() {

    private val getEvents = MutableLiveData<List<Event>>()
    private val openBottomCalendarFragment = MutableLiveData<List<Event>>()
    private val showStartAnimation = MutableLiveData<Boolean>()
    private val vkPosts = MutableLiveData<WallResponse>()

    val getEventsLiveData: LiveData<List<Event>>
        get() = getEvents
    val openBottomCalendarFragmentLiveData: LiveData<List<Event>>
        get() = openBottomCalendarFragment
    val showStartAnimationLiveData: LiveData<Boolean>
        get() = showStartAnimation
    val vkPostsLiveData: LiveData<WallResponse>
        get() = vkPosts

    fun loadEvents() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ref = databaseReference.child(CALENDAR_DATABASE_ENDPOINT)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                showStartAnimation.value = false
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()
                for (i in snapshot.children) {
                    val event = i.getValue(Event::class.java)
                    if (event != null)
                        eventList.add(event)
                }
                getEvents.value = eventList
                showStartAnimation.value = false
            }
        })
    }

    fun loadVkPosts(postCount: Int) {
        wallInteractor.getWallPosts(postCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i("Posts", it.response.items.toString())
                    vkPosts.postValue(it)
                },
                {
                    Log.e("VkError", "Что-то не так")
                    //showError
                }
            ).addTo(rxCompositeDisposable)
    }

    fun openBottomCalendarFragment(event: List<Event>) {
        openBottomCalendarFragment.value = event
    }

    companion object {
        private const val CALENDAR_DATABASE_ENDPOINT = "calendar"
    }
}