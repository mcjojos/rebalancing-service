package com.jojos.rebalancingservice.domain.models.validator

import com.jojos.rebalancingservice.domain.models.Strategy
import org.springframework.stereotype.Component

@Component
class PercentagesValidator {
    fun isValid(value: Strategy) =
        value.sumOfPercentages() == 100

    private fun Strategy.sumOfPercentages() =
        this.bondsPercentage + this.stocksPercentage + this.cashPercentage
}
