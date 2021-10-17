package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.config.properties.CSVProperties
import com.jojos.rebalancingservice.domain.models.validator.PercentagesValidator
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ReadStrategyTest {
    @MockK
    lateinit var percentagesValidator: PercentagesValidator

    @Test
    fun `should load strategies successfully`() {
        every { percentagesValidator.isValid(any()) } returns true
        val readStrategies = ReadStrategies(
            CSVProperties("", "src/test/resources/data/strategy.csv"),
            percentagesValidator)
        val strategy = readStrategies()
        Assertions.assertEquals(3, strategy.size)
    }

    @Test
    fun `should throw exception on non-existent strategy file`() {
        val readStrategies = ReadStrategies(
            CSVProperties("", "not-here"),
            percentagesValidator)
        assertThrows<java.nio.file.NoSuchFileException> {
            readStrategies()
        }
    }
}