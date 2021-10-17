package com.jojos.rebalancingservice.domain.ports

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio

interface FetchPortfolio {
    operator fun invoke(customerId: Int): CustomerPortfolio
}
