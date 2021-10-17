package com.jojos.rebalancingservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = ["com.jojos.rebalancingservice.config"])
class RebalancingServiceApplication

fun main(args: Array<String>) {
    runApplication<RebalancingServiceApplication>(*args)
}
