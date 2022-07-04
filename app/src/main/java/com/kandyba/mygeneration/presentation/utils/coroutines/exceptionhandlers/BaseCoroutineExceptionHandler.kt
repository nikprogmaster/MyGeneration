package com.kandyba.mygeneration.presentation.utils.coroutines.exceptionhandlers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler
}