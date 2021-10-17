package com.jojos.rebalancingservice.domain.models

data class Strategy(
    val strategyId: Int = -1,
    val minRiskLevel: Int = -1,
    val maxRiskLevel: Int = -1,
    val minYearsToRetirement: Int = -1,
    val maxYearsToRetirement: Int = -1,
    val stocksPercentage: Int = 0,
    val bondsPercentage: Int = 0,
    val cashPercentage: Int = 100
)

