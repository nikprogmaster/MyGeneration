package com.kandyba.mygeneration.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (
    private val viewModelCreator: () -> ViewModel
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return viewModelCreator.invoke() as T
    }
}