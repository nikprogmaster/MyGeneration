package com.kandyba.mygeneration.presentation.utils.coroutines.exceptionhandlers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

internal class AggregationExceptionHandler(
    private val handlerList: List<HandlerWithChecker>,
    private val defaultHandler: CoroutineExceptionHandler
) : BaseCoroutineExceptionHandler() {

    internal data class HandlerWithChecker(
        val checker: (Throwable) -> Boolean,
        val handler: CoroutineExceptionHandler
    )

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val handler = handlerList.find { it.checker(exception) }?.handler ?: defaultHandler
        handler.handleException(context, exception)
    }

    class Builder {
        private val handlerList: MutableList<HandlerWithChecker> = mutableListOf()
        private var defaultHandler: CoroutineExceptionHandler =
            CoroutineExceptionHandler { _, _ -> }

        fun addHandler(
            handler: CoroutineExceptionHandler,
            checker: (Throwable) -> Boolean
        ): Builder {
            handlerList.add(HandlerWithChecker(checker, handler))
            return this
        }

        fun addHandler(handlerWithChecker: HandlerWithChecker): Builder {
            handlerList.add(handlerWithChecker)
            return this
        }

        fun setDefaultHandler(handler: CoroutineExceptionHandler): Builder {
            defaultHandler = handler
            return this
        }

        fun build(): BaseCoroutineExceptionHandler =
            AggregationExceptionHandler(handlerList, defaultHandler)
    }
}