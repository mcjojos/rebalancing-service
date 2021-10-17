package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio
import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade
import com.jojos.rebalancingservice.adapters.fps.rest.dto.totalSum
import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.domain.models.Strategy
import com.jojos.rebalancingservice.domain.ports.RebalancePortfolio
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class RebalancePortfolioService : RebalancePortfolio {
    override fun invoke(existingPortfolio: CustomerPortfolio, strategy: Strategy): CustomerTrade {
        val total = existingPortfolio.totalSum()
        val newStocks = total * strategy.stocksPercentage / 100.toDouble()
        val newBonds = total * strategy.bondsPercentage / 100.toDouble()
        val newCash = total * strategy.cashPercentage / 100.toDouble()
        log.info("calculating trades for rebalancing")
        return CustomerTrade(
            customerId = existingPortfolio.customerId,
            stocks = newStocks.roundToInt() - existingPortfolio.stocks,
            bonds = newBonds.roundToInt() - existingPortfolio.bonds,
            cash = newCash.roundToInt() - existingPortfolio.cash
        )
    }
}