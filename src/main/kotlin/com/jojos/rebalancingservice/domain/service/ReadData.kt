package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.domain.models.Customer
import com.jojos.rebalancingservice.domain.models.CustomersAndStrategies
import com.jojos.rebalancingservice.domain.models.Strategy
import org.springframework.stereotype.Service

@Service
class ReadData(
    private val readCustomers: ReadCustomers,
    private val readStrategies: ReadStrategies,
    private val toCustomersAndStrategiesMapper: (List<Customer>, List<Strategy>) -> CustomersAndStrategies
) {
    operator fun invoke(): CustomersAndStrategies {
        val customers = readCustomers()
        log.info("${customers.size} customers loaded")

        val strategy = readStrategies()
        log.info("${strategy.size} strategies loaded")
        return toCustomersAndStrategiesMapper(customers, strategy)
    }
}