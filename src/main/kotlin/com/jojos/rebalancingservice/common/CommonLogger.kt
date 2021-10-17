package com.jojos.rebalancingservice.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/* Adds implicit logger to every class lazily */

val loggerMap = mutableMapOf<KClass<*>, Logger>()

@Suppress("UNUSED_PARAMETER")
inline val <reified T> T.log: Logger
    get() = loggerMap.getOrPut(T::class) {
        LoggerFactory.getLogger(
            if (T::class.isCompanion) T::class.java.enclosingClass else T::class.java
        )
    }