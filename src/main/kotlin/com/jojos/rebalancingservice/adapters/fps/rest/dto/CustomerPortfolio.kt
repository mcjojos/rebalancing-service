package com.jojos.rebalancingservice.adapters.fps.rest.dto

data class CustomerPortfolio(
    val customerId: Int,
    val stocks: Int,
    val bonds: Int,
    val cash: Int
)

fun CustomerPortfolio.totalSum() =
    stocks + bonds + cash