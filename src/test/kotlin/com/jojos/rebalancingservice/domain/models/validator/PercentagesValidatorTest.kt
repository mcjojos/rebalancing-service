package com.jojos.rebalancingservice.domain.models.validator

import com.jojos.rebalancingservice.domain.models.Strategy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PercentagesValidatorTest {
    private val validator = PercentagesValidator()

    @ParameterizedTest
    @MethodSource("provideCorrectPercentages")
    fun `should approve strategies whose percentages add up to 100`(stocks: Int, cash: Int, bonds: Int) {
        val strategy = Strategy(1,0,3,20,30,stocks,cash,bonds)
        val res = validator.isValid(strategy)
        Assertions.assertTrue(res)
    }

    @ParameterizedTest
    @MethodSource("provideWrongPercentages")
    fun `should not approve strategies whose percentages do not add up to 100`(stocks: Int, cash: Int, bonds: Int) {
        val strategy = Strategy(1,0,3,20,30,stocks,cash,bonds)
        val res = validator.isValid(strategy)
        Assertions.assertFalse(res)
    }

    private companion object {
        @JvmStatic
        fun provideCorrectPercentages(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(10, 10, 80),
                Arguments.of(20, 20, 60),
                Arguments.of(25, 25, 50),
                Arguments.of(0, 0, 100),
            )
        }

        @JvmStatic
        fun provideWrongPercentages(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(1000, 0, 0),
                Arguments.of(1, 1, 99),
                Arguments.of(0, 0, 99),
            )
        }
    }
}