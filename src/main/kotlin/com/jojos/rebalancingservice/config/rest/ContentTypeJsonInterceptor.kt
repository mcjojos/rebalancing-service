package com.jojos.rebalancingservice.config.rest

import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class ContentTypeJsonInterceptor : ClientHttpRequestInterceptor {
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        request.headers.contentType = MediaType.APPLICATION_JSON
        request.headers.accept = listOf(MediaType.APPLICATION_JSON)
        return execution.execute(request, body)
    }
}
