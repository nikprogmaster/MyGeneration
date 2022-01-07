package com.kandyba.mygeneration.presentation.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    val rxCompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        rxCompositeDisposable.dispose()
    }

    protected fun deleteDisposable(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            rxCompositeDisposable.remove(disposable)
        }
    }

    fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
        apply { compositeDisposable.add(this) }
}