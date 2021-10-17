package com.jojos.rebalancingservice.domain.models

import org.springframework.stereotype.Component

@Component
class CustomerToStrategyMapper : (List<Customer>, List<Strategy>) -> CustomersAndStrategies {
    override fun invoke(customers: List<Customer>, strategies: List<Strategy>): CustomersAndStrategies {
        val customerToStrategy = mutableMapOf<Int, Int>()

        customers.forEach { c ->
            val strategy = strategies
                .filter { c.riskLevel <= it.maxRiskLevel }
                .filter { c.riskLevel >= it.minRiskLevel }
                .filter { c.yearsToRetirement <= it.maxYearsToRetirement }
                .firstOrNull { c.yearsToRetirement >= it.minYearsToRetirement }
            if (strategy != null) {
                customerToStrategy[c.customerId] = strategy.strategyId
            }
        }
        return CustomersAndStrategies(customers, strategies, customerToStrategy)
    }
}