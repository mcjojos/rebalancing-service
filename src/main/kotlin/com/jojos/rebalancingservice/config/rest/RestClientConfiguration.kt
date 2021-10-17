package com.jojos.rebalancingservice.config.rest

import com.jojos.rebalancingservice.config.properties.FpsApiClientProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestClientConfiguration {
    @Bean("fpsApiRestTemplate")
    fun fpsApiRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        fpsApiClientProperties: FpsApiClientProperties
    ): RestTemplate {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(fpsApiClientProperties.connectionTimeout))
            .setReadTimeout(Duration.ofMillis(fpsApiClientProperties.readTimeout))
            .additionalInterceptors(ContentTypeJsonInterceptor())
            .build()
    }
}