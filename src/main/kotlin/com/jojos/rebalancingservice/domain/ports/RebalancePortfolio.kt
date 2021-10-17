package com.jojos.rebalancingservice.domain.ports

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio
import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade
import com.jojos.rebalancingservice.domain.models.Strategy

interface RebalancePortfolio {
    operator fun invoke(existingPortfolio: CustomerPortfolio, strategy: Strategy): CustomerTrade
}