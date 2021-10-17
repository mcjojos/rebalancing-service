package com.jojos.rebalancingservice.domain.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class ConnectionException(
    val status: HttpStatus = HttpStatus.NOT_FOUND,
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()