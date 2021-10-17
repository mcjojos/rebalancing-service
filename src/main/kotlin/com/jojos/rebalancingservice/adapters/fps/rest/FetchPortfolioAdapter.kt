package com.jojos.rebalancingservice.adapters.fps.rest

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio
import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.common.retry
import com.jojos.rebalancingservice.config.properties.FpsApiClientProperties
import com.jojos.rebalancingservice.domain.exception.ConnectionException
import com.jojos.rebalancingservice.domain.ports.FetchPortfolio
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Component
class FetchPortfolioAdapter(
    @Qualifier("fpsApiRestTemplate") private val restTemplate: RestTemplate,
    private val fpsProperties: FpsApiClientProperties
) : FetchPortfolio {

    /**
     * This method is fetching the customer information using the fps-api.
     */
    override fun invoke(customerId: Int): CustomerPortfolio =
        runCatching {
            val url = "${fpsProperties.url}/customer/${customerId}"
            retry {
                log.info("Fetching customer $customerId")
                val response = restTemplate.getForObject<CustomerPortfolio>(url)
                log.info("successfully fetched $response")
                response
            }
        }.getOrElse {
            throw ConnectionException(
                message = "cannot connect to FPS",
                cause = it
            )
        }

}
