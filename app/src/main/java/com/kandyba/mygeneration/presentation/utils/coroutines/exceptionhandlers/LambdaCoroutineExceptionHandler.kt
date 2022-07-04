package com.kandyba.mygeneration.presentation.utils.coroutines.exceptionhandlers

import kotlin.coroutines.CoroutineContext

internal open class LambdaCoroutineExceptionHandler(
    private val handler: (context: CoroutineContext, exception: Throwable) -> Unit
) : BaseCoroutineExceptionHandler() {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        handler(context, exception)
    }
}

internal fun lambdaCoroutineExceptionHandler(handler: (context: CoroutineContext, exception: Throwable) -> Unit) =
    LambdaCoroutineExceptionHandler(handler)