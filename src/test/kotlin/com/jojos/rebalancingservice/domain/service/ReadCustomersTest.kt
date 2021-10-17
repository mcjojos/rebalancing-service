package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.config.properties.CSVProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

internal class ReadCustomersTest {
    @Test
    fun `should load customers successfully`() {
        val readCustomers = ReadCustomers(CSVProperties("src/test/resources/data/customers.csv", ""))
        val customers = readCustomers()
        Assertions.assertEquals(2, customers.size)
    }

    @Test
    fun `should throw exception on non-existent customers file`() {
        val readCustomers = ReadCustomers(CSVProperties("not-here", ""))
        assertThrows<java.nio.file.NoSuchFileException> {
            readCustomers()
        }
    }

    @Test
    fun `should calculate correct positive years to retirement`() {
        val retirementAge = 70
        val dateOfBirth = LocalDate.parse("1961-04-29", ReadCustomers.dateFormatter)
        val expected = 2031 - LocalDate.now().year
        Assertions.assertEquals(expected, yearsToRetirement(dateOfBirth, retirementAge))
    }

    @Test
    fun `should calculate negative years to retirement`() {
        val retirementAge = 70
        val dateOfBirth = LocalDate.parse("1941-04-29", ReadCustomers.dateFormatter)
        val expected = 2011 - LocalDate.now().year
        Assertions.assertEquals(expected, yearsToRetirement(dateOfBirth, retirementAge))
    }

}