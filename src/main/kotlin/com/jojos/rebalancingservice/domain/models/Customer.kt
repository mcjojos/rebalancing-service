package com.jojos.rebalancingservice.domain.models

data class Customer(
    val customerId: Int,
    val email: String,
    val riskLevel: Int,
    val yearsToRetirement: Int
)
