package com.jojos.rebalancingservice.adapters.fps.rest

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerTrade
import com.jojos.rebalancingservice.config.properties.FpsApiClientProperties
import com.jojos.rebalancingservice.domain.exception.ConnectionException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@ExtendWith(MockKExtension::class)
internal class SendTradesAdapterTest {
    @MockK
    lateinit var restTemplate: RestTemplate

    @MockK
    lateinit var fpsProperties: FpsApiClientProperties

    @InjectMockKs
    lateinit var sendTrades: SendTradesAdapter

    private val trades = mutableListOf<CustomerTrade>()

    @BeforeEach
    fun setup() {
        every { fpsProperties.url } returns "baseUrl"
        every { fpsProperties.batchSize } returns 2
        trades.add(CustomerTrade(1, 10, 20, -30))
        trades.add(CustomerTrade(2, 20, 40, -60))
    }

    @Test
    fun `should call the rest template`() {
        every {
            restTemplate.postForLocation("baseUrl/execute", any())
        } returns URI("baseUrl/execute")

        sendTrades(trades)
    }

    @Test
    fun `should throw ConnectionException on server error`() {
        every {
            restTemplate.postForLocation("baseUrl/execute", any())
        } throws HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)

        assertThrows<ConnectionException> {
            sendTrades(trades)
        }.also {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, it.status)
        }
    }

    @Test
    fun `should throw ConnectionException if something failed during request`() {
        every {
            restTemplate.postForLocation("baseUrl/execute", any())
        } throws RuntimeException()

        assertThrows<ConnectionException> {
            sendTrades(trades)
        }
    }


}