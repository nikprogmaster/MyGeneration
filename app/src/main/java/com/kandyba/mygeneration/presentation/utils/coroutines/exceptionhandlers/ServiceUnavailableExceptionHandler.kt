package com.kandyba.mygeneration.presentation.utils.coroutines.exceptionhandlers

import android.util.Log

internal fun serviceUnavailableExceptionHandler(): BaseCoroutineExceptionHandler =
    lambdaCoroutineExceptionHandler { _, e ->
        Log.e(e.cause.toString(), e.message.toString())
    }