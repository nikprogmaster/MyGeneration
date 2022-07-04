package com.kandyba.mygeneration.presentation.utils.coroutines.exceptionhandlers

import com.kandyba.mygeneration.presentation.viewmodel.BaseViewModel

internal fun BaseViewModel.buildExceptionHandler() =
    AggregationExceptionHandler.Builder()
        .setDefaultHandler(serviceUnavailableExceptionHandler())
        .build()