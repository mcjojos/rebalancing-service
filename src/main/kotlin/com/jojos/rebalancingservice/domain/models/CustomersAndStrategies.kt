package com.jojos.rebalancingservice.domain.models

/**
 * A data class that holds information about all customers, strategies
 * and the 1 to 1 relationship (if it exists) between them
 */
data class CustomersAndStrategies(
    val customers: List<Customer>,
    val strategies: List<Strategy>,
    val customerToStrategy: MutableMap<Int, Int>
)

fun CustomersAndStrategies.onlyCustomersWithStrategies(): List<Customer> =
    this.customers.filter {
        this.customerToStrategy[it.customerId] != null
    }

/**
 * If no strategy is matched for a given customer then
 * the default strategy is returned, the one with cashPercentage = 100
 */
fun CustomersAndStrategies.strategyOfCustomer(customerId: Int): Strategy =
    strategies.find { it.strategyId == this.customerToStrategy[customerId] } ?: Strategy()
//    this.map.mapKeys { entry -> this.customers[entry.key] }.keys
