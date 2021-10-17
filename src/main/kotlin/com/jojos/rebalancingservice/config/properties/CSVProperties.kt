package com.jojos.rebalancingservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("csv")
data class CSVProperties(
    val customersFile: String,
    val strategiesFile: String
)