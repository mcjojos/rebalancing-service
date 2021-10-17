package com.jojos.rebalancingservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("fps")
data class FpsApiClientProperties(
    val url: String,
    val batchSize: Int = 1,
    val connectionTimeout: Long = 500,
    val readTimeout: Long = 3000
)