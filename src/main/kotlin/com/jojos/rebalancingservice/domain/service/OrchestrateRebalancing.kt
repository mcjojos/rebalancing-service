package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.domain.models.onlyCustomersWithStrategies
import com.jojos.rebalancingservice.domain.models.strategyOfCustomer
import com.jojos.rebalancingservice.domain.ports.FetchPortfolio
import com.jojos.rebalancingservice.domain.ports.RebalancePortfolio
import com.jojos.rebalancingservice.domain.ports.SendTrades
import org.springframework.stereotype.Component

@Component
class OrchestrateRebalancing(
    private val readData: ReadData,
    private val fetchPortfolio: FetchPortfolio,
    private val rebalancePortfolio: RebalancePortfolio,
    private val sendTrades: SendTrades
) {
    operator fun invoke() {
        val customersAndStrategies = readData()
        val customerExistingPortfoliosToStrategies = customersAndStrategies
            .onlyCustomersWithStrategies()
            .associate { c -> fetchPortfolio(c.customerId) to customersAndStrategies.strategyOfCustomer(c.customerId) }
        val trades = customerExistingPortfoliosToStrategies
            .map { (portfolio, strategy) -> rebalancePortfolio(portfolio, strategy) }
        sendTrades(trades)
    }

}