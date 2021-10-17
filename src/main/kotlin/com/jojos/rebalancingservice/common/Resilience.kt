package com.jojos.rebalancingservice.common

import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.UnknownHttpStatusCodeException

val defaultRetryTemplate: RetryTemplate = RetryTemplate.builder()
    .maxAttempts(3)
    .retryOn(ResourceAccessException::class.java)
    .retryOn(UnknownHttpStatusCodeException::class.java)
    .retryOn(HttpServerErrorException::class.java)
    .uniformRandomBackoff(100, 300)
    .build()

inline fun <R> retry(retryTemplate: RetryTemplate = defaultRetryTemplate, crossinline exec: () -> R): R =
    retryTemplate.execute<R, RuntimeException> {
        exec()
    }
