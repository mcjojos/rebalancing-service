package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio
import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade
import com.jojos.rebalancingservice.domain.models.Strategy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class RebalancePortfolioServiceTest {

    private val rebalancePortfolio = RebalancePortfolioService()

    @ParameterizedTest
    @MethodSource("providePortfoliosAndStrategies")
    fun `should rebalance existing portfolio correctly according to strategy`(
        existingPortfolio: CustomerPortfolio, strategy: Strategy, expected: CustomerTrade
    ) {
        val newPortfolio = rebalancePortfolio(existingPortfolio, strategy)
        with(newPortfolio) {
            Assertions.assertEquals(expected.stocks, this.stocks)
            Assertions.assertEquals(expected.bonds, this.bonds)
            Assertions.assertEquals(expected.cash, this.cash)
        }
    }

    private companion object {
        @JvmStatic
        fun providePortfoliosAndStrategies(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    CustomerPortfolio(1, 20, 20, 60),
                    Strategy(stocksPercentage = 25, bondsPercentage = 35, cashPercentage = 40),
                    CustomerTrade(1, 5, 15, -20)),
                Arguments.of(
                    CustomerPortfolio(1, 6700, 1200, 400),
                    Strategy(stocksPercentage = 20, bondsPercentage = 30, cashPercentage = 50),
                    CustomerTrade(1, -5040, 1290, 3750)),
                Arguments.of(
                    CustomerPortfolio(1, 1500, 3500, 3500),
                    Strategy(),
                    CustomerTrade(1, -1500, -3500, 5000))
            )
        }
    }
}