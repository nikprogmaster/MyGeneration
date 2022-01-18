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
import com.kandyba.mygeneration.models.presentation.SingleLiveEvent
import com.kandyba.mygeneration.models.presentation.calendar.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

class MainFragmentViewModel(
    private val wallInteractor: WallInteractor,
    private val areEventsLoaded: ReplaySubject<Unit>,
    private val areVkPostsLoaded: ReplaySubject<Unit>
) : BaseViewModel() {

    private val getEvents = MutableLiveData<List<Event>>()
    private val openBottomCalendarFragment = SingleLiveEvent<List<Event>>()
    private val vkPosts = MutableLiveData<WallResponse>()
    private var postCount = INITIAL_POSTS_COUNT

    val getEventsLiveData: LiveData<List<Event>>
        get() = getEvents
    val openBottomCalendarFragmentLiveData: LiveData<List<Event>>
        get() = openBottomCalendarFragment
    val vkPostsLiveData: LiveData<WallResponse>
        get() = vkPosts

    fun loadEvents() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ref = databaseReference.child(CALENDAR_DATABASE_ENDPOINT)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                areEventsLoaded.onNext(Unit)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()
                for (i in snapshot.children) {
                    val event = i.getValue(Event::class.java)
                    if (event != null)
                        eventList.add(event)
                }
                getEvents.value = eventList
                areEventsLoaded.onNext(Unit)
            }
        })
    }

    fun loadVkPosts(double: Boolean) {
        if (double) postCount *= 2
        wallInteractor.getWallPosts(postCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { areVkPostsLoaded.onNext(Unit) }
            .subscribe(
                {
                    Log.i("Posts", it.response.items.toString())
                    vkPosts.postValue(it)
                    areVkPostsLoaded.onNext(Unit)
                },
                {
                    Log.e("VkError", it.message.toString())
                    areVkPostsLoaded.onNext(Unit)
                }
            ).addTo(rxCompositeDisposable)
    }

    fun openBottomCalendarFragment(event: List<Event>) {
        openBottomCalendarFragment.value = event
    }

    companion object {
        private const val CALENDAR_DATABASE_ENDPOINT = "calendar"
        private const val INITIAL_POSTS_COUNT = 100
    }
}