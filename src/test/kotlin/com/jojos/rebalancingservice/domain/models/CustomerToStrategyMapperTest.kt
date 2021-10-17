package com.jojos.rebalancingservice.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CustomerToStrategyMapperTest {
    private val customerToStrategyMapper = CustomerToStrategyMapper()
    private val strategies = mutableListOf<Strategy>()

    @BeforeAll
    fun init() {
        strategies.add(Strategy(1,0,3,20,30,20,20,60))
        strategies.add(Strategy(2,0,3,10,20,10,20,70))
        strategies.add(Strategy(3,6,9,20,30,10,0,90))
        strategies.add(Strategy(4,0,0,10,30,20,30,50))
    }

    @Test
    fun `should not map bob to any strategy because of years of retirement`() {
        val customer = Customer(1, "bob@bob.com",  3, 4)
        val customersWithStrategies = customerToStrategyMapper(listOf(customer), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isEmpty())
    }

    @Test
    fun `should not map sally to any strategy because of unmatched risk level`() {
        val sally = Customer(1, "sally@gmail.com",  10, 15)
        val customersWithStrategies = customerToStrategyMapper(listOf(sally), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isEmpty())
    }

    @Test
    fun `should map sally to second strategy testing minRiskLevel inclusive`() {
        val sally = Customer(1, "sally@gmail.com",  0, 15)
        val customersWithStrategies = customerToStrategyMapper(listOf(sally), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isNotEmpty())
        Assertions.assertEquals(2, customersWithStrategies.customerToStrategy[1])
    }

    @Test
    fun `should map sally to third strategy testing minYearsOfRetirement inclusive`() {
        val sally = Customer(1, "sally@gmail.com",  7, 20)
        val customersWithStrategies = customerToStrategyMapper(listOf(sally), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isNotEmpty())
        Assertions.assertEquals(3, customersWithStrategies.customerToStrategy[1])
    }


    @Test
    fun `should map sally to third strategy testing maxYearsOfRetirement inclusive`() {
        val sally = Customer(1, "sally@gmail.com",  7, 30)
        val customersWithStrategies = customerToStrategyMapper(listOf(sally), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isNotEmpty())
        Assertions.assertEquals(3, customersWithStrategies.customerToStrategy[1])
    }

    @Test
    fun `should test overlapping strategies and take the first one`() {
        val sally = Customer(1, "sally@gmail.com",  3, 20)
        val customersWithStrategies = customerToStrategyMapper(listOf(sally), strategies)
        Assertions.assertTrue(customersWithStrategies.customerToStrategy.isNotEmpty())
        Assertions.assertEquals(1, customersWithStrategies.customerToStrategy[1])
    }

}