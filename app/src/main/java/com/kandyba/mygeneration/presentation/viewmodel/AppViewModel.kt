package com.kandyba.mygeneration.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class AppViewModel(
    private val areVkPostsLoaded: Observable<Unit>,
    private val areEventsLoaded: Observable<Unit>
) : BaseViewModel() {

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
        Observable.zip(areEventsLoaded, areVkPostsLoaded, BiFunction<Unit, Unit, Unit> { _, _ -> })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showAnimation.postValue(false)
            }.addTo(rxCompositeDisposable)
    }

    fun showAnimation(show: Boolean) {
        showAnimation.value = show
    }

    fun launchProfileActivity() {
        launchProfile.postValue(Unit)
    }
}