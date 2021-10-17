package com.jojos.rebalancingservice.domain.ports

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade

interface SendTrades {
    operator fun invoke(trades: Collection<CustomerTrade>)
}
