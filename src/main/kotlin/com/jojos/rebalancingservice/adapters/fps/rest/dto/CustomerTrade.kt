package com.jojos.rebalancingservice.adapters.fps.rest.dto

data class CustomerTrade(
    val customerId: Int,
    val stocks: Int,
    val bonds: Int,
    val cash: Int
)