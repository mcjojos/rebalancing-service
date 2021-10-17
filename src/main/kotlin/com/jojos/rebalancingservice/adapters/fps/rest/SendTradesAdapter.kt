package com.jojos.rebalancingservice.adapters.fps.rest

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade
import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.common.retry
import com.jojos.rebalancingservice.config.properties.FpsApiClientProperties
import com.jojos.rebalancingservice.domain.exception.ConnectionException
import com.jojos.rebalancingservice.domain.ports.SendTrades
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SendTradesAdapter(
    @Qualifier("fpsApiRestTemplate") private val restTemplate: RestTemplate,
    private val fpsProperties: FpsApiClientProperties
) : SendTrades {

    /**
     * This method is sending in batch customers trades
     */
    override fun invoke(trades: Collection<CustomerTrade>) {
        trades.chunked(fpsProperties.batchSize) {
            runCatching {
                val url = "${fpsProperties.url}/execute"
                retry {
                    log.info("sending ${it.size} trade(s)")
                    val httpEntity = HttpEntity(it)
                    restTemplate.postForLocation(url, httpEntity)
                }
            }.onFailure {
                throw ConnectionException(
                    message = "cannot connect to FPS",
                    cause = it
                )
            }
        }
    }
}
