package com.jojos.rebalancingservice.adapters.fps.rest

import com.jojos.rebalancingservice.adapters.fps.rest.dto.CustomerPortfolio
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
import org.springframework.web.client.getForObject

@ExtendWith(MockKExtension::class)
internal class FetchPortfolioAdapterTest {
    @MockK
    lateinit var restTemplate: RestTemplate

    @MockK
    lateinit var fpsProperties: FpsApiClientProperties

    @InjectMockKs
    lateinit var fetchPortfolio: FetchPortfolioAdapter

    private val response = CustomerPortfolio(1, 10, 20, 40)

    @BeforeEach
    fun setup() {
        every { fpsProperties.url } returns "baseUrl"
    }

    @Test
    fun `should call the rest template`() {
        every {
            restTemplate.getForObject<CustomerPortfolio>("baseUrl/customer/1")
        } returns response

        fetchPortfolio(1)

        Assertions.assertEquals(1, response.customerId)
        Assertions.assertEquals(10, response.stocks)
        Assertions.assertEquals(20, response.bonds)
        Assertions.assertEquals(40, response.cash)
    }

    @Test
    fun `should throw ConnectionException on server error`() {
        every {
            restTemplate.getForObject<CustomerPortfolio>("baseUrl/customer/1")
        } throws HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)

        assertThrows<ConnectionException> {
            fetchPortfolio(1)
        }.also {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, it.status)
        }
    }

    @Test
    fun `should throw ConnectionException if something failed during request`() {
        every {
            restTemplate.getForObject<CustomerPortfolio>("baseUrl/customer/1")
        } throws RuntimeException()

        assertThrows<ConnectionException> {
            fetchPortfolio(1)
        }
    }
}

